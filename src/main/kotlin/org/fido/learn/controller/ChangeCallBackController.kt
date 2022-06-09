package org.fido.learn.controller

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.fido.learn.utils.CommonUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * @author: wangxianfei
 * @description:
 * @date: Created in  2022/6/7
 */
@RestController
@RequestMapping("/callback")
class ChangeCallBackController {

    @Autowired
    private lateinit var okHttpClient: OkHttpClient

    val executor: ExecutorService = Executors.newFixedThreadPool(5) {
        val index = AtomicInteger()
        Thread(it, "demoMultiThreadPool-${index.incrementAndGet()}")
    }

    val singleExecutor: ScheduledExecutorService = Executors.newScheduledThreadPool(1) {
        Thread(it, "singleThreadPool")
    }

    /**
     * 模拟带有异步回调处理逻辑的耗时IO操作
     * 假设这就是别人提供的工具方法
     */
    private fun syncHttpReq(host: String, time: String, callback: (Int) -> Unit) {
        val response = okHttpClient.newCall(
            Request.Builder()
                .url(host)
                .build()
        ).execute()
        return if (response.isSuccessful) {
            val result = String(response.body().bytes()).toInt()
            println(CommonUtils.formatOutput("$time call success, get result is $result"))
            callback(result)
        } else {
            println(CommonUtils.formatOutput("$time call fail"))
        }
    }

    /**
     * 初始传递参数为1，然后拿得到的结果（请求参数递增）来接着进行http请求，进行三次
     */
    @GetMapping("/hell")
    fun changeSyncToAsyncByThreadPool() {
        executor.submit {
            syncHttpReq("http://localhost:8080/main/inc/1", "first") { firstId ->
                syncHttpReq("http://localhost:8080/main/inc/$firstId", "second") { secondId ->
                    syncHttpReq("http://localhost:8080/main/inc/$secondId", "final") { finalId ->
                        println(CommonUtils.formatOutput("final get result $finalId"))
                    }
                }
            }
        }
        println(CommonUtils.formatOutput("doing other work in main http thread....."))
    }

    private suspend fun replaceCallbackWithSuspendFun(host: String, time: String): Int {
        return suspendCoroutine { continuation ->
            syncHttpReq(host, time) { continuation.resume(it) }
        }
    }

    /**
     * 使用协程API、重新包一层工具方法
     * 对于调用方而言，少了嵌套的回调函数，看着像是一个同步代码来处理本身提供的异步回调方法
     */
    @GetMapping("/avoidHell")
    fun replaceCallBack() {
        GlobalScope.launch(executor.asCoroutineDispatcher()) {
            val firstId = replaceCallbackWithSuspendFun("http://localhost:8080/main/inc/1", "first")
            val secondId = replaceCallbackWithSuspendFun("http://localhost:8080/main/inc/$firstId", "second")
            val finalId = replaceCallbackWithSuspendFun("http://localhost:8080/main/inc/$secondId", "final")
            println(CommonUtils.formatOutput("final get result $finalId"))
        }
        println(CommonUtils.formatOutput("doing other work in main http thread ....."))
    }

    /**
     * 让一个单线程一秒钟跑一次打印任务
     * 模拟有一个主工作线程
     */
    @GetMapping("/keepWorkingTrigger")
    fun keepWorkingTrigger() {
        singleExecutor.scheduleAtFixedRate(
            { println(CommonUtils.formatOutput("i am working in single thread pool, at ${System.currentTimeMillis()}")) },
            0,
            1,
            TimeUnit.SECONDS
        )
    }

    @GetMapping("/useFutureButBlock")
    fun useFutureButBlock() {
        GlobalScope.launch(singleExecutor.asCoroutineDispatcher()) {
            println(CommonUtils.formatOutput("doing work in single thread pool..."))
            val future = executor.submit(
                Callable<String> {
                    return@Callable callSyncHttpReqAndGetSuccess()
                }
            )
            val result = future.get()
            println(CommonUtils.formatOutput("switch back to single thread pool,get result is $result,time is ${System.currentTimeMillis()}"))
        }
        println(CommonUtils.formatOutput("doing other work in main http thread ....."))
    }

    @GetMapping("/useAsync")
    fun useAsyncNotBlock() {
        GlobalScope.launch(singleExecutor.asCoroutineDispatcher()) {
            println(CommonUtils.formatOutput("doing work in single thread pool..."))
            val deferred = async(executor.asCoroutineDispatcher()) {
                callSyncHttpReqAndGetSuccess()
            }
            val result = deferred.await()
            println(CommonUtils.formatOutput("switch back to single thread pool,get result is $result,time is ${System.currentTimeMillis()}"))
        }
        println(CommonUtils.formatOutput("doing other work in main http thread ....."))
    }


    /**
     * 利用自带的线程池也可以完成同样的功能
     * 1.需要处理回调
     * 2.需要自己指定用什么线程
     */
    @GetMapping("/useFutureNotBlock")
    fun useFutureNotBlock() {
        GlobalScope.launch(singleExecutor.asCoroutineDispatcher()) {
            println(CommonUtils.formatOutput("doing work in single thread pool..."))
            executor.submit {
                syncHttpReq("http://localhost:8080/main/inc/1", "useFutureNotBlockMethod") {
                    //切回来
                    singleExecutor.execute { println(CommonUtils.formatOutput("switch back to single thread pool,get result is ${it},time is ${System.currentTimeMillis()}")) }
                }
            }
        }
        println(CommonUtils.formatOutput("doing other work in main http thread ....."))
    }

    private fun callSyncHttpReqAndGetSuccess(): String {
        syncHttpReq("http://localhost:8080/main/inc/1", "callSyncHttpReqAndGetSuccess") {}
        return "success"
    }
}
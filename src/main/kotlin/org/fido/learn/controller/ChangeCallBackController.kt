package org.fido.learn.controller

import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.fido.learn.utils.CommonUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
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

    val executor = Executors.newFixedThreadPool(2)

    val singleExecutor = Executors.newScheduledThreadPool(1)

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

    @GetMapping("/replaceBySuspend")
    fun replaceCallBack() {
        GlobalScope.launch(executor.asCoroutineDispatcher()) {
            val firstId = replaceCallbackWithSuspendFun("http://localhost:8080/main/inc/1", "first")
            val secondId = replaceCallbackWithSuspendFun("http://localhost:8080/main/inc/$firstId", "second")
            val finalId = replaceCallbackWithSuspendFun("http://localhost:8080/main/inc/$secondId", "final")
            println(CommonUtils.formatOutput("get final result [${finalId}]"))
        }
        println(CommonUtils.formatOutput("doing other work in main http thread ....."))
    }

    @GetMapping("/switchThread")
    fun switchThread() {
        keepWorking()
        GlobalScope.launch(singleExecutor.asCoroutineDispatcher()) {
            println(CommonUtils.formatOutput("doing work in single thread pool..."))
            val result = withContext(executor.asCoroutineDispatcher()) {
                syncHttpReq("http://localhost:8080/main/inc/1", "test") {}
            }
            println(CommonUtils.formatOutput("switch back to single thread pool,get result is $result,time is ${System.currentTimeMillis()}"))
        }
        println(CommonUtils.formatOutput("doing other work in main http thread ....."))
    }

    @GetMapping("/useAsync")
    fun useAsync() {
        keepWorking()
        GlobalScope.launch(singleExecutor.asCoroutineDispatcher()) {
            println(CommonUtils.formatOutput("doing work in single thread pool..."))
            val result = async(executor.asCoroutineDispatcher()) {
                syncHttpReq("http://localhost:8080/main/inc/1", "test") {}
            }
            println(CommonUtils.formatOutput("switch back to single thread pool,get result is ${result.await()},time is ${System.currentTimeMillis()}"))
        }
        println(CommonUtils.formatOutput("doing other work in main http thread ....."))
    }

    @GetMapping("/useFuture")
    fun useFuture() {
        keepWorking()
        GlobalScope.launch(singleExecutor.asCoroutineDispatcher()) {
            println(CommonUtils.formatOutput("doing work in single thread pool..."))
            val result = executor.submit {
                syncHttpReq("http://localhost:8080/main/inc/1", "test") {}
            }
            println(CommonUtils.formatOutput("switch back to single thread pool,get result is ${result.get()},time is ${System.currentTimeMillis()}"))
        }
        println(CommonUtils.formatOutput("doing other work in main http thread ....."))
    }


    private fun keepWorking() {
        singleExecutor.scheduleAtFixedRate(
            { println(CommonUtils.formatOutput("i am working in single thread pool, at ${System.currentTimeMillis()}")) },
            0,
            1,
            TimeUnit.SECONDS
        )
    }

    private fun syncHttpReq(host: String, time: String, callback: (Int) -> Unit): Int {
        val response = okHttpClient.newCall(
            Request.Builder()
                .url(host)
                .build()
        ).execute()
        if (response.isSuccessful) {
            val result = String(response.body().bytes()).toInt()
            println(CommonUtils.formatOutput("$time call success, get result is $result"))
            callback(result)
            return result
        } else {
            println(CommonUtils.formatOutput("$time call fail"))
        }
        return -1
    }

    private suspend fun replaceCallbackWithSuspendFun(host: String, time: String): Int {
        return suspendCoroutine { continuation ->
            syncHttpReq(host, time) { continuation.resume(it) }
        }
    }
}
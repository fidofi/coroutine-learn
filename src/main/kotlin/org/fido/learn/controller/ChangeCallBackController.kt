package org.fido.learn.controller

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author: wangxianfei
 * @description:
 * @date: Created in  2022/6/6
 */
@RestController
@RequestMapping("/callback-hell")
class CallbackSampleController {

    @Autowired
    private lateinit var okHttpClient: OkHttpClient

    val threadMap = mutableMapOf<String, CoroutineDispatcher>()

    @GetMapping
    fun asyncLoadData() {
        val threadName = Thread.currentThread().name
        firstCall { firstResult ->
            secondCall(firstResult) { secondResult ->
                finalCall(secondResult) { finalResult ->
                    println("get final result [${finalResult}]")
                }
            }
        }
        println("current")
    }

    @GetMapping("/fix-with-coroutine")
    fun asyncLoadDataByCoroutine() {
        GlobalScope.launch {
            val firstId = firstCallWithCoroutine()
            val secondId = secondCallWithCoroutine(firstId)
            val finalId = finalCallWithCoroutine(secondId)
            println("get final result [${finalId}]")
        }
    }


    suspend fun firstCallWithCoroutine(): Int {
        val response = asyncReq("http://localhost:8080/test/1").execute()
        if (response.isSuccessful) {
            println("first call with coroutine success")
            return String(response.body().bytes()).toInt()
        }
        return -1
    }

    suspend fun secondCallWithCoroutine(id: Int): Int {
        val response = asyncReq("http://localhost:8080/test/${id}").execute()
        if (response.isSuccessful) {
            println("second call with coroutine success")
            return String(response.body().bytes()).toInt()
        }
        return -1
    }

    suspend fun finalCallWithCoroutine(id: Int): Int {
        val response = asyncReq("http://localhost:8080/test/${id}").execute()
        if (response.isSuccessful) {
            println("final call with coroutine success")
            return String(response.body().bytes()).toInt()
        }
        return -1
    }

    private fun firstCall(callback: (Int) -> Unit) {
        val response = asyncReq("http://localhost:8080/test/1").execute()
        if (response.isSuccessful) {
            println("first call success")
            val result = String(response.body().bytes()).toInt()
            callback(result)
        } else {
            println("first call fail")
        }
    }

    private fun secondCall(id: Int, callback: (Int) -> Unit) {
        val response = asyncReq("http://localhost:8080/test/${id}").execute()
        if (response.isSuccessful) {
            println("second call success")
            val result = String(response.body().bytes()).toInt()
            callback(result)
        } else {
            println("second call fail")
        }
    }

    private fun finalCall(id: Int, callback: (Int) -> Unit) {
        val response = asyncReq("http://localhost:8080/test/${id}").execute()
        if (response.isSuccessful) {
            println("final call success")
            val result = String(response.body().bytes()).toInt()
            callback(result)
        } else {
            println("final call fail")
        }
    }

    private fun asyncReq(host: String): Call {
        return okHttpClient.newCall(
            Request.Builder()
                .url(host)
                .build()
        )
    }
}
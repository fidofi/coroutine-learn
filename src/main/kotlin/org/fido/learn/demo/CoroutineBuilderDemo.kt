package org.fido.learn.demo

import kotlinx.coroutines.*
import org.fido.learn.utils.CommonUtils

/**
 * @author: wangxianfei
 * @description:
 * @date: Created in  2022/6/9
 */

fun main() {
//    useLaunch()
//    useAsync()
    useRunBlocking()
}

fun useLaunch() {
    println(CommonUtils.formatOutput("Before start coroutine"))
    GlobalScope.launch(Dispatchers.IO) {
        println(CommonUtils.formatOutput("I am in coroutine with launch builder"))
    }
    println(CommonUtils.formatOutput("After start coroutine"))
    CommonUtils.holdProcessNotExit()
}

suspend fun useAsync() {
    println(CommonUtils.formatOutput("Before start coroutine"))
    val result = GlobalScope.async(Dispatchers.IO) {
        println(CommonUtils.formatOutput("I am in coroutine with launch builder"))
        1
    }
    println(CommonUtils.formatOutput("After start coroutine, get result is ${result.await()}"))
    CommonUtils.holdProcessNotExit()
}

fun useRunBlocking() {
    println(CommonUtils.formatOutput("Before start coroutine"))
    runBlocking {
        println(CommonUtils.formatOutput("I am in coroutine with launch builder"))
    }
    println(CommonUtils.formatOutput("After start coroutine"))
}
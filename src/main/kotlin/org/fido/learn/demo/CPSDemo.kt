package org.fido.learn.demo

import org.fido.learn.utils.CommonUtils
import java.util.concurrent.Callable

/**
 * @author: wangxianfei
 * @description:
 * @date: Created in  2022/6/8
 */

fun main() {
    directStyle()
//    cps()
    println(CommonUtils.formatOutput("doing other work...."))
    CommonUtils.holdProcessNotExit()
}

fun directStyle() {
    val a = a()
    val b = b()
    val result = CommonUtils.GLOBAL_EXECUTOR.submit(Callable {
        customPlus(a, b)
    })
    getResult(result.get())
}

fun cps() {
    val a = a()
    val b = b()
    CommonUtils.GLOBAL_EXECUTOR.submit(Callable {
        customPlusWithCallBack(a, b) { getResult(it) }
    })
}

fun a(): Int {
    return 1
}

fun b(): Int {
    return 2
}

fun customPlus(a: Int, b: Int): Int {
    CommonUtils.simulateHttpReq()
    return a + b
}

fun customPlusWithCallBack(a: Int, b: Int, callback: (Int) -> Unit) {
    CommonUtils.simulateHttpReq()
    val result = a + b
    callback(result)
}

fun getResult(result: Int) {
    println(CommonUtils.formatOutput("get the result $result"))
}
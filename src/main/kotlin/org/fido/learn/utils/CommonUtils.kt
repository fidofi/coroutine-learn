package org.fido.learn.utils

import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

/**
 * @author: wangxianfei
 * @description:
 * @date: Created in  2022/6/7
 */
object CommonUtils {

    /**
     * 打印的同时输出当前线程
     */
    fun formatOutput(str: String): String {
        return "[Thread name is : ${Thread.currentThread().name}]: $str"
    }

    /**
     * 让当前进程不要退出
     */
    fun holdProcessNotExit() {
        Thread.sleep(20000)
    }

    /**
     * 模拟一个耗时的IO操作
     */
    fun simulateHttpReq() {
        val url = URL("http://localhost:8080/main/inc/1")
        (url.openConnection() as HttpURLConnection).responseCode
    }

    val GLOBAL_EXECUTOR = Executors.newFixedThreadPool(3)
}
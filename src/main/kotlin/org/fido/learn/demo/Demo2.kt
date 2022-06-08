import org.fido.learn.utils.CommonUtils
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * @author: wangxianfei
 * @description:
 * @date: Created in  2022/6/7
 */

suspend fun main() {
//    test4()
    test5()
}

suspend fun test4() {
    println(CommonUtils.formatOutput(CommonUtils.BEFORE))
    suspendCoroutine<Unit> { continuation ->
        Thread.sleep(2000)
        continuation.resume(Unit)
        println(CommonUtils.formatOutput("want to do other thing"))
    }
    println(CommonUtils.formatOutput(CommonUtils.AFTER))
}


suspend fun test5() {
    println(CommonUtils.formatOutput(CommonUtils.BEFORE))
    suspendCoroutine<Unit> { continuation ->
        thread {
            Thread.sleep(2000)
            continuation.resume(Unit)
        }
        println(CommonUtils.formatOutput("doing other thing..."))
    }
    println(CommonUtils.formatOutput(CommonUtils.AFTER))
}
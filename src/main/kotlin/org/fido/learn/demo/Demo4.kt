import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
    for (i in 1..3) {
        GlobalScope.launch {
            println(CommonUtils.formatOutput(CommonUtils.BEFORE + " $i"))
//            test6()
            test7()
            println(CommonUtils.formatOutput(CommonUtils.AFTER + " $i"))
        }
    }
    CommonUtils.holdProcessNotExit()
}

suspend fun test6() {
    suspendCoroutine<Unit> { continuation ->
        Thread.sleep(2000)
        continuation.resume(Unit)
        println(CommonUtils.formatOutput("want to do other thing..."))
    }
}


suspend fun test7() {
    suspendCoroutine<Unit> { continuation ->
        thread {
            Thread.sleep(2000)
            continuation.resume(Unit)
        }
        println(CommonUtils.formatOutput("doing other thing..."))
    }
}
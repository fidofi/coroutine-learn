import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * @author: wangxianfei
 * @description:
 * @date: Created in  2022/6/7
 */

private val executor = Executors
    .newSingleThreadScheduledExecutor {
        Thread(it, "scheduler")
            .apply { isDaemon = true }
    }

suspend fun main() {
    for (i in 1..3) {
        GlobalScope.launch {
            println(CommonUtils.formatOutput("Before $i"))
            delay(1000)
            println(CommonUtils.formatOutput("After $i,${CommonUtils.simulateHttpReq()}"))
        }
    }
    CommonUtils.holdProcessNotExit()
}


suspend fun delay(timeMillis: Long): Unit =
    suspendCoroutine { cont ->
        executor.schedule({
            cont.resume(Unit)
        }, timeMillis, TimeUnit.MILLISECONDS)
        println(CommonUtils.formatOutput("doing other thing..."))
    }
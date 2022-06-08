import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

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

fun main() {
    for (i in 1..3) {
        GlobalScope.launch {
            println(CommonUtils.formatOutput("Before $i"))
            executor.schedule({
                println(CommonUtils.formatOutput("After $i, ${CommonUtils.simulateHttpReq()}"))
            }, 2, TimeUnit.SECONDS)
            println(CommonUtils.formatOutput("doing other thing..."))
        }
    }
    CommonUtils.holdProcessNotExit()
}
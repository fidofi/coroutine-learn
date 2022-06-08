import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author: wangxianfei
 * @description:
 * @date: Created in  2022/6/7
 */

fun main() {
    for (i in 1..3) {
        GlobalScope.launch {
            println(CommonUtils.formatOutput("Before coroutine $i"))
            println(CommonUtils.formatOutput("After coroutine $i"))
        }
    }
    CommonUtils.holdProcessNotExit()
}
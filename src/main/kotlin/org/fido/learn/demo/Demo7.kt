import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.fido.learn.utils.CommonUtils

/**
 * @author: wangxianfei
 * @description:
 * @date: Created in  2022/6/7
 */
fun main() {
    GlobalScope.launch(Dispatchers.IO) {
        println(CommonUtils.formatOutput("first call"))
        kotlinx.coroutines.delay(500)
        println(CommonUtils.formatOutput("second call"))
        kotlinx.coroutines.delay(500)
        println(CommonUtils.formatOutput("third call"))
        kotlinx.coroutines.delay(500)
    }
    CommonUtils.holdProcessNotExit()
}

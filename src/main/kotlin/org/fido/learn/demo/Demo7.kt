/**
 * @author: wangxianfei
 * @description:
 * @date: Created in  2022/6/7
 */

suspend fun myFunction() {
    println(CommonUtils.formatOutput(CommonUtils.BEFORE))
    kotlinx.coroutines.delay(1000) // suspending
    println(CommonUtils.formatOutput(CommonUtils.AFTER))
}
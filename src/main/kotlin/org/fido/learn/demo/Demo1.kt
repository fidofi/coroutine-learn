import org.fido.learn.utils.CommonUtils
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * @author: wangxianfei
 * @description:
 * @date: Created in  2022/6/7
 */
suspend fun main() {
//    test1()
//    test2()
    test3()
}

fun test1() {
    println(CommonUtils.formatOutput(CommonUtils.BEFORE))
    println(CommonUtils.formatOutput(CommonUtils.AFTER))
}

suspend fun test2() {
    println(CommonUtils.formatOutput(CommonUtils.BEFORE))
    suspendCoroutine<Unit> { }
    println(CommonUtils.formatOutput(CommonUtils.AFTER))
}


suspend fun test3() {
    println(CommonUtils.formatOutput(CommonUtils.BEFORE))
    suspendCoroutine<Unit> { continuation ->
        continuation.resume(Unit)
    }
    println(CommonUtils.formatOutput(CommonUtils.AFTER))
}
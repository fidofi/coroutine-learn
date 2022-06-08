import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

/**
 * @author: wangxianfei
 * @description:
 * @date: Created in  2022/6/7
 */
object CommonUtils {
    fun formatOutput(str: String): String {
        return "[Thread name: ${Thread.currentThread().name}]: $str"
    }

    fun holdProcessNotExit() {
        Thread.sleep(20000)
    }

    @OptIn(ExperimentalTime::class)
    fun simulateHttpReq(): String {
        return (measureTime {
            val url =
                URL("http://ccloud-dev.gz.cvte.cn/serviceMap/getServiceMapData?appId=278bd39c25e24b8f6fe8cf2cd08d0e77fda46cd5&from=1654601879417&to=1654605479417&callerDepth=1&calleeDepth=1")
            val connection = url.openConnection() as HttpURLConnection
            connection.responseCode
        }.toLongMilliseconds()).toString()
    }

    private fun readData(inputStream: InputStream): String {
        val reader = InputStreamReader(inputStream, "UTF8")
        val br = BufferedReader(reader)
        val sb = StringBuilder()
        var context: String? = null
        while (context != null) {
            context = br.readLine()
            sb.append(context)
        }
        return sb.toString()
    }

    const val BEFORE = "Before"
    const val AFTER = "After"
    const val SUSPEND = "Suspended"
    const val RESUME = "Resumed"
}
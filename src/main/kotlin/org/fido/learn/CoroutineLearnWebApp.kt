package org.fido.learn

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * @author: wangxianfei
 * @description:
 * @date: Created in  2022/5/14
 */
@SpringBootApplication
class CoroutineLearnWebApp {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<CoroutineLearnWebApp>(*args)
        }
    }
}
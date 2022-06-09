package org.fido.learn.controller

import org.fido.learn.utils.CommonUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author: wangxianfei
 * @description:
 * @date: Created in  2022/6/7
 */
@RequestMapping("/main")
@RestController
class MainController {
    @GetMapping("/inc/{id}")
    fun getInt(@PathVariable id: Int): Int {
        //模拟耗时
        println(CommonUtils.formatOutput("i am in [MainController]"))
        Thread.sleep(3000)
        return id + 1
    }
}
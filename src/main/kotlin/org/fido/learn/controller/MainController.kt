package org.fido.learn.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author: wangxianfei
 * @description:
 * @date: Created in  2022/6/7
 */
@RequestMapping("/test")
@RestController
class TestController {
    @GetMapping("/{id}")
    fun test(@PathVariable id: Int): Int {
        return id + 1
    }
}
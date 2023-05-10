package com.example.springkt.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping



class CalcController {
    // 루트 경로로 접속(GET)하면 calcTemplate.html 에 해당되는 템플릿을 랜더링(반환)
    @GetMapping("/")
    fun home()="calc"
    // 루트 경로로 접속(POST)하면 num1 과 num2 를 받아서 더한 값을 result 에 담아서
    // calc.html 에 해당되는 템플릿을 랜더링(반환)

    @PostMapping("/")
    fun calc(model: Model, num1 : Int, num2: Int) : String {

        model.addAttribute("num1", num1)
        model.addAttribute("num2", num2)
        model.addAttribute("result", num1 + num2)

        return "calc"
    }
}
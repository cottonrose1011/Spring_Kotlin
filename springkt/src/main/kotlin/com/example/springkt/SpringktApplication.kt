package com.example.springkt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

@SpringBootApplication
class SpringktApplication

fun main(args: Array<String>) {
	runApplication<SpringktApplication>(*args)
}

var counter = 1

@Controller
class MainController {

	@GetMapping("/")
	@ResponseBody
	fun index(): String {
		return "${counter++} 번째 접속자"
	}
}
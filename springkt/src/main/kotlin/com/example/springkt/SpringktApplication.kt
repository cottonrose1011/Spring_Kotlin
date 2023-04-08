package com.example.springkt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody

@SpringBootApplication
class SpringktApplication

fun main(args: Array<String>) {
	runApplication<SpringktApplication>(*args)
}

var news = mutableListOf("2030부산세계박람회 유치기원 ‘불꽃쇼’ 안전 총력 대응! ", "커터칼로 초등생 목 그은 고등학생 긴급체포", "광화문광장서 신원 미상 중년 남성 분신...중태 빠져")



class MainController {
	@GetMapping("/")
	fun home(model: Model): String {
		model.addAttribute("newsList", news)
		return "news"
	}

	@PostMapping("/")
	fun add(txt: String): String {
		news.add(txt)
		return "redirect:/"
	}

}
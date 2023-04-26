package com.example.springkt.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

class RestfulAPI {

}
var news = mutableListOf(
    News("누리호 5월 24일 3차 발사…인공위성 8개 싣고 우주로",
        "https://n.news.naver.com/mnews/article/448/0000404277?sid=105"),
    News("‘넷플릭스’ 잡으려다 ‘누누티비’까지 등장… 적자폭 껑충 뛴 토종 OTT",
        "https://n.news.naver.com/mnews/article/366/0000892375?sid=105"),
    News("The end of a myth: Distributed transactions can scale",
        "https://muratbuffalo.blogspot.com/2023/04/the-end-of-myth-distributed.html"),
    News("Getting the ^D", "https://owengage.com/writing/2023-04-08-getting-the-ctrl-d/")
)


@Controller
@RequestMapping("/api")
class RestfulAPIController{
    @GetMapping("/")
    @ResponseBody
    fun main() = news

    @PostMapping("/")
    @ResponseBody
    fun createNews(@RequestBody req: NewsRequest) : Boolean {
        news.add(News(req.title, req.address))
        return true;
    }

    @DeleteMapping("/{idx}")
    @ResponseBody
    fun deleteNews(@PathVariable idx: Int) : Boolean{
        Result.runCatching { news.removeAt(idx) } //오류 방지

        return true;
    }
}
data class News(val title: String, val address: String)
data class NewsRequest(val title: String, val address: String)
//Tomcat에서 파라미터를 바로 보내면 알아서 처리하지만 JSON은 따로 파싱을 해줘야
//함. RequestBody 어노테이션을 해줌
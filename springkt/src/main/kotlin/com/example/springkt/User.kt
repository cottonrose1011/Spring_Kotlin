package com.example.springkt

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Repository
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.security.MessageDigest
import java.time.LocalDateTime
import java.util.*

// 로그인 DB 연동 예시를 위한 엔티티와 리포지토리
@Entity
data class UserEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    val username: String,
    val password: String,
    val name: String
)

@Repository
interface UserRepo : CrudRepository<UserEntity, Long> {
    fun findByUsername(username: String): Optional<UserEntity>
    fun findByUsernameAndPassword(username: String, password: String): Optional<UserEntity>
}

// 세션 예시를 위한 전역변수
// Key => 세션키, Value => 세션 데이터
var sessionStorage = mutableMapOf<String, Map<String, String>>()

@Controller
@RequestMapping("/user")
class UserController(@Autowired val userRepo: UserRepo) {
    // 비밀번호 안전 저장을 위한 해싱 함수
    fun sha512Hasher(str: String): String {
        val bytes = MessageDigest.getInstance("SHA-512").digest(str.toByteArray())
        val hexString = StringBuilder(128)
        for (i in bytes.indices) {
            val hex = Integer.toHexString(0xff and bytes[i].toInt())
            if (hex.length == 1) hexString.append('0')
            hexString.append(hex)
        }
        return hexString.toString()
    }

    // 쿠키 저장을 위한 예시 페이지
    @GetMapping("/cookie")
    fun cookie(model: Model): String {
        return "cookie"
    }

    @PostMapping("/cookie")
    fun setCookie(model: Model, cookieKey: String, cookieVal: String, req: HttpServletResponse): String {
        req.addCookie(Cookie(cookieKey, cookieVal))

        return "redirect:/user/"
    }

    // 세션 저장의 내부 원리를 표현하기 위한 페이지
    @GetMapping("/pseudo")
    fun sessionPseudo(
        model: Model, @CookieValue("session_key") sessionKey: String?, resp: HttpServletResponse
    ): String {
        if (sessionKey == null || !sessionStorage.containsKey(sessionKey)) {
            val key = UUID.randomUUID().toString()
            resp.addCookie(Cookie("session_key", key))
            sessionStorage[key] = mapOf("last_seen" to Date().toString())
            model.addAttribute("sess", sessionStorage[key]!!.toList())
            println(sessionStorage[key]!!.toList())
        } else {
            sessionStorage[sessionKey] = sessionStorage[sessionKey]!!.plus("last_seen" to Date().toString())
            model.addAttribute("sess", sessionStorage[sessionKey]!!.toList())
        }

        return "session"
    }

    @PostMapping("/pseudo")
    fun setSession(
        model: Model,
        key: String,
        value: String,
        @CookieValue("session_key") sessionKey: String,
        resp: HttpServletResponse
    ): String {
        sessionStorage[sessionKey] = sessionStorage[sessionKey]!!.plus(key to value)
        model.addAttribute("sess", sessionStorage[sessionKey]!!.toList())

        return "redirect:/user/pseudo"
    }


    // 세션을 이용한 로그인 구현 예시
    @GetMapping("/")
    fun index(
        model: Model, session: HttpSession, @CookieValue("cookie_name") cookie: String?, req: HttpServletRequest
    ): String {
        model.addAttribute("cookie", cookie)
        model.addAttribute("cookie_list", req.cookies.map { it.name to it.value })
        model.addAttribute("user", session.getAttribute("user") as UserEntity?)
        return "user"
    }

    @GetMapping("/login")
    fun login(model: Model): String {
        return "login"
    }

    @PostMapping("/login")
    fun loginPost(model: Model, username: String, password: String, session: HttpSession): String {
        val user = userRepo.findByUsernameAndPassword(username, sha512Hasher(password))
        if (user.isEmpty) {
            model.addAttribute("error", "User not found")
            return "login"
        }

        session.setAttribute("user", user.get())
        return "redirect:/user/"
    }

    @GetMapping("/register")
    fun register(model: Model): String {
        return "register"
    }

    @PostMapping("/register")
    fun registerPost(model: Model, username: String, password: String, name: String): String {
        val user = userRepo.findByUsername(username)
        if (user.isPresent) {
            model.addAttribute("error", "User already exists")
            return "register"
        }

        userRepo.save(UserEntity(username = username, password = sha512Hasher(password), name = name))
        return "redirect:/user/login/"
    }
}
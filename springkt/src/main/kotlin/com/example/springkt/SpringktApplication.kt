package com.example.springkt

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Repository
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@SpringBootApplication
class SpringktApplication

fun main(args: Array<String>) {
	runApplication<SpringktApplication>(*args)
}

@Entity
data class NewsEntity(
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Int? = null,
	val title: String,
	val address: String
)

@Repository
interface NewsRepository : CrudRepository<NewsEntity, Int> {
	fun findByTitleContainingIgnoreCaseOrAddressContainingIgnoreCase(title: String, address: String): List<NewsEntity>
}

@Controller
class MainController(@Autowired val newsRepository: NewsRepository){
	@GetMapping("/")
	fun index(model: Model) : String{
		model.addAttribute("newsList", newsRepository.findAll())
		return "news"
	}

	@PostMapping("/")
	fun write(title: String, address: String): String{
		val newData = NewsEntity(null, title, address)
		newsRepository.save(newData)

		return "news"
	}

	}
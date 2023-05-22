package com.example.springkt

import jakarta.persistence.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Repository
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@Entity
data class ArticleEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    @Column(length = 60, nullable = false) val title: String,
    @Lob @Basic(fetch = FetchType.LAZY) @Column(nullable = false) val content: String,
    @Column(nullable = false) val created: LocalDateTime = LocalDateTime.now(),
)

@Repository
interface ArticleRepository : CrudRepository<ArticleEntity, Long>

@Entity
data class CommentEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    @JoinColumn(name = "article_id", nullable = false) @ManyToOne val article: ArticleEntity,

    @Lob @Basic(fetch = FetchType.LAZY) @Column(nullable = false) val content: String,
    @Column(nullable = false) val created: LocalDateTime = LocalDateTime.now(),
)

@Repository
interface CommentRepository : CrudRepository<CommentEntity, Long> {
    fun findAllByArticleId(articleId: Long): List<CommentEntity>
}

@Controller
class BoardController(@Autowired val articleRepo: ArticleRepository, @Autowired val commentRepo: CommentRepository) {
    @GetMapping("/")
    fun articleList(model: Model): String {
        model.addAttribute("articles", articleRepo.findAll())
        return "articleList"
    }


    @PostMapping("/write")
    fun articleCreate(article: ArticleEntity): String {
        articleRepo.save(article)
        return "redirect:/"
    }

    @GetMapping("/write")
    fun articleCreateForm(): String {
        return "articleCreateForm"
    }

    @GetMapping("/article/{articleId}")
    fun articleRead(model: Model, @PathVariable articleId: Long): String {
        val entityOptional = articleRepo.findById(articleId)
        if (entityOptional.isEmpty) {
            return "redirect:/"
        }


        val article = entityOptional.get()
        model.addAttribute("article", article)
        model.addAttribute("comments", commentRepo.findAllByArticleId(articleId))
        return "articleRead"
    }
    @PostMapping("/article/{articleId}/comment")
    fun commentCreate(content: String, @PathVariable articleId: Long): String {
        val entityOptional = articleRepo.findById(articleId)
        if (entityOptional.isEmpty) {
            return "redirect:/"
        }

        commentRepo.save(CommentEntity(article = entityOptional.get(), content = content))
        return "redirect:/article/$articleId"
    }


    @DeleteMapping("/article/{articleId}")
    @ResponseBody
    fun articleDelete(@PathVariable articleId: Long): Boolean {
        return Result.runCatching { articleRepo.deleteById(articleId) }.isSuccess
    }
}




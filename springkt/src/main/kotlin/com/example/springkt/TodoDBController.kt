package com.example.springkt

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Repository
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Entity
data class TodoEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    @Column(length = 60, nullable = false) val title: String
)

@Repository
interface TodoRepository : CrudRepository<TodoEntity, Int>

@Controller
class TodoDBController(@Autowired val todoRepo : TodoRepository) {

    @GetMapping("/todo")
    fun showTodo3(model: Model) : String {
        model.addAttribute("todoList", todoRepo.findAll())
        return "todoDB"
    }

    @PostMapping("/todo")
    fun addTodo3(@RequestParam newTitle: String) : String {
        todoRepo.save(TodoEntity(title = newTitle))
        return "redirect:/todo"
    }

    @DeleteMapping("/todo")
    @ResponseBody
    fun deleteTodo(@RequestBody delList : List<Int>) {
        for (id in delList) {
            todoRepo.deleteById(id)
        }
    }
}
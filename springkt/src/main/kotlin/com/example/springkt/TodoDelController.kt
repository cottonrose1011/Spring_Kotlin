package com.example.springkt

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

var count : Int = 0
data class Todo(val id: Int, val title: String)

var todoList = mutableListOf(
    Todo(count++,"공부"),
    Todo(count++,"식사"),
    Todo(count++,"회의")
)

@Controller
class TodoDelController {

    @GetMapping("/todo")
    fun showTodo2(model: Model) : String{
        model.addAttribute("todoList", todoList)
        return "todoDel"
    }

    @PostMapping("/todo")
    fun addTodo2(newTodo: String): String{
        todoList.add(Todo(count++, newTodo))
        return "redirect:/todo"
    }


}
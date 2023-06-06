package com.example.springkt

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping


var todo = mutableListOf(
    "공부",
    "청소",
    "운동"
    )

//@Controller
class TodoController {

    @GetMapping("/todo")
    fun showTodo(model: Model) : String{
        model.addAttribute("todo", todo)
        return "todo"
    }

    @PostMapping("/todo")
    fun addTodo(newTodo: String) : String {
        todo.add(newTodo)
        return "redirect:/todo"
    }
}


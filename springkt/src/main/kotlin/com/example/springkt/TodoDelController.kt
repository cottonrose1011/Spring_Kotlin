package com.example.springkt

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

var count : Int = 0
data class Todo(val id: Int, val title: String)

var todoList = mutableListOf(
    Todo(count++,"공부"),
    Todo(count++,"식사"),
    Todo(count++,"회의")
)

//@Controller
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

    @DeleteMapping("/todo")
    @ResponseBody
    fun deleteTodo(@RequestBody delList: List<Int>) {
        Result.runCatching { for (ids in delList){ todoList.removeAt(ids) } }
        count--; //todoList의 index number와 일치 시키기 위함, 만약 count--를 하지 않고 3개중 하나를 삭제한 다음,
                //하나를 다시 추가하면 그 리스트의 id는 3인데 현재 todoList는 0,1,2 까지만 있어 Index 오류가 발생하여 삭제를 하지않음.
    }
}
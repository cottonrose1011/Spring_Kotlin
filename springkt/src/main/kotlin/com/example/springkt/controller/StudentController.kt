package com.example.springkt

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

data class Student(
    val id: Int,
    val name: String,
    val age : Int,
    val enroll: String,
    val score: Int
)

val students = mutableListOf<Student>()


class StudentController {

    @GetMapping("/")
    fun index(model: Model) : String {
    // Model 은 Dictionary 와 같은 형태로 Key-Value 형태로 데이터를 저장할 수 있다.
    // Model 에 학생 목록을 집어넣는다. (key: students, value: students)
        model.addAttribute("students", students)
        return "studentTable"
    }

    @PostMapping("/stud")
    fun add(id: Int, name: String, age: Int, enroll: String, score: Int): String{

        students.add(Student(id, name, age, enroll, score))

        return "redirect:/"
    }



}
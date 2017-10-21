package com.example.controller

import com.example.model.User
import com.example.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.PathVariable


@RestController
class UserController {

    @Autowired
    lateinit var userService: UserService

    @GetMapping("/user/{id}")
    internal fun getUser(@PathVariable id: Long): ResponseEntity<User> {
        return userService.getUser(id)
    }

    @GetMapping("/users")
    internal fun getAllUsers(): ResponseEntity<List<User>> {
        return userService.getAllUser()
    }

    @PutMapping("/user/{id}")
    internal fun putUserLove(@PathVariable id: Long, @RequestBody user: User): ResponseEntity<User> {
        return userService.putUser(id, user)
    }

    @PostMapping("/user")
    internal fun postUser(@RequestBody user: User): ResponseEntity<User> {
        return userService.postUser(user)
    }

    @PostMapping("/login")
    internal fun login(@RequestBody user: User): ResponseEntity<String> {
        var result = userService.login(user)
        return ResponseEntity.status(if (result == "") HttpStatus.OK else HttpStatus.UNAUTHORIZED).body(result)
    }
}


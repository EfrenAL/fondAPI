package com.example.service

import com.example.model.User
import com.example.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    fun getAllUser(): ResponseEntity<List<User>> {
        var users: List<User>? = userRepository.findAll()
        return ResponseEntity.status(if (users != null) HttpStatus.OK else HttpStatus.BAD_REQUEST).body(users)
    }

    fun getUser(id: Long): ResponseEntity<User> {
        var user = userRepository.findUser(id)
        return ResponseEntity.status(if (user != null) HttpStatus.OK else HttpStatus.BAD_REQUEST).body(user)
    }

    fun putUser(id: Long, user: User): ResponseEntity<User> {
        var user = userRepository.putUser(id, user)
        return ResponseEntity.status(if (user != null) HttpStatus.OK else HttpStatus.BAD_REQUEST).body(user)
    }

    fun postUser(user: User): ResponseEntity<User> {
        var user = userRepository.postUser(user)
        return ResponseEntity.status(if (user != null) HttpStatus.OK else HttpStatus.BAD_REQUEST).body(user)
    }

    fun login(user: User): String {
        return userRepository.login(user)
    }

}
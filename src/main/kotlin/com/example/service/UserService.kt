package com.example.service

import com.example.model.User
import com.example.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    fun getAllUser(): List<User>{
        System.out.println("In service")
        return userRepository.findAll();
    }
}
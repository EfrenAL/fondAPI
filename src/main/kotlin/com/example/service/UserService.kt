package com.example.service

import com.example.model.User
import com.example.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.sql.DataSource


@Service
class UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    fun getAllUser(dbUrl: String?, dataSource: DataSource): List<User>{
        System.out.println("In service")
        return userRepository.findAll(dbUrl, dataSource);
    }
}
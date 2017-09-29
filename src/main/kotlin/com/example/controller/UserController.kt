package com.example.controller

import com.example.model.Greeting
import com.example.model.User
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.*
import java.sql.SQLException
import java.util.concurrent.atomic.AtomicLong
import javax.sql.DataSource

@RestController
class UserController {

    @Value("\${spring.datasource.url}")
    private var dbUrl: String? = null

    @Autowired
    lateinit private var dataSource: DataSource

    val counter = AtomicLong()

    @GetMapping("/user/{id}")
    internal fun getUser():User{
        return User(1,"Marioo", "pio pio", "Muyayo", "" )
    }

    @PostMapping("/user")
    internal fun setUser(@RequestBody user: User ):User{

        val connection = dataSource.getConnection()
        try{
            val stmt = connection.createStatement()
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS user (name varchar(255), " +
                    "lastName varchar(255), " +
                    "nickName varchar(255), " +
                    "picture varchar(255))")

            stmt.executeUpdate("INSERT INTO user VALUES +"+ user + ")")
            return user

        } catch (e: Exception) {

        }

        return User(counter.incrementAndGet(), "Marioo", "pio pio", "Muyayo", "" )
    }
}


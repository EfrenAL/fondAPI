package com.example.controller

import com.example.model.Greeting
import com.example.model.User
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.*
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
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

    @GetMapping("/users")
    internal fun getAllUser(): ResultSet? {
        val connection = dataSource.getConnection()
        try{
            val stmt = connection.createStatement()
            checkDb(stmt)
            val rs = stmt.executeQuery("SELECT * FROM users")
            return rs

        } catch (e: Exception) {

        }
        return null;
    }


    @PostMapping("/user")
    internal fun setUser(@RequestBody user: User ):User{

        val connection = dataSource.getConnection()
        System.out.println("Connection created");
        try{
            val stmt = connection.createStatement()
            checkDb(stmt)
            System.out.println("DB checked");
            stmt.executeUpdate("INSERT INTO user " +
                    "(id, name, lastName, nickName, picture) VALUES " +
                    "("+ counter.incrementAndGet() + "," +
                    user.name + ","+
                    user.lastName + "," +
                    user.nickName + ","+
                    user.picture + ")")
            System.out.println("Element inserted");
            return user

        } catch (e: Exception) {
            System.out.println("Exception: " +  e);
        }

        return User(counter.incrementAndGet(), "Marioo", "pio pio", "Muyayo", "" )
    }

    fun checkDb(stmt: Statement){
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (name varchar(255), " +
                "lastName varchar(255), " +
                "nickName varchar(255), " +
                "picture varchar(255))")
    }

    @Bean
    @Throws(SQLException::class)
    fun dataSource(): DataSource {
        if (dbUrl?.isEmpty() ?: true) {
            return HikariDataSource()
        } else {
            val config = HikariConfig()
            config.jdbcUrl = dbUrl
            return HikariDataSource(config)
        }
    }
}


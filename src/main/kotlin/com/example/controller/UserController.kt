package com.example.controller

import com.example.model.Greeting
import com.example.model.User
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
    internal fun getUser(@PathVariable id: Long):ResponseEntity<User>{
        val connection = dataSource.getConnection()
        try{
            val stmt = connection.createStatement()
            checkDb(stmt)
            System.out.println("Conection with the db successful")
            val rs = stmt.executeQuery("SELECT * FROM users WHERE id = "+ id + "")
            System.out.println("Select performed: " + rs);
            return ResponseEntity.ok(User(rs.getInt("id"), rs.getString("name"),rs.getString("lastName"), rs.getString("nickName"), rs.getString("picture")))

        } catch (e: Exception) {
            System.out.println("Error: " + e );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/users")
    internal fun getAllUser(): ResponseEntity<ArrayList<User>> {
        val connection = dataSource.getConnection()
        val array = ArrayList<User>()

        try{
            val stmt = connection.createStatement()
            checkDb(stmt)
            System.out.println("Conection with db sucessfully")
            val rs = stmt.executeQuery("SELECT * FROM users")
            System.out.println("Request performed: " + rs )
            while (rs.next()) {
                array.add(User(rs.getInt("id"), rs.getString("name"),rs.getString("lastName"), rs.getString("nickName"), rs.getString("picture")))
                System.out.println("Element inserted into the array" )
            }
            return ResponseEntity.ok(array)

        } catch (e: Exception) {

        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }


    @PostMapping("/user")
    internal fun setUser(@RequestBody user: User ):User{

        val connection = dataSource.getConnection()
        System.out.println("Connection created");
        try{
            val stmt = connection.createStatement()
            checkDb(stmt)
            System.out.println("DB checked");
            stmt.executeUpdate("INSERT INTO users " +
                    "(name, lastName, nickName, picture) VALUES " +
                    "('"+ user.name      + "','" +
                         user.lastName  + "','" +
                         user.nickName  + "','" +
                         user.picture   + "')")
            System.out.println("Element inserted");
            return user

        } catch (e: Exception) {
            System.out.println("Exception: " +  e);
        }

        return User(1, "Marioo", "pio pio", "Muyayo", "" )
    }

    fun checkDb(stmt: Statement){
        stmt.executeUpdate("DROP TABLE IF EXISTS table_name")
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users ( " +
                "id SERIAL PRIMARY KEY, " +
                "name varchar(255), " +
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


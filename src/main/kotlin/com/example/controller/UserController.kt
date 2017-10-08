package com.example.controller

import com.example.model.User
import com.example.service.UserService
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.sql.SQLException
import java.sql.Statement
import java.util.concurrent.atomic.AtomicLong
import javax.sql.DataSource
import org.springframework.web.bind.annotation.PathVariable


@RestController
class UserController {

    @Value("\${spring.datasource.url}")
    private var dbUrl: String? = null

    @Autowired
    lateinit private var dataSource: DataSource

    @Autowired
    lateinit var userService: UserService

    val counter = AtomicLong()

    /*@GetMapping("/user/{id}")
    internal fun getUser(@PathVariable id: Long):ResponseEntity<User>{
        val connection = dataSource.getConnection()
        var user: User = User()
        try{
            val stmt = connection.createStatement()
            checkDb(stmt)
            System.out.println("Conection with the db successful")
            val rs = stmt.executeQuery("SELECT * FROM users WHERE id = "+ id)
            System.out.println("Select performed successfuly");
            while(rs.next()){
                user = User(rs.getInt("id"), rs.getString("name"),rs.getString("lastName"), rs.getString("nickName"), rs.getInt("love"), rs.getString("picture"))
            }
            connection.close()
            return ResponseEntity.ok(user)

        } catch (e: Exception) {
            System.out.println("Error: " + e );
            connection.close()
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }*/


    @GetMapping("/allusers")
    internal fun getUserNew():ResponseEntity<List<User>>{
        System.out.println("In the controller")
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUser(dbUrl, dataSource));
    }



    /*@PutMapping("/user/{id}")
    fun giveLove(@PathVariable id: Long, @RequestBody user: User): ResponseEntity<User> {
        val connection = dataSource.getConnection()

        try{
            val stmt = connection.createStatement()
            checkDb(stmt)
            System.out.println("Conection with the db successful")
            user.love = user.love + 1;
            System.out.println("Query: " + "UPDATE users " + "SET love = " + (user.love) + " WHERE id = "+ user.id)
            val rs = stmt.executeUpdate("UPDATE users " +
                    "SET love = " + (user.love) + " WHERE id = "+ user.id)

            System.out.println("User with id: " + user.id + " updated to love: " + user.love + " correctly");

            connection.close()
            return ResponseEntity.status(HttpStatus.OK).body(user)

        } catch (e: Exception) {
            System.out.println("Error: " + e );
            connection.close()
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }*/


    @GetMapping("/users")
    internal fun getAllUser(): ResponseEntity<ArrayList<User>> {
        val connection = dataSource.getConnection()
        val list: kotlin.collections.ArrayList<User> = java.util.ArrayList()

        try{
            val stmt = connection.createStatement()
            checkDb(stmt)
            System.out.println("Conection with db sucessfully")
            val rs = stmt.executeQuery("SELECT * FROM users")
            System.out.println("Request performed successfully")
            while (rs.next()) {
                list.add(User(rs.getInt("id"), rs.getString("name"),rs.getString("lastName"), rs.getString("nickName"), rs.getInt("love"), rs.getString("picture")))
            }
            connection.close()
            return ResponseEntity.ok(list)

        } catch (e: Exception) {
            connection.close()
            System.out.println("Exception: " + e )
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

    /*@GetMapping("/refresh")
    internal fun refreshDB(): ResponseEntity<Int> {
        val connection = dataSource.getConnection()
        val stmt = connection.createStatement()
        stmt.executeUpdate("DROP TABLE IF EXISTS users")
        //System.out.println("DB refreshed")
        return ResponseEntity.status(HttpStatus.OK).body(1);
    }*/

    /*@PostMapping("/user")
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
            connection.close()
            return user

        } catch (e: Exception) {
            System.out.println("Exception: " +  e);
            connection.close()
            return User(1, "Marioo", "pio pio", "Muyayo", 0, "" )
        }
    }*/

    fun checkDb(stmt: Statement){
        //stmt.executeUpdate("DROP TABLE IF EXISTS users")
        //System.out.println("DB refreshed")
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users ( " +
                "id SERIAL PRIMARY KEY, " +
                "name varchar(255), "     +
                "lastName varchar(255), " +
                "nickName varchar(255), " +
                "love integer, "          +
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


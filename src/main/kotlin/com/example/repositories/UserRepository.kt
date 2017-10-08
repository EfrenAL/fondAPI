package com.example.repositories

import com.example.model.User
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Repository
import java.sql.SQLException
import java.sql.Statement
import javax.sql.DataSource

@Repository
//interface UserRepository : JpaRepository<User, Long> {}
class UserRepository {

    @Value("\${spring.datasource.url}")
    private var dbUrl: String? = null

    @Autowired
    lateinit private var dataSource: DataSource


    fun findAll(): List<User>{
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
            return list

        } catch (e: Exception) {
            connection.close()
            System.out.println("Exception: " + e )
            return ArrayList<User>()
        }
    }

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
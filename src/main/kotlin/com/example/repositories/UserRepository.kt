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
import java.sql.DriverManager
import java.net.URISyntaxException
import java.sql.Connection


//interface UserRepository : JpaRepository<User, Long> {}
@Repository
class UserRepository {


    @Value("\${spring.datasource.url}")
    private var dbUrl: String? = null

    @Autowired
    lateinit private var dataSource: DataSource

    fun findAll(): List<User> {

        val list: ArrayList<User> = ArrayList()
        val connection = createConnection()
        var stmt = connectWithDb(connection)

        val rs = stmt.executeQuery("SELECT * FROM users")
        System.out.println("Request performed successfully")
        while (rs.next()) {
            list.add(User(rs.getInt("id"), rs.getString("name"), rs.getString("lastName"), rs.getString("nickName"), rs.getString("email"), "", rs.getInt("love"), rs.getString("picture")))
        }
        connection.close()
        return list

    }

    fun findTopUser(num: Int): List<User> {

        val list: ArrayList<User> = ArrayList()
        val connection = createConnection()
        var stmt = connectWithDb(connection)

        val rs = stmt.executeQuery("SELECT * FROM users")
        System.out.println("Request performed successfully")
        while (rs.next()) {
            list.add(User(rs.getInt("id"), rs.getString("name"), rs.getString("lastName"), rs.getString("nickName"), rs.getString("email"), "", rs.getInt("love"), rs.getString("picture")))
        }

        connection.close()

        val subList = list.sortedWith(compareBy({ it.love })).subList(list.size-num,list.size).reversed()

        return subList
    }

    fun findUser(id: Long): User? {

        var user: User = User()
        val connection = createConnection()
        var stmt = connectWithDb(connection)

        val rs = stmt.executeQuery("SELECT * FROM users WHERE id = " + id)

        log("All users found successfully");
        while (rs.next()) {
            user = User(rs.getInt("id"), rs.getString("name"), rs.getString("lastName"), rs.getString("nickName"), rs.getString("email"), "", rs.getInt("love"), rs.getString("picture"))
        }
        connection.close()
        return user
    }

    fun putUser(id: Long, user: User): User? {

        val connection = createConnection()
        var stmt = connectWithDb(connection)

        val rs = stmt.executeUpdate("UPDATE users " + "SET love = love + 1 WHERE id = " + user.id)

        System.out.println("User with id: " + user.id + " updated to love: " + user.love + " correctly");
        connection.close()
        return user
    }

    fun postUser(user: User): User? {

        val connection = createConnection()
        var stmt = connectWithDb(connection)

        stmt.executeUpdate("INSERT INTO users " +
                "(name, lastName, email, password, nickName, picture) VALUES " +
                "('" + user.name + "','" +
                user.lastName + "','" +
                user.email + "','" +
                user.password + "','" +
                user.nickName + "','" +
                user.picture + "')")

        log("Element inserted");

        connection.close()

        return user
    }

    fun login(user: User): String {
        var response: String = ""
        var password: String = "-1"
        log("On login method mail: " + user.email + " password: " + user.password)
        val connection = createConnection()
        var stmt = connectWithDb(connection)

        val rs = stmt.executeQuery("SELECT password FROM users WHERE email = '" + user.email + "'")
        System.out.println("Request performed successfully")
        while (rs.next()) {
            password = rs.getString("password")
        }
        connection.close()

        if (password != user.password)
            response = "Authentication problem"
        log("Login response: " + response);
        return response
    }

    fun createConnection(): Connection {
        val connection: Connection = getConnection()    //Local
        //val connection = dataSource.getConnection()   //Production
        return connection
    }

    fun connectWithDb(connection: Connection): Statement {
        var stmt = connection.createStatement()
        createTable(stmt)
        log("Conection with the db successful")
        return stmt
    }

    fun createTable(stmt: Statement) {
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users ( " +
                "id SERIAL PRIMARY KEY, " +
                "email varchar(255) UNIQUE, " +
                "name varchar(255), " +
                "lastName varchar(255), " +
                "nickName varchar(255), " +
                "password varchar(255), " +
                "love integer, " +
                "picture varchar(255))")
    }

    fun dropTable(stmt: Statement) {
        stmt.executeUpdate("DROP TABLE IF EXISTS users")
        System.out.println("DB refreshed")
    }

    fun log(string: String) {
        System.out.println(string)
    }

    @Throws(URISyntaxException::class, SQLException::class)
    private fun getConnection(): Connection {
        //val dbUrl = System.getenv("JDBC_DATABASE_URL")
        val dbUrl = "jdbc:postgresql://ec2-54-163-235-175.compute-1.amazonaws.com:5432/d1223th2qe00mu?user=ycvjcjtdovkwpp&password=d2d9acc363549dec388e76e22ba620c8b0970b19b66974ccc817c967211c4818&sslmode=require"
        return DriverManager.getConnection(dbUrl)
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


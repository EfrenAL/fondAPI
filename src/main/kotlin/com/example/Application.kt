package com.example

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

//@SpringBootApplication(scanBasePackages = arrayOf("com.example.repositories","com.example.model","com.example.controller","com.example.service"))
//@EnableAutoConfiguration(exclude = arrayOf(DataSourceAutoConfiguration::class, HibernateJpaAutoConfiguration::class))
//@EnableJpaRepositories("com.example.repositories")
//@EntityScan("com.example.model")

@SpringBootApplication
@ComponentScan(basePackages = arrayOf("com.example.repositories","com.example.model","com.example.controller","com.example.service"))
@EntityScan("com.example.model")
@EnableJpaRepositories("com.example.repositories")
open class Application {
    companion object {
        @JvmStatic fun main(args: Array<String>) {
            SpringApplication.run(Application::class.java, *args)
        }
    }
}
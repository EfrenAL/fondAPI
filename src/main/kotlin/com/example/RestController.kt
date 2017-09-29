package com.example

import com.example.model.Greeting
import com.example.model.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

@RestController
class GreetingController {

    val counter = AtomicLong()

    @GetMapping("/user")
    fun user() = User(counter.incrementAndGet(), "Mario", "pio pio", "Muyayo", "" )

}
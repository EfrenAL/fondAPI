package com.example.model

import java.util.concurrent.atomic.AtomicLong

class User(var id: Long,
           var name: String,
           var lastName: String,
           var nickName: String,
           var picture: String){

    constructor() : this(1, "","","","")
}



package com.example.model

import java.util.concurrent.atomic.AtomicLong
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "users")

class User(var id: Int,
           var name: String,
           var lastName: String,
           var nickName: String,
           var love: Int,
           var picture: String){

    constructor() : this(1, "","","",0,"")
}



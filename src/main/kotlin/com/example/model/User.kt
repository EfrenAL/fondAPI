package com.example.model

//@Entity
//@Table(name = "users")
class User(var id: Int,
           var name: String,
           var lastName: String,
           var nickName: String,
           var email: String,
           var password: String,
           var love: Int,
           var picture: String){

    constructor() : this(1, "","","","","",0,"")
}



package com.example.jetpacklearn

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(var firstName: String, var lastName: String, var age: Int) {

    @PrimaryKey(autoGenerate = true)    //@PrimaryKey注解将id设置为主键，autoGenerate = true表示主键自增
    var id: Long = 0
}

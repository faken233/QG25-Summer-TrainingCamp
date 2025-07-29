package com.example.jetpacklearn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap

class MainViewModel(countReserved: Int) : ViewModel() {

//    private val userLiveData = MutableLiveData<User>()
//
//    val userName: LiveData<String> = userLiveData.map { user ->
//        "${user.firstName} ${user.lastName}"
//    }
    val counter: LiveData<Int>
        get() = _counter

    private val _counter = MutableLiveData<Int>()

    init {
        _counter.value = countReserved
    }

    fun plusOne() {
        val count = _counter.value ?: 0
        _counter.value = count + 1
    }

    fun clear() {
        _counter.value = 0
    }

    private val userIdLiveData = MutableLiveData<String>()

    val user: LiveData<User> = userIdLiveData.switchMap { userId ->
        Repository.getUser(userId)
    }

    fun getUser(userId: String){
        userIdLiveData.value = userId
    }
}
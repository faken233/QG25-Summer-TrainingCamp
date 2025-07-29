package com.example.mvpdemo.view

interface ILoginView {

    fun onClearText()

    fun onLoginResult(result: Boolean, code: Int)
}
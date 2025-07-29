package com.example.mvpdemo.presenter

interface ILoginPresenter {
    fun clear()
    fun doLogin(name: String, password: String)
}
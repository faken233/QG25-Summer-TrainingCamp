package com.example.mvpdemo.presenter

import android.annotation.SuppressLint
import com.example.mvpdemo.model.User
import com.example.mvpdemo.view.ILoginView

class LoginPresenterCompl(private val loginView: ILoginView): ILoginPresenter {

    private val user: User = User("张三", "123456")


    override fun clear() {
        loginView.onClearText()
    }


    override fun doLogin(name: String, password: String) {
        var result: Boolean
        var code: Int
        if(name == user.name && password == user.password) {
            result = true
            code = 1;
        }else {
            result = false
            code = 0;
        }
        loginView.onLoginResult(result, code)
    }
}



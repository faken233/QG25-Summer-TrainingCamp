package com.example.mvpdemo.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mvpdemo.R
import com.example.mvpdemo.databinding.ActivityMainBinding
import com.example.mvpdemo.presenter.LoginPresenterCompl

class LoginActivity : AppCompatActivity(), ILoginView {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val loginPresenter = LoginPresenterCompl(this)
        binding.btnLogin.setOnClickListener {
            loginPresenter.doLogin(binding.etName.text.toString(), binding.etPassword.text.toString())
        }
        binding.btnClear.setOnClickListener {
            loginPresenter.clear()
        }
    }

    override fun onClearText() {
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.etName.text?.clear()
        binding.etPassword.text?.clear()
        Toast.makeText(this, "clear", Toast.LENGTH_SHORT).show()

    }

    override fun onLoginResult(result: Boolean, code: Int) {
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnLogin.isEnabled = true
        binding.btnClear.isEnabled = true
        when (result) {
            true -> Toast.makeText(this, "登陆成功：" + code, Toast.LENGTH_SHORT).show()
            else -> Toast.makeText(this, "登陆失败：" + code, Toast.LENGTH_SHORT).show()
        }
    }
}
package com.example.instagramclone.activities.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.instagramclone.data.LoginRepository


class LoginViewModelFactory(private val loginRepository: LoginRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(loginRepository) as T

    }
}
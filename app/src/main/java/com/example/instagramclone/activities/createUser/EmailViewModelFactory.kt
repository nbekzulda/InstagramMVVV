package com.example.instagramclone.activities.createUser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.instagramclone.data.EmailRepository

class EmailViewModelFactory(private val emailRepository: EmailRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EmailViewModel(emailRepository) as T
    }
}
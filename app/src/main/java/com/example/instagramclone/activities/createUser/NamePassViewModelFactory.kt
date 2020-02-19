package com.example.instagramclone.activities.createUser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.instagramclone.data.NamePassRepository

class NamePassViewModelFactory(val namePassRepository: NamePassRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NamePassViewModel(namePassRepository) as T
    }

}
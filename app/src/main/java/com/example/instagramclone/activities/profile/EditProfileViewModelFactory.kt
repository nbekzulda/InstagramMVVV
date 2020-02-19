package com.example.instagramclone.activities.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.instagramclone.data.EditProfileRepository

class EditProfileViewModelFactory(val editProfRepository: EditProfileRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EditProfViewModel(editProfRepository) as T

    }
}

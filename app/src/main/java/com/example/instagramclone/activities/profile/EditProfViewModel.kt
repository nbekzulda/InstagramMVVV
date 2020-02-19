package com.example.instagramclone.activities.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.instagramclone.activities.ValueEventListenerAdapter
import com.example.instagramclone.data.EditProfileRepository
import com.example.instagramclone.models.User
import com.example.instagramclone.views.PasswordDialog
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class EditProfViewModel(val editProfRepository: EditProfileRepository) : ViewModel() {

    val livaData = MutableLiveData<StateProfiles>()


    fun onPasswordConfirm(password: String) {
       livaData.value = StateProfiles.ShowLoading
        CompositeDisposable().add(
            editProfRepository.onPasswordConfirmed(password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                livaData.value = StateProfiles.SuccessPasswordConfirm
                livaData.value = StateProfiles.HideLoading
            },{
                livaData.value = it.message?.let { it1 -> StateProfiles.Error(it1) }
                livaData.value = StateProfiles.HideLoading
            })
        )
    }


    fun getCurrentUser() {
        CompositeDisposable().add(
            editProfRepository
                .getCurrentUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> livaData.value = StateProfiles.CurrentUser(result) },
                    { error -> }
                )
        )
    }


    fun updateProfile(user: User) {
        CompositeDisposable().add(
            editProfRepository.updateProfile(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                livaData.value = StateProfiles.Success("Profile saved")
                livaData.value = StateProfiles.HideLoading
            },{
                livaData.value = StateProfiles.Error(it.message!!)
                livaData.value = StateProfiles.HideLoading
            })
        )
    }


    sealed class StateProfiles {
        object ShowLoading : StateProfiles()
        object HideLoading : StateProfiles()
        object Additional : StateProfiles()
        object AddDataBase : StateProfiles()
        object SuccessPasswordConfirm : StateProfiles()
        object SuccessEmail : StateProfiles()
        object Result : StateProfiles()
        data class Error(val message: String) : StateProfiles()
        data class ErrorPassword(val message: String) : StateProfiles()
        data class ErrorEmail(val message: String) : StateProfiles()
        data class Success(val message: String) : StateProfiles()
        data class CurrentUser(val user: User) : StateProfiles()
        data class ResultUser(val user: User) : StateProfiles()

    }

}

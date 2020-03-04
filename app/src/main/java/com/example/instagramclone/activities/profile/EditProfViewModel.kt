package com.example.instagramclone.activities.profile

import androidx.lifecycle.MutableLiveData
import com.example.instagramclone.data.EditProfileRepository
import com.example.instagramclone.models.User
import com.example.instagramclone.utils.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class EditProfViewModel @Inject constructor(val editProfRepository: EditProfileRepository) : BaseViewModel() {

    val livaData = MutableLiveData<StateProfiles>()


    fun onPasswordConfirm(password: String) {

        disposables.add(
            editProfRepository.onPasswordConfirmed(password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe{
                    livaData.value = StateProfiles.ShowLoading
                }
                .doFinally {
                    livaData.value = StateProfiles.HideLoading
                }
                .subscribe({
                livaData.value = StateProfiles.SuccessPasswordConfirm

            },{
                livaData.value = it.message?.let { it1 -> StateProfiles.Error(it1) }

            })
        )
    }


    fun getCurrentUser() {
        disposables.add(
            editProfRepository
                .getCurrentUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> livaData.value = StateProfiles.CurrentUser(result) },
                    { error -> livaData.value = StateProfiles.Error(error.message!!)}
                )
        )
    }


    fun updateProfile(user: User) {
        disposables.add(
            editProfRepository.updateProfile(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    livaData.value = StateProfiles.ShowLoading
                }
                .doFinally {
                    livaData.value = StateProfiles.HideLoading
                }
                .subscribe({
                livaData.value = StateProfiles.Success("Profile saved")

            },{
                livaData.value = StateProfiles.Error(it.message!!)

            })
        )
    }

    sealed class StateProfiles {
        object ShowLoading : StateProfiles()
        object HideLoading : StateProfiles()
        object Additional : StateProfiles()
        object SuccessPasswordConfirm : StateProfiles()
        data class Error(val message: String) : StateProfiles()
        data class ErrorPassword(val message: String) : StateProfiles()
        data class Success(val message: String) : StateProfiles()
        data class CurrentUser(val user: User) : StateProfiles()

    }

}

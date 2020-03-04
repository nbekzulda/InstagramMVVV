package com.example.instagramclone.activities.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.instagramclone.data.LoginRepository
import com.example.instagramclone.utils.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class LoginViewModel @Inject constructor(val loginRepository: LoginRepository) : BaseViewModel() {

    val liveData = MutableLiveData<State>()

    private fun validate(email: String, password: String) = email.isNotEmpty() && password.isNotEmpty()

    fun login(email: String, password: String){


        disposables.add(
            loginRepository.getlogin(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    liveData.value = State.ShowLoading
                }
                .doFinally{
                    liveData.value = State.HideLoading
                }
                .subscribe ({ result ->
                Log.d("nur_result", result.toString())
                    liveData.value = State.Result
            }, {
                    liveData.value = State.Error("SOMETHING WRONG")

                })
            )

//        if (validate(email, password)) {
//            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener {
//                    if (it.isSuccessful) {
//                        liveData.value = State.Result
//                        liveData.value = State.HideLoading
//                    }
//                }
//                .addOnFailureListener {
//                    liveData.value = State.Error("Wrong email or password")
//                    liveData.value = State.HideLoading
//                }
//        }
//        else{
//            liveData.value = State.Error("Please enter email and password")
//        }
    }

    sealed class State {
        object ShowLoading: State()
        object HideLoading: State()
        object Result: State()
        data class Error(val message: String): State()
    }


}


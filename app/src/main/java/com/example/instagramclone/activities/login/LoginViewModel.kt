package com.example.instagramclone.activities.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.instagramclone.data.LoginRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ValueEventListener
import io.reactivex.Completable
import io.reactivex.MaybeSource
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Publisher

class LoginViewModel(val loginRepository: LoginRepository) : ViewModel() {

    val liveData = MutableLiveData<State>()

    private fun validate(email: String, password: String) = email.isNotEmpty() && password.isNotEmpty()

    fun login(email: String, password: String){
        liveData.value = State.ShowLoading

        CompositeDisposable().add(
            loginRepository.getlogin(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                Log.d("nur_result", result.toString())
                    liveData.value = State.Result
                    liveData.value = State.HideLoading
            }, {
                    liveData.value = State.Error("SOMETHING WRONG")
                    liveData.value = State.HideLoading
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

    sealed class State{
        object ShowLoading: State()
        object HideLoading: State()
        object Result: State()
        data class Error(val message: String): State()
    }


}


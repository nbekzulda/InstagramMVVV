package com.example.instagramclone.activities.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.instagramclone.data.LoginRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ValueEventListener
import io.reactivex.Completable
import io.reactivex.MaybeSource
import io.reactivex.functions.Function
import org.reactivestreams.Publisher

class LoginViewModel() : ViewModel() {

    val liveData = MutableLiveData<State>()

    private fun validate(email: String, password: String) = email.isNotEmpty() && password.isNotEmpty()

    fun login(email: String, password: String){
        liveData.value = State.ShowLoading
        if (validate(email, password)) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        liveData.value = State.Result
                        liveData.value = State.HideLoading
                    }
                }
                .addOnFailureListener {
                    liveData.value = State.Error("Wrong email or password")
                    liveData.value = State.HideLoading
                }
        }
        else{
            liveData.value = State.Error("Please enter email and password")
        }
    }

    sealed class State{
        object ShowLoading: State()
        object HideLoading: State()
        object Result: State()
        data class Error(val message: String): State()
    }






}
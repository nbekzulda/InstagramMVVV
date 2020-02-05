package com.example.instagramclone.activities.createUser

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.instagramclone.activities.showToast
import com.google.firebase.auth.FirebaseAuth

class EmailViewModel : ViewModel() {

    val liveData = MutableLiveData<Any>()

    fun onNext(email: String){
        liveData.value = State.ShowLoading
        Log.d("email", email)
        if(validate(email)){
            FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email){signInMethods ->
                if (signInMethods.isEmpty()){
                    liveData.value = State.Result
                    liveData.value = State.HideLoading
                }
                else{
                    liveData.value = State.Error("This email already exists")
                    liveData.value = State.HideLoading
                }
            }
        }
    }

    private fun FirebaseAuth.fetchSignInMethodsForEmail(email: String, onSuccess: (List<String>) -> Unit){
        fetchSignInMethodsForEmail(email)
            .addOnCompleteListener{
            if (it.isSuccessful){
                onSuccess (it.result?.signInMethods?: emptyList<String>())


            }
            else{
                liveData.value = State.Error(it.exception!!.message!!)
                liveData.value = State.HideLoading
            }
        }
    }


    private fun validate(email: String) = email.isNotEmpty()

    sealed class State{
        object ShowLoading: State()
        object HideLoading: State()
        object Result: State()
        data class Error(val message: String): State()
    }
}
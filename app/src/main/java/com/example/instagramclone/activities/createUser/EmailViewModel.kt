package com.example.instagramclone.activities.createUser

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.instagramclone.data.EmailRepository
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.disposables.CompositeDisposable

class EmailViewModel(val emailRepository: EmailRepository) : ViewModel() {

    val liveData = MutableLiveData<State>()

    fun onNext(email: String){
        liveData.value = State.ShowLoading

        CompositeDisposable().add(
            emailRepository.onNext(email).subscribe({result ->
                liveData.value = State.Result
                liveData.value = State.HideLoading
            }, {
                liveData.value = State.Error("SOMETHING WRONG")
                liveData.value = State.HideLoading
            })
        )
    }

//    private fun FirebaseAuth.fetchSignInMethodsForEmail(email: String, onSuccess: (List<String>) -> Unit){
//        fetchSignInMethodsForEmail(email)
//            .addOnCompleteListener{
//            if (it.isSuccessful){
//                onSuccess (it.result?.signInMethods?: emptyList<String>())
//
//
//            }
//            else{
//                liveData.value = State.Error(it.exception!!.message!!)
//                liveData.value = State.HideLoading
//            }
//        }
//    }
//
//    fun onNext(email: String){
//        liveData.value = State.ShowLoading
//        Log.d("email", email)
//        if(validate(email)){
//            FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email){signInMethods ->
//                if (signInMethods.isEmpty()){
//                    liveData.value = State.Result
//                    liveData.value = State.HideLoading
//                }
//                else{
//                    liveData.value = State.Error("This email already exists")
//                    liveData.value = State.HideLoading
//                }
//            }
//        }
//    }
//
//    private fun validate(email: String) = email.isNotEmpty()

    sealed class State{
        object ShowLoading: State()
        object HideLoading: State()
        object Result: State()
        data class Error(val message: String): State()
    }
}
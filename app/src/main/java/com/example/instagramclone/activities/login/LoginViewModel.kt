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
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function
import org.reactivestreams.Publisher

class LoginViewModel(val loginRepository: LoginRepository) : ViewModel() {

    val liveData = MutableLiveData<State>()



    private fun validate(email: String, password: String) = email.isNotEmpty() && password.isNotEmpty()

    fun login(email: String, password: String){
        liveData.value = State.ShowLoading

        CompositeDisposable().add(
            Observable.fromCallable { loginRepository.getlogin(email, password)}
                .subscribe({ result ->
                Log.d("nur_result", result.toString())
                    liveData.value = State.Result
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

private fun CompositeDisposable.add(doOnSubscribe: Observable<Boolean>?) {

}

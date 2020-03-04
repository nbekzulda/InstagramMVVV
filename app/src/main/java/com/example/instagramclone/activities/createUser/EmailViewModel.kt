package com.example.instagramclone.activities.createUser


import androidx.lifecycle.MutableLiveData
import com.example.instagramclone.data.EmailRepository
import com.example.instagramclone.utils.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class EmailViewModel @Inject constructor(val emailRepository: EmailRepository) : BaseViewModel() {

    val liveData = MutableLiveData<State>()

    fun onNext(email: String){


        disposables.add(
            emailRepository.onNext(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    liveData.value = State.ShowLoading
                }
                .doFinally {
                    liveData.value = State.HideLoading
                }
                .subscribe({result ->
                liveData.value = State.Result

            }, {
                liveData.value = State.Error("SOMETHING WRONG")

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

    sealed class State {
        object ShowLoading: State()
        object HideLoading: State()
        object Result: State()
        data class Error(val message: String): State()
    }
}
package com.example.instagramclone.activities.createUser


import androidx.lifecycle.MutableLiveData
import com.example.instagramclone.data.NamePassRepository
import com.example.instagramclone.utils.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NamePassViewModel @Inject constructor(val namePassRepository: NamePassRepository) : BaseViewModel() {

    val liveData = MutableLiveData<State>()


    fun onRegister(fullname: String, password: String, email: String) {


        disposables.add (
            namePassRepository.onRegister(fullname, password, email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    liveData.value = State.ShowLoading
                }
                .doFinally {
                    liveData.value = State.HideLoading
                }
                .subscribe({
                liveData.value = State.Result

            },{
                liveData.value = State.Error(it.message!!)

            })
        )
    }

//    private fun mkUsername(fullname: String) = fullname.toLowerCase().replace(" ", ".")
//
//    private fun DatabaseReference.createUser(uid: String, user: User, onSuccess: () -> Unit){
//        val reference = child("users").child(uid)
//        reference.setValue(user).addOnCompleteListener{
//            if (it.isSuccessful){
//                onSuccess()
//            }
//            else{
//                unknownRegisterError(it)
//            }
//        }
//    }
//
//    private fun FirebaseAuth.createUserWithEmailAndPassword(email: String, password: String, onSuccess: (AuthResult) -> Unit){
//        Log.d("email value ", email)
//        createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener{
//                if (it.isSuccessful ){
//                    onSuccess(it.result!!)
//                }else{
//                    Log.e("err", "failed to create user profile", it.exception)
//                    unknownRegisterError(it)
//                }
//            }
//    }
//
//    private fun unknownRegisterError(it: Task<out Any>) {
//        liveData.value = State.Error(it.exception!!.message!!)
//        liveData.value = State.HideLoading
//
//    }
//
//
//    fun onRegister(fullname: String, password: String, mEmail: String){
//        liveData.value = State.ShowLoading
//        if(fullname.isNotEmpty() && password.isNotEmpty()){
//            if (mEmail != null) {
//                FirebaseAuth.getInstance().createUserWithEmailAndPassword(mEmail, password){
//                    val user = mkUser(fullname, mEmail)
//                    FirebaseDatabase.getInstance().reference.createUser(it.user!!.uid, user) {
//                        liveData.value = State.Result
//                        liveData.value = State.HideLoading
//                    }
//                }
//            }
//            else{
//                liveData.value = State.Error("Something is wrong. Please try it later")
//                liveData.value = State.HideLoading
//            }
//        }
//        else{
//            liveData.value = State.Error("Please enter full name and password")
//            liveData.value = State.HideLoading
//        }
//    }


    sealed class State {
        object ShowLoading: State()
        object HideLoading: State()
        object Result: State()
        data class Error(val message: String): State()
    }
}
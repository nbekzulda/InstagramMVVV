package com.example.instagramclone.data


import android.widget.Toast
import com.example.instagramclone.activities.createUser.EmailViewModel
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Observable

interface EmailRepository{
    fun onNext(email: String) : Observable<Boolean>
}


class EmailRepositoryImpl(val firebaseAuth: FirebaseAuth) : EmailRepository{
    override fun onNext(email: String): Observable<Boolean> {
        return Observable.create {emitter ->
            firebaseAuth.fetchSignInMethodsForEmail(email) { signInMethods ->
                if (signInMethods.isEmpty()) {
                    emitter.onNext(true)
                }
                emitter.onComplete()
            }
        }
    }

    private fun FirebaseAuth.fetchSignInMethodsForEmail(email: String, onSuccess: (List<String>) -> Unit){
        fetchSignInMethodsForEmail(email)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    onSuccess (it.result?.signInMethods?: emptyList<String>())
                }
            }
    }
}
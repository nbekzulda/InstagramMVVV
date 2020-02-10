package com.example.instagramclone.data

import android.util.Log
import com.example.instagramclone.models.User
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.MaybeSource
import io.reactivex.Observable
import io.reactivex.functions.Function
import org.reactivestreams.Publisher


interface LoginRepository{
    fun getlogin(email: String, password: String) : Observable<Boolean>
}

class LoginRepositoryImpl(val firebaseAuth: FirebaseAuth ): LoginRepository {
    override fun getlogin(email: String, password: String): Observable<Boolean> {
        return Observable.create { emitter ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {result ->
                    if(result.isSuccessful){
                        emitter.onNext(true)
                    }
                    emitter.onComplete()
                }
                .addOnFailureListener {error ->
                    emitter.onError(error)
                    emitter.onComplete()
                }

        }
    }


}
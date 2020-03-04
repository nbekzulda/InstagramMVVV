package com.example.instagramclone.data


import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Observable
import javax.inject.Inject


interface LoginRepository {
    fun getlogin(email: String, password: String) : Observable<Boolean>
}


class LoginRepositoryImpl @Inject constructor (val firebaseAuth: FirebaseAuth ): LoginRepository {

    override fun getlogin(email: String, password: String): Observable<Boolean> {
        return Observable.create { emitter ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {result ->
                    if(result.isSuccessful){
                        emitter.onNext(true)
                    }
                    emitter.onComplete()
                }
                .addOnFailureListener { error ->
                    emitter.onError(error)
                    emitter.onComplete()
                }
        }
    }

}
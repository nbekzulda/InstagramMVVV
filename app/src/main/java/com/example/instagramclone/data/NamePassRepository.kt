package com.example.instagramclone.data

import android.util.Log
import com.example.instagramclone.activities.createUser.NamePassViewModel
import com.example.instagramclone.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Observable
import io.reactivex.Single
import java.lang.Error

interface NamePassRepository{
    fun onRegister(fullname: String, password: String, email: String): Observable<Boolean>
}

class NamePassRepositoryImpl(val firebaseAuth: FirebaseAuth): NamePassRepository{
    override fun onRegister(fullname: String, password: String, email: String): Observable<Boolean> {
        return Observable.create { registrate ->
            if (fullname.isNotEmpty() && password.isNotEmpty()) {
                if (email != null) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password) {
                        val user = mkUser(fullname, email)
                        FirebaseDatabase.getInstance().reference.createUser(it.user!!.uid, user) {
                            registrate.onNext(true)
                        }
                    }
                } else {
                    registrate.onError(error("Something is wrong. Please try it later"))
                }
            } else {
                registrate.onError(error("Please enter full name and password"))
            }
        }
    }


    private fun mkUser(fullname: String, email: String): User {
        val username = mkUsername(fullname)
        return User(name = fullname, username = username, email = email)
    }

    private fun mkUsername(fullname: String) = fullname.toLowerCase().replace(" ", ".")

    private fun DatabaseReference.createUser(uid: String, user: User, onSuccess: () -> Unit){
        val reference = child("users").child(uid)
        reference.setValue(user).addOnCompleteListener{
            if (it.isSuccessful){
                onSuccess()
            }
            else{
                Error(it.exception?.message)
            }
        }
    }

    private fun FirebaseAuth.createUserWithEmailAndPassword(email: String, password: String, onSuccess: (AuthResult) -> Unit){
        Log.d("email value ", email)
        createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if (it.isSuccessful ){
                    onSuccess(it.result!!)
                }else{
                    Error(it.exception?.message)
                }
            }
    }


}
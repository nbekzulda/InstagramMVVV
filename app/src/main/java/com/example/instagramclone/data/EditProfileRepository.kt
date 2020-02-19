package com.example.instagramclone.data

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.instagramclone.activities.ValueEventListenerAdapter

import com.example.instagramclone.models.User
import com.example.instagramclone.views.PasswordDialog
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.*



interface EditProfileRepository{
    fun getCurrentUser() : Single<User>
    fun updateProfile(user: User): Single<User>
    fun onPasswordConfirmed(password: String) : Observable<Boolean>

}

class EditProfileRepositoryImpl(val firebaseAuth: FirebaseAuth) : EditProfileRepository, PasswordDialog.Listener {

    override fun onPasswordConfirmed(password: String): Observable<Boolean> {
        return Observable.create{emitter ->
            if(!password.isNullOrEmpty()){
                val credential = EmailAuthProvider.getCredential(mUser!!.email, password)
                FirebaseAuth.getInstance().currentUser!!.reauthenticate(credential){
                    FirebaseAuth.getInstance().currentUser!!.updateEmail(mUser!!.email){
                        updateProfile(mUser!!)
                        emitter.onNext(true)
                    }
                    emitter.onComplete()
                }
            }
            else  {
                emitter.onError(error("You should enter your password"))
            }
        }
    }

    var mUser: User? = null
    var mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference


    override fun getCurrentUser(): Single<User> {
        return Single.create { emitter ->
            FirebaseDatabase.getInstance().reference.child("users")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .addListenerForSingleValueEvent(ValueEventListenerAdapter { snapshot ->
                    val result = snapshot.getValue(User::class.java)
                    Log.d("user_result", result?.toString() ?: "null")
                    if (result == null) {
                        emitter.onError(Throwable("User is null"))
                    } else {
                        mUser = result
                        emitter.onSuccess(result)
                    }
            })

        }
    }


    override fun updateProfile(user: User): Single<User> {
        return Single.create{emitter ->
            updateUser(user)
            emitter.onSuccess(user)
        }
    }

    private fun updateUser(user: User) {
        val updatesMap = mutableMapOf<String, Any>()
        if (user.name != mUser?.name) updatesMap["name"] = user.name
        if (user.username != mUser?.username) updatesMap["username"] = user.username
        if (user.website != mUser?.website) updatesMap["website"] = user.website
        if (user.bio != mUser?.bio) updatesMap["bio"] = user.bio
        if (user.email != mUser?.email) updatesMap["email"] = user.email
        if (user.phone != mUser?.phone) updatesMap["phone"] = user.phone

        mDatabase.updateUser(FirebaseAuth.getInstance().currentUser!!.uid, updatesMap){}


    }

    fun DatabaseReference.updateUser(uid: String, updates: Map<String, Any>, onSuccess: () -> Unit) {
        child("users").child(FirebaseAuth.getInstance().currentUser!!.uid).updateChildren(updates)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    error(it.exception!!.message!!)
                }
            }
    }

    fun FirebaseUser.updateEmail(email: String, onSuccess: () -> Unit) {
        updateEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess()
            } else {
                error(it.exception!!.message!!)
            }
        }
    }


    fun FirebaseUser.reauthenticate(credential: AuthCredential, onSuccess: () -> Unit) {
        reauthenticate(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess()
            } else {
                error(it.exception!!.message!!)
            }
        }
    }

}
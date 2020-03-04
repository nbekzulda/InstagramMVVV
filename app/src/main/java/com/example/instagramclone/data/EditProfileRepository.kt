package com.example.instagramclone.data


import android.util.Log
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
import javax.inject.Inject


interface EditProfileRepository {
    fun getCurrentUser() : Single<User>
    fun updateProfile(user: User): Single<User>
    fun onPasswordConfirmed(password: String) : Observable<Boolean>

}

class EditProfileRepositoryImpl @Inject constructor(private val firebaseAuth: FirebaseAuth) : EditProfileRepository, PasswordDialog.Listener {

    override fun onPasswordConfirmed(password: String): Observable<Boolean> {
        return Observable.create { emitter ->
            if(password.isNotEmpty()){
                val credential = EmailAuthProvider.getCredential(getUser!!.email, password)
                FirebaseAuth.getInstance().currentUser!!.reauthenticate(credential){
                    FirebaseAuth.getInstance().currentUser!!.updateEmail(getUser!!.email) {
                        updateProfile(getUser!!)
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

    var getUser: User? = null
    var getDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference


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
                        getUser = result
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
        if (user.name != getUser?.name) updatesMap["name"] = user.name
        if (user.username != getUser?.username) updatesMap["username"] = user.username
        if (user.website != getUser?.website) updatesMap["website"] = user.website
        if (user.bio != getUser?.bio) updatesMap["bio"] = user.bio
        if (user.email != getUser?.email) updatesMap["email"] = user.email
        if (user.phone != getUser?.phone) updatesMap["phone"] = user.phone

        getDatabase.updateUser(FirebaseAuth.getInstance().currentUser!!.uid, updatesMap){}

    }

    fun DatabaseReference.updateUser(uid: String, updates: Map<String, Any>, onSuccess: () -> Unit) {
        child("users").child(FirebaseAuth.getInstance().currentUser!!.uid).updateChildren(updates)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    it.exception?.message?.let { it1 -> error(it1) }
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
                it.exception?.message?.let { it1 -> error(it1) }
            }
        }
    }

}
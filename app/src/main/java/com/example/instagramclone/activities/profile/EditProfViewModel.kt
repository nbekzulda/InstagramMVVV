package com.example.instagramclone.activities.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.instagramclone.activities.ValueEventListenerAdapter
import com.example.instagramclone.models.User
import com.example.instagramclone.views.PasswordDialog
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditProfViewModel : ViewModel(), PasswordDialog.Listener {

    val livaData = MutableLiveData<StateProfiles>()

    var mUser: User? = null

    var mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun getCurrentUser() {
        FirebaseDatabase.getInstance().reference.child("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .addListenerForSingleValueEvent(ValueEventListenerAdapter {
                livaData.value = StateProfiles.CurrentUser(it.getValue(User::class.java)!!)
                mUser = it.getValue(User::class.java)!!
            })
    }

    override fun onPasswordConfirm(password: String) {
        if (!password.isNullOrEmpty()) {
            val credential = EmailAuthProvider.getCredential(mUser!!.email, password)
            FirebaseAuth.getInstance().currentUser!!.reauthenticate(credential){
                updateProfile(mUser!!)
            }
        } else {
            livaData.value = StateProfiles.Error("You should enter your password")
        }
    }

    fun updateProfile(user: User) {
        updateUser(user)
    }

    private fun updateUser(user: User) {
        val updatesMap = mutableMapOf<String, Any>()
        if (user.name != mUser?.name) updatesMap["name"] = user.name
        if (user.username != mUser?.username) updatesMap["username"] = user.username
        if (user.website != mUser?.website) updatesMap["website"] = user.website
        if (user.bio != mUser?.bio) updatesMap["bio"] = user.bio
        if (user.email != mUser?.email) updatesMap["email"] = user.email
        if (user.phone != mUser?.phone) updatesMap["phone"] = user.phone


        mDatabase.updateUser(FirebaseAuth.getInstance().currentUser!!.uid, updatesMap) {
            livaData.value = StateProfiles.Success("Profile saved")

        }

    }



        fun DatabaseReference.updateUser(uid: String, updates: Map<String, Any>, onSuccess: () -> Unit) {
            child("users").child(FirebaseAuth.getInstance().currentUser!!.uid).updateChildren(updates)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        onSuccess()
                    } else {
                        livaData.value = StateProfiles.Error(it.exception!!.message!!)
                    }
                }
        }

        fun FirebaseUser.reauthenticate(credential: AuthCredential, onSuccess: () -> Unit) {
            reauthenticate(credential).addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess()
                    livaData.value = StateProfiles.SuccessPasswordConfirm
                } else {
                    livaData.value = StateProfiles.ErrorPassword(it.exception!!.message!!)
                }
            }
        }

        fun FirebaseUser.updateEmail(email: String, onSuccess: () -> Unit) {
            updateEmail(email).addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess()
                    livaData.value = StateProfiles.SuccessEmail
                } else {
                    livaData.value = StateProfiles.ErrorEmail(it.exception!!.message!!)
                }
            }
        }






    sealed class StateProfiles {
        object ShowLoading : StateProfiles()
        object HideLoading : StateProfiles()
        object Additional : StateProfiles()
        object AddDataBase : StateProfiles()
        object SuccessPasswordConfirm : StateProfiles()
        object SuccessEmail : StateProfiles()
        object Result : StateProfiles()
        data class Error(val message: String) : StateProfiles()
        data class ErrorPassword(val message: String) : StateProfiles()
        data class ErrorEmail(val message: String) : StateProfiles()
        data class Success(val message: String) : StateProfiles()
        data class CurrentUser(val user: User) : StateProfiles()

    }

}

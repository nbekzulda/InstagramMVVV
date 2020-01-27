package com.example.instagramclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.BatchUpdates
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.instagramclone.R
import com.example.instagramclone.models.User
import com.example.instagramclone.views.PasswordDialog
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity(), PasswordDialog.Listener{

    private lateinit var mUser: User
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference
    private lateinit var mPendingUser: User

    private val TAG = "EditProfileActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        Log.d(TAG, "onCreate")

        close_image.setOnClickListener{ finish() }

        save_image.setOnClickListener {
            updateProfile()
            //progressBar.visibility = View.VISIBLE
        }
        mAuth = FirebaseAuth.getInstance()

        mDatabase = FirebaseDatabase.getInstance().reference
        mDatabase.child("users").child(mAuth.currentUser!!.uid)
            .addListenerForSingleValueEvent(ValueEventListenerAdapter {
                mUser = it.getValue(User::class.java)!!
                name_input.setText(mUser.name, TextView.BufferType.EDITABLE)
                Username_input.setText(mUser.username, TextView.BufferType.EDITABLE)
                website_input.setText(mUser.website, TextView.BufferType.EDITABLE)
                bio_input.setText(mUser.bio, TextView.BufferType.EDITABLE)
                email_input.setText(mUser.email, TextView.BufferType.EDITABLE)
                phone_input.setText(mUser.phone, TextView.BufferType.EDITABLE)


            })
    }

    private fun updateProfile() {
        mPendingUser = readInputs()
        val error = validate(mPendingUser)
        if (error == null){
            if (mPendingUser.email == mUser.email){
                updateUser(mPendingUser)
            }
            else{
                PasswordDialog().show(supportFragmentManager, "password_dialog")

            }
        }
        else{
            showToast(error)
        }
    }

    private fun readInputs(): User {

        val phoneStr = phone_input.text.toString()
        return User(
            name = name_input.text.toString(),
            username = Username_input.text.toString(),
            website = website_input.text.toString(),
            bio = bio_input.text.toString(),
            email = email_input.text.toString(),
            phone = if(phoneStr.isEmpty()) 0.toString() else phoneStr.toLong().toString()
        )
    }

    override fun onPasswordConfirm(password: String) {
        if (password.isNotEmpty()){
            val credential = EmailAuthProvider.getCredential(mUser.email, password)
            mAuth.currentUser!!.reauthenticate(credential){
                mAuth.currentUser!!.updateEmail(mPendingUser.email) {
                    updateUser(mPendingUser)
                }
            }
        }
        else{
            showToast("You should enter your password")
        }

    }



        private fun updateUser(user: User){
            val updatesMap = mutableMapOf<String, Any>()
            if(user.name != mUser.name) updatesMap["name"] = user.name
            if(user.username != mUser.username) updatesMap["username"] = user.username
            if(user.website != mUser.website) updatesMap["website"] = user.website
            if(user.bio != mUser.bio) updatesMap["bio"] = user.bio
            if(user.email != mUser.email) updatesMap["email"] = user.email
            if(user.phone != mUser.phone) updatesMap["phone"] = user.phone


            mDatabase.updateUser(mAuth.currentUser!!.uid, updatesMap) {
                    showToast("Profile saved")
                    //progressBar.visibility = View.INVISIBLE
                    finish()

            }
    }

    private fun DatabaseReference.updateUser(uid: String, updates: Map<String, Any>, onSuccess: () -> Unit){
        child("users").child(mAuth.currentUser!!.uid).updateChildren(updates)
            .addOnCompleteListener{
                if (it.isSuccessful){
                   onSuccess()
                }
                else{
                    showToast(it.exception!!.message!!)
                }
            }
    }

    private fun validate(user: User) =
        when{
            user.name.isEmpty() -> "Please enter name"
            user.username.isEmpty() -> "Please enter username"
            user.email.isEmpty() -> "Please enter email"
            else -> null
        }

    private fun FirebaseUser.reauthenticate(credential: AuthCredential, onSuccess: () -> Unit) {
        reauthenticate(credential).addOnCompleteListener{
            if(it.isSuccessful){
                onSuccess()
            }
            else{
                showToast(it.exception!!.message!!)
            }
        }
    }

    private fun FirebaseUser.updateEmail(email: String, onSuccess: () -> Unit) {

        updateEmail(email).addOnCompleteListener{
            if(it.isSuccessful){
                onSuccess()
            }
            else{
                showToast(it.exception!!.message!!)
            }
        }

    }
}




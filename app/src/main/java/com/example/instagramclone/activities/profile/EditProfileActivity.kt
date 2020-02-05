package com.example.instagramclone.activities.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.instagramclone.R
import com.example.instagramclone.activities.ValueEventListenerAdapter
import com.example.instagramclone.activities.home.MainActivity
import com.example.instagramclone.activities.login.LoginViewModel
import com.example.instagramclone.activities.showToast
import com.example.instagramclone.models.User
import com.example.instagramclone.views.PasswordDialog
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.progressBar
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.dialog_password.*

class EditProfileActivity : AppCompatActivity(), PasswordDialog.Listener{

    private lateinit var mUser: User
    private lateinit var mPendingUser: User
    private var viewModel: EditProfViewModel? = null
    private lateinit var buttonSave:ImageView
    private lateinit var buttonClose:ImageView

    private val TAG = "EditProfileActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        bindView()
        inputData()
    }

    private fun bindView() {
        buttonSave = findViewById(R.id.save_image)
        buttonClose = findViewById(R.id.close_image)

        viewModel = ViewModelProviders.of(this).get(EditProfViewModel::class.java)

        buttonClose.setOnClickListener{ finish() }




        buttonSave.setOnClickListener {
            val phoneStr = phone_input.text.toString()
            mPendingUser = User(name = name_input.text.toString(),
                username = Username_input.text.toString(),
                website = website_input.text.toString(),
                bio = bio_input.text.toString(),
                email = email_input.text.toString(),
                phone = if(phoneStr.isEmpty()) 0.toString() else phoneStr.toLong().toString())
            if (mUser.email != email_input.text.toString() && email_input.text.toString().isNotEmpty()) {
                PasswordDialog().show(supportFragmentManager, "password_dialog")


            } else {
                viewModel!!.updateProfile(mPendingUser)
            }

        }
    }

    private fun inputData(){
        viewModel!!.getCurrentUser()
        viewModel!!.livaData.observe(this, Observer { state ->
            when (state){
                is EditProfViewModel.StateProfiles.ShowLoading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is EditProfViewModel.StateProfiles.HideLoading -> {
                    progressBar.visibility = View.INVISIBLE
                    finish()
                }
                is EditProfViewModel.StateProfiles.CurrentUser -> {
                    mUser = state.user
                    name_input.setText(mUser.name, TextView.BufferType.EDITABLE)
                    Username_input.setText(mUser.username, TextView.BufferType.EDITABLE)
                    website_input.setText(mUser.website, TextView.BufferType.EDITABLE)
                    bio_input.setText(mUser.bio, TextView.BufferType.EDITABLE)
                    email_input.setText(mUser.email, TextView.BufferType.EDITABLE)
                    phone_input.setText(mUser.phone, TextView.BufferType.EDITABLE)
                }
                is EditProfViewModel.StateProfiles.Error -> {
                    showToast(state.message)
                }
                is EditProfViewModel.StateProfiles.Success -> {
                    showToast(state.message)
                    finish()
                }
                is EditProfViewModel.StateProfiles.Additional -> {
                    PasswordDialog().show(supportFragmentManager, "password_dialog")
                }
                is EditProfViewModel.StateProfiles.SuccessPasswordConfirm -> {
                    viewModel!!.updateProfile(mPendingUser)
                }

            }
        })
    }

    override fun onPasswordConfirm(password: String) {
        viewModel!!.onPasswordConfirm(password)
    }

//    private fun addDataBse(){
//
//        mDatabase.child("users").child(mAuth.currentUser!!.uid)
//            .addListenerForSingleValueEvent(ValueEventListenerAdapter {
//                mUser = it.getValue(User::class.java)!!
//                name_input.setText(mUser.name, TextView.BufferType.EDITABLE)
//                Username_input.setText(mUser.username, TextView.BufferType.EDITABLE)
//                website_input.setText(mUser.website, TextView.BufferType.EDITABLE)
//                bio_input.setText(mUser.bio, TextView.BufferType.EDITABLE)
//                email_input.setText(mUser.email, TextView.BufferType.EDITABLE)
//                phone_input.setText(mUser.phone, TextView.BufferType.EDITABLE)
//
//
//            })
//    }
//
//    private fun updateProfile() {
//        mPendingUser = readInputs()
//        val error = validate(mPendingUser)
//        if (error == null){
//            if (mPendingUser.email == mUser.email){
//                updateUser(mPendingUser)
//            }
//            else{
//                PasswordDialog().show(supportFragmentManager, "password_dialog")
//
//            }
//        }
//        else{
//            showToast(error)
//        }
//    }
//
//    private fun readInputs(): User {
//
//        val phoneStr = phone_input.text.toString()
//        return User(
//            name = name_input.text.toString(),
//            username = Username_input.text.toString(),
//            website = website_input.text.toString(),
//            bio = bio_input.text.toString(),
//            email = email_input.text.toString(),
//            phone = if(phoneStr.isEmpty()) 0.toString() else phoneStr.toLong().toString()
//        )
//    }
//
//    override fun onPasswordConfirm(password: String) {
//        if (password.isNotEmpty()){
//            val credential = EmailAuthProvider.getCredential(mUser.email, password)
//            mAuth.currentUser!!.reauthenticate(credential){
//                mAuth.currentUser!!.updateEmail(mPendingUser.email) {
//                    updateUser(mPendingUser)
//                }
//            }
//        }
//        else{
//            showToast("You should enter your password")
//        }
//
//    }
//
//
//
//        private fun updateUser(user: User){
//            val updatesMap = mutableMapOf<String, Any>()
//            if(user.name != mUser.name) updatesMap["name"] = user.name
//            if(user.username != mUser.username) updatesMap["username"] = user.username
//            if(user.website != mUser.website) updatesMap["website"] = user.website
//            if(user.bio != mUser.bio) updatesMap["bio"] = user.bio
//            if(user.email != mUser.email) updatesMap["email"] = user.email
//            if(user.phone != mUser.phone) updatesMap["phone"] = user.phone
//
//
//            mDatabase.updateUser(mAuth.currentUser!!.uid, updatesMap) {
//                    showToast("Profile saved")
//                    //progressBar.visibility = View.INVISIBLE
//                    finish()
//
//            }
//    }
//
//    private fun DatabaseReference.updateUser(uid: String, updates: Map<String, Any>, onSuccess: () -> Unit){
//        child("users").child(mAuth.currentUser!!.uid).updateChildren(updates)
//            .addOnCompleteListener{
//                if (it.isSuccessful){
//                   onSuccess()
//                }
//                else{
//                    showToast(it.exception!!.message!!)
//                }
//            }
//    }
//
//    private fun validate(user: User) =
//        when{
//            user.name.isEmpty() -> "Please enter name"
//            user.username.isEmpty() -> "Please enter username"
//            user.email.isEmpty() -> "Please enter email"
//            else -> null
//        }
//
//    private fun FirebaseUser.reauthenticate(credential: AuthCredential, onSuccess: () -> Unit) {
//        reauthenticate(credential).addOnCompleteListener{
//            if(it.isSuccessful){
//                onSuccess()
//            }
//            else{
//                showToast(it.exception!!.message!!)
//            }
//        }
//    }
//
//    private fun FirebaseUser.updateEmail(email: String, onSuccess: () -> Unit) {
//
//        updateEmail(email).addOnCompleteListener{
//            if(it.isSuccessful){
//                onSuccess()
//            }
//            else{
//                showToast(it.exception!!.message!!)
//            }
//        }
//
//    }
}




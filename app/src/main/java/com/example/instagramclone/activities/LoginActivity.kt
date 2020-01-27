package com.example.instagramclone.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.instagramclone.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener

class LoginActivity : AppCompatActivity(), KeyboardVisibilityEventListener,
    View.OnClickListener {

    private val TAG = "LoginActivity"
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Log.d(TAG, "onCreate")

        KeyboardVisibilityEvent.setEventListener(this, this)
        coordinateBtnAndInputs(login_button, email_Input, password_Input)
        login_button.setOnClickListener(this)
        create_account_text.setOnClickListener(this)

        mAuth = FirebaseAuth.getInstance()
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.login_button -> {
                val email = email_Input.text.toString()
                val password = password_Input.text.toString()
                progressBar.visibility = View.VISIBLE
                if(validate(email, password)){
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                        if(it.isSuccessful){
                            progressBar.visibility = View.INVISIBLE
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                        else{
                            progressBar.visibility = View.INVISIBLE
                            showToast("Wrong email or password")
                        }
                    }
                }
                else{
                    progressBar.visibility = View.INVISIBLE
                    showToast("Please enter email and password")
                }
            }
            R.id.create_account_text -> {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
        }


    }


    override fun onVisibilityChanged(isKeyboardOpen: Boolean) {
        if(isKeyboardOpen){
            scroll_view.scrollTo(0, scroll_view.bottom)
            create_account_text.visibility = View.GONE
        }
        else{
            scroll_view.scrollTo(0, scroll_view.top)
            create_account_text.visibility = View.VISIBLE

        }
    }


    private fun validate(email: String, password: String) = email.isNotEmpty() && password.isNotEmpty()


}

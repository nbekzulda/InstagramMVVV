package com.example.instagramclone.activities.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.instagramclone.R
import com.example.instagramclone.activities.*
import com.example.instagramclone.activities.createUser.RegisterActivity
import com.example.instagramclone.activities.home.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener

class LoginActivity : AppCompatActivity(), KeyboardVisibilityEventListener,
    View.OnClickListener {
    override fun onClick(p0: View) {
        when(p0.id){
            R.id.create_account_text -> {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
        }

    }



    private val TAG = "LoginActivity"
    private lateinit var mAuth: FirebaseAuth
    private var viewModel: LoginViewModel? = null

    private lateinit var buttonLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        bindView()
        Log.d(TAG, "onCreate")

        KeyboardVisibilityEvent.setEventListener(this, this)
        coordinateBtnAndInputs(
            login_button,
            email_Input,
            password_Input
        )
        login_button.setOnClickListener(this)
        create_account_text.setOnClickListener(this)

        mAuth = FirebaseAuth.getInstance()

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)


        buttonLogin.setOnClickListener {
            viewModel!!.login(
                email = email_Input.text.toString(),
                password = password_Input.text.toString()
            )
        }

        inputData()
    }

    private fun bindView() {
        buttonLogin = findViewById(R.id.login_button)


    }

    private fun inputData(){
        viewModel!!.liveData.observe(this, Observer { state ->
            when (state){
                is LoginViewModel.State.ShowLoading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is LoginViewModel.State.HideLoading -> {
                    progressBar.visibility = View.INVISIBLE
                    finish()
                }
                is LoginViewModel.State.Result -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()

                }
                is LoginViewModel.State.Error -> {
                    showToast(state.message)
                }
            }
        })
    }

//    override fun onClick(v: View) {
//        when(v.id){
//            R.id.login_button -> {
//                val email = email_Input.text.toString()
//                val password = password_Input.text.toString()
//
//
//                progressBar.visibility = View.VISIBLE
//                if(validate(email, password)){
//                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
//                        if(it.isSuccessful){
//                            progressBar.visibility = View.INVISIBLE
//                            startActivity(Intent(this, MainActivity::class.java))
//                            finish()
//                        }
//                        else{
//                            progressBar.visibility = View.INVISIBLE
//                            showToast("Wrong email or password")
//                        }
//                    }
//                }
//                else{
//                    progressBar.visibility = View.INVISIBLE
//                    showToast("Please enter email and password")
//                }
//            }
//            R.id.create_account_text -> {
//                startActivity(Intent(this, RegisterActivity::class.java))
//            }
//        }
//
//
//    }


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


//    private fun validate(email: String, password: String) = email.isNotEmpty() && password.isNotEmpty()


}

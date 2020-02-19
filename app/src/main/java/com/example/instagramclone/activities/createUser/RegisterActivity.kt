package com.example.instagramclone.activities.createUser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.example.instagramclone.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(
                R.id.frame_layout,
                EmailFragment()
            )
                .commit()
        }
    }

}




//    override fun onNext(email: String) {
//        if (email.isNotEmpty()){
//            mEmail = email
//            mAuth.fetchSignInMethodsForEmail(email){signInMethods ->
//                if (signInMethods.isEmpty()) {
//
//                        supportFragmentManager.beginTransaction().replace(R.id.frame_layout,
//                            NamePassFragment()
//                        )
//                            .addToBackStack(null)
//                            .commit()
//                }
//                else{
//                        showToast("This email already exists")
//                }
//            }
//
//        }
//        else{
//            showToast("Please enter email")
//        }
//    }




//    override fun onRegister(fullname: String, password: String) {
//        if(fullname.isNotEmpty() && password.isNotEmpty()){
//            val email = mEmail
//            if (email != null) {
//                mAuth.createUserWithEmailAndPassword(email, password){
//                    val user = mkUser(fullname, email)
//                    mDatabase.createUser(it.user!!.uid, user) {
//                        startMainActivity()
//                        finish()
//                    }
//                }
//            }
//            else{
//                Log.e(TAG, "onRegister: email is null")
//                showToast("Please enter email")
//                supportFragmentManager.popBackStack()
//            }
//
//        }
//        else{
//            showToast("Please enter full name and password")
//        }
//    }


//    private fun unknownRegisterError(it: Task<out Any>) {
//         Log.e(TAG, "failed to create user profile", it.exception)
//        showToast("Something wrong happened. Please try again later")
//    }
//
//    private fun startMainActivity() {
//        startActivity(Intent(this, MainActivity::class.java))
//        finish()
//    }
//
//    private fun mkUser(fullname: String, email: String): User {
//        val username = mkUsername(fullname)
//        return User(name = fullname, username = username, email = email)
//    }
//    private fun mkUsername(fullname: String) = fullname.toLowerCase().replace(" ", ".")

//    private fun FirebaseAuth.fetchSignInMethodsForEmail(email: String, onSuccess: (List<String>) -> Unit){
//        fetchSignInMethodsForEmail(email).addOnCompleteListener{
//            if (it.isSuccessful){
//                onSuccess (it.result?.signInMethods?: emptyList<String>())
//            }
//            else{
//                showToast(it.exception!!.message!!)
//            }
//        }
//    }

//    private fun DatabaseReference.createUser(uid: String, user: User, onSuccess: () -> Unit){
//        val reference = child("users").child(uid)
//        reference.setValue(user).addOnCompleteListener{
//            if (it.isSuccessful){
//                onSuccess()
//            }
//            else{
//                unknownRegisterError(it)
//            }
//        }
//    }
//
//    private fun FirebaseAuth.createUserWithEmailAndPassword(email: String, password: String, onSuccess: (AuthResult) -> Unit){
//        createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener{
//                if (it.isSuccessful ){
//                    onSuccess(it.result!!)
//                }else{
//                    unknownRegisterError(it)
//                }
//            }
//    }
//
//}

//1 - email, next

//class EmailFragment() : Fragment(){
//
//    private lateinit var mListener: Listener
//    interface Listener{
//        fun onNext(email: String)
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.fragment_register_email, container, false)
//
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        coordinateBtnAndInputs(next_btn, email_register)
//        next_btn.setOnClickListener {
//            val email = email_register.text.toString()
//            mListener.onNext(email)
//        }
//    }
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        mListener = context as Listener
//    }
//
//
//}

//2 - fullname, register

//class NamePassFragment() : Fragment(){
//
//    private lateinit var mListener: Listener
//    interface Listener{
//        fun onRegister(fullname: String, password: String)
//    }
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.fragment_register_namepass, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        coordinateBtnAndInputs(
//            register_btn,
//            fullname_input,
//            password_register
//        )
//        register_btn.setOnClickListener {
//            val fullname = fullname_input.text.toString()
//            val password = password_register.text.toString()
//            mListener.onRegister(fullname, password)
//        }
//    }
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        mListener = context as Listener
//    }
//
//
//
//}
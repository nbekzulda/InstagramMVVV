package com.example.instagramclone.activities.createUser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.instagramclone.R
import com.example.instagramclone.activities.home.MainActivity
import com.example.instagramclone.activities.coordinateBtnAndInputs
import com.example.instagramclone.data.NamePassRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_register_namepass.*
import kotlinx.android.synthetic.main.fragment_register_namepass.view.*

class NamePassFragment() : Fragment(){

    private val viewModel by lazy {
        val namePassRepository = NamePassRepositoryImpl(firebaseAuth = FirebaseAuth.getInstance())
        ViewModelProviders.of(this, NamePassViewModelFactory(namePassRepository)).get(NamePassViewModel::class.java)

    }
    private lateinit var buttonRegister: Button
    private lateinit var email: TextView
    private lateinit var comViewModel: CommunicatorViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_register_namepass, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        bindView(view)
        setData()

        coordinateBtnAndInputs(
            register_btn,
            fullname_input,
            password_register
        )

        comViewModel.liveData.observe(this, object: Observer<Any>{
            override fun onChanged(o: Any?) {
                email.text = o!!.toString()
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        comViewModel = ViewModelProviders.of(activity!!).get(CommunicatorViewModel::class.java)
    }

    private fun bindView(view: View) = with(view){
        buttonRegister = view.findViewById(R.id.register_btn)
        email = view.findViewById(R.id.emailUser)
        buttonRegister.setOnClickListener {
            val fullname = fullname_input.text.toString()
            val password = password_register.text.toString()

            viewModel.onRegister(fullname, password, email.text.toString())

        }
    }

    private fun setData(){
        viewModel.liveData.observe(viewLifecycleOwner, Observer { state ->
            when (state){
                is NamePassViewModel.State.ShowLoading -> {
                    progressBARR.visibility = View.VISIBLE
                }
                is NamePassViewModel.State.HideLoading -> {
                    progressBARR.visibility = View.INVISIBLE

                }
                is NamePassViewModel.State.Result -> {
                    Log.d("email_fragment_result", "onResult")
                    startActivity(Intent(context, MainActivity::class.java))
                    activity?.finish()
                }
                is NamePassViewModel.State.Error -> {
                    Toast.makeText(activity, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
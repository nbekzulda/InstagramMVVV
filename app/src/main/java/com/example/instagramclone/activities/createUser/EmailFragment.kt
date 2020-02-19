package com.example.instagramclone.activities.createUser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.instagramclone.R
import com.example.instagramclone.activities.coordinateBtnAndInputs
import com.example.instagramclone.activities.login.LoginViewModel
import com.example.instagramclone.data.EmailRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_register_email.*
import kotlinx.android.synthetic.main.fragment_register_email.view.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

class EmailFragment : Fragment() {

    private val viewModel by lazy {
        val emailRepository = EmailRepositoryImpl(firebaseAuth = FirebaseAuth.getInstance())
        ViewModelProviders.of(this, EmailViewModelFactory(emailRepository = emailRepository)).get(EmailViewModel::class.java)

    }
    private lateinit var comViewModel: CommunicatorViewModel
    private lateinit var buttonNext: Button

    companion object {
        lateinit var mctx:Context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_register_email, container, false)

        return rootView

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        bindView(view)
        setData()

        buttonNext.setOnClickListener {

            viewModel.onNext(email = email_register.text.toString())
            comViewModel.setMsgCommunicator(email_register.text.toString())
        }

        coordinateBtnAndInputs(next_btn, email_register)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        comViewModel = ViewModelProviders.of(activity!!).get(CommunicatorViewModel::class.java)
    }

   private fun bindView(view: View) = with(view){
        buttonNext = view.findViewById(R.id.next_btn)
    }

    private fun setData(){
        viewModel.liveData.observe(viewLifecycleOwner, Observer { state ->
            when (state){
                is EmailViewModel.State.ShowLoading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is EmailViewModel.State.HideLoading -> {
                    progressBar.visibility = View.INVISIBLE

                }
                is EmailViewModel.State.Result -> {
                    Log.d("email_fragment_result", "onResult")
                    activity?.getSupportFragmentManager()?.beginTransaction()?.replace(R.id.frame_layout,
                           NamePassFragment()
                       )
                        ?.addToBackStack(null)
                        ?.commit()

                }
                is EmailViewModel.State.Error -> {
                    Toast.makeText(activity, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
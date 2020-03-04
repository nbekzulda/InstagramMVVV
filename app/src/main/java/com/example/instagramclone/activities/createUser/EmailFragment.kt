package com.example.instagramclone.activities.createUser

import android.content.Context
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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.instagramclone.R
import com.example.instagramclone.activities.coordinateBtnAndInputs
import com.example.instagramclone.data.EmailRepository
import com.example.instagramclone.data.EmailRepositoryImpl
import com.example.instagramclone.di.DaggerAppComponent
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_register_email.*
import javax.inject.Inject


class EmailFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    private val viewModel by lazy {

        ViewModelProviders.of(this, viewModelFactory).get(EmailViewModel::class.java)

    }
    private lateinit var comViewModel: CommunicatorViewModel
    private lateinit var buttonNext: Button
    private lateinit var registerEmail: TextView



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_register_email, container, false)
        return rootView
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView(view)
        setData()

        coordinateBtnAndInputs(next_btn, email_register)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerAppComponent.create().inject(this)
        comViewModel = ViewModelProviders.of(activity!!).get(CommunicatorViewModel::class.java)
    }

   private fun bindView(view: View) = with(view) {
        buttonNext = view.findViewById(R.id.next_btn)
        registerEmail = view.findViewById(R.id.email_register)

        buttonNext.setOnClickListener {

           viewModel.onNext(email = registerEmail.text.toString())
           comViewModel.setMsgCommunicator(registerEmail.text.toString())
       }
    }


    private fun setData() {
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
                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,
                           NamePassFragment()
                       )
                        .addToBackStack(null)
                        .commit()

                }
                is EmailViewModel.State.Error -> {
                    Toast.makeText(activity, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
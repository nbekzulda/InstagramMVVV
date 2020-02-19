package com.example.instagramclone.activities.createUser

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CommunicatorViewModel : ViewModel() {

    val liveData = MutableLiveData<Any>()

    fun setMsgCommunicator(msg:String){
        liveData.setValue(msg)
    }
}
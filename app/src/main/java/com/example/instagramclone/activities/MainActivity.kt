package com.example.instagramclone.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instagramclone.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*
import java.util.ArrayList

class MainActivity : BaseActivity(0) {

    private val TAG = "MainActivity"
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupBottomNavigation()
        Log.d(TAG, "onCreate")

        val users = ArrayList<String>()
        val username = ArrayList<String>()
        val post = ArrayList<String>()


        recycler_view.adapter =
            PostAdapter(users, username, post)
        recycler_view.layoutManager = LinearLayoutManager(this)

        users.add("https://upload.wikimedia.org/wikipedia/commons/7/7a/Harvard_logo.jpg")
        username.add("kbtu_official")
        post.add("https://tengrinews.kz/userdata/news/2016/news_289672/thumb_xm/photo_178312.jpg")

        users.add("https://admissions.nu.edu.kz/wps/wcm/connect/eeafcc3e-59b1-448a-8403-c21779bd5aed/sst_logo.png?MOD=AJPERES&CACHEID=eeafcc3e-59b1-448a-8403-c21779bd5aed")
        username.add("nu_official")
        post.add("https://kassir.kz/files/venues/194-aRe6cnZL-1508480751.jpg")

        users.add("https://upload.wikimedia.org/wikipedia/ru/0/00/Logotip_KazNU.gif")
        username.add("kaznu_official")
        post.add("https://kapital.kz/4d7f1358d2b68e980dccdf42cc5f1118/c/1440/816/d5d4a7725a5612f25528957a1372bdd6.jpg")

        users.add("https://upload.wikimedia.org/wikipedia/commons/7/7a/Harvard_logo.jpg")
        username.add("harvard_official")
        post.add("https://www.studylab.ru/upload/Institutions/image/big/9ad79a21f09a00097c6a136d12e095f5.jpg")

        users.add("https://i.pinimg.com/originals/4a/21/25/4a2125ea3d703bee63e98de2913ba2ad.png")
        username.add("mit_official")
        post.add("https://www.studylab.ru/upload/Institutions/image/big/a9e89341c4564012f7a6ff7986b9e8a6.jpg")

        users.add("https://identity.stanford.edu/img/SU_SealColor_web3.png")
        username.add("standford_official")
        post.add("https://industrywired.com/wp-content/uploads/2019/08/Stanford-University123.jpg")

        users.add("https://upload.wikimedia.org/wikipedia/ru/d/d8/CaltechLogo.png")
        username.add("caltech_official")
        post.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTkQhkAvzZGUENsQJUFBHY_sEFHnqhCC96kh8fzCUzmNu1bfTrR&s")

        users.add("http://phdru.com/wp-content/uploads/2016/09/msulogo260260.png")
        username.add("msu_official")
        post.add("https://upload.wikimedia.org/wikipedia/commons/d/d1/MSU.jpg")

        users.add("https://www.eduopinions.com/wp-content/uploads/2017/09/National-Taiwan-University-NTU-logo.png")
        username.add("ntu_official")
        post.add("https://s.yimg.com/lo/api/res/1.2/9bNB8kEWRDGQXlBPAqiOoA--/YXBwaWQ9YXBlY21lZGlhO3NtPTE7dz04MDA-/http://media.zenfs.com/en/homerun/feed_manager_auto_publish_494/b025f48d5db2d98deb8cae70aa1a3380")

        users.add("https://images-na.ssl-images-amazon.com/images/I/61HjntKtAYL._SL1000_.jpg")
        username.add("oxford_official")
        post.add("https://www.studylab.ru/upload/Programs/image/big/a3f3f8c065138384636260d7aa496ae5.jpg")

        mAuth = FirebaseAuth.getInstance()

//        auth.signInWithEmailAndPassword("nbekzulda@gmail.com", "16052000nis")
//            .addOnCompleteListener{
//                if(it.isSuccessful){
//                    Log.d(TAG, "signIn: success")
//                }
//                else{
//                    Log.e(TAG, "signIn: failure", it.exception)
//                }
//            }
        sign_out_text.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            mAuth.signOut()

        }
        mAuth.addAuthStateListener {
            if(it.currentUser == null){
                progressBar.visibility = View.INVISIBLE
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        if(mAuth.currentUser == null){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }



    }



}


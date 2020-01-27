package com.example.instagramclone.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramclone.R
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.posts_list.view.*

class PostAdapter(val users: ArrayList<String>, val username: ArrayList<String>, val post: ArrayList<String>) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.posts_list, parent, false)
        val holder = ViewHolder(view)

        return holder
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.user.loadImage(users[position])
        holder.username.text = username[position]
        holder.post.loadImage(post[position])
    }

    private fun ImageView.loadImage(image: String){
        Glide.with(this).load(image).into(this)
    }

    class ViewHolder (view: View): RecyclerView.ViewHolder (view) {
        val user: CircleImageView = view.user_photo_image
        val username: TextView = view.username_text
        val post: ImageView = view.post_image
    }
}
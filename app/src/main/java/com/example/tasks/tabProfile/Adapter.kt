package com.example.tasks.tabProfile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tasks.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class Adapter(options: FirebaseRecyclerOptions<Model>) :
    FirebaseRecyclerAdapter<Model, Adapter.MyViewHolder>(options) {
    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Model) {
        holder.firstName.text = model.firstName
        holder.lastName.text = model.lastName
        holder.phone.text = model.phone
        Glide.with(holder.img1.context).load(model.image).into(holder.img1)
        holder.img1.setOnClickListener { view ->
            val activity = view.context as AppCompatActivity
            activity.supportFragmentManager.beginTransaction().replace(
                R.id.profile_view_pager,
                ViewFragment(model.firstName, model.image, model.email, model.phone)
            ).addToBackStack(null).commit()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.card_view, parent, false)
        return MyViewHolder(view)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img1: ImageView = itemView.findViewById(R.id.card_profile_image_view)
        var firstName: TextView = itemView.findViewById(R.id.card_first_name)
        var phone: TextView = itemView.findViewById(R.id.card_phone)
        var lastName: TextView = itemView.findViewById(R.id.card_last_name)

    }
}
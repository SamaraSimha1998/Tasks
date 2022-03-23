package com.example.tasks.aapoonLoginPage.dashboard.connect

import com.google.firebase.auth.FirebaseAuth

class PrivateChannel {

    private lateinit var auth: FirebaseAuth

    fun getUrl(): String {
        return auth.currentUser!!.uid
    }

}

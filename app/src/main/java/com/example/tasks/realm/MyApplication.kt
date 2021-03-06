package com.example.tasks.realm

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        // creates a version for file
        val config = RealmConfiguration.Builder()
            .name("test.realm")
            .schemaVersion(1)
            .build()
        Realm.setDefaultConfiguration(config)
    }
}
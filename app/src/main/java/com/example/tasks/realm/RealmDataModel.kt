package com.example.tasks.realm

import io.realm.RealmObject

open class RealmDataModel : RealmObject() {
    var id = 0
    var name: String? = null
    var email: String? = null

}
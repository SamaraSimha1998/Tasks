package com.example.tasks.chatBox

class ChatBoxUser {
    var name: String? = null
    var email: String? = null
    var uid: String? = null
    var phone: String? = null
    var profile: String? = null
    var status: String? = null

    constructor() {}

    constructor(name: String?, email: String?, uid: String?, profile: String?, phone: String?, status: String?){
        this.name = name
        this.email = email
        this.uid = uid
        this.phone = phone
        this.profile = profile
        this.status = status
    }
}
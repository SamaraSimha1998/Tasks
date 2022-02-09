package com.example.tasks.chatBox

class ChatBoxUser {
    var name: String? = null
    var email: String? = null
    var uid: String? = null
    var profile: String? = null

    constructor() {}

    constructor(name: String?, email: String?, uid: String?, profile: String?){
        this.name = name
        this.email = email
        this.uid = uid
        this.profile = profile
    }
}
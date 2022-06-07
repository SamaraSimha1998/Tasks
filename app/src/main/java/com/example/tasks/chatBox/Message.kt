package com.example.tasks.chatBox

class Message {
    var message: String? = null
    var senderId: String? = null
    var timestamp: Long = 0
    var currenttime: String? = null

    constructor() {}
    constructor(message: String?, senderId: String?, timestamp: Long, currenttime: String?) {
        this.message = message
        this.senderId = senderId
        this.timestamp = timestamp
        this.currenttime = currenttime
    }
}

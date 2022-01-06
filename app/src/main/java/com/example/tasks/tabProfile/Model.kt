package com.example.tasks.tabProfile

class Model {
    var image: String? = null
    var email: String? = null
    var firstName: String? = null
    var phone: String? = null
    var lastName: String? = null
    private var dob: String? = null
    var gender: String? = null

    constructor() {}
    constructor(image: String?, email: String?, firstName: String?, lastName: String?, dob: String?, phone: String?, gender: String?) {
        this.image = image
        this.email = email
        this.firstName = firstName
        this.phone = phone
        this.lastName = lastName
        this.dob = dob
        this.gender = gender
    }
}

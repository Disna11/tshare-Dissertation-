package com.example.tshare.model

data class chatUser(
    var username: String? = null,
    private var email: String? = null,
    var profilePicture: String? = null,
    private var lastName: String? = null,
    private var address: String? = null,
    private var dob: String? = null,
    private var phone: String? = null)
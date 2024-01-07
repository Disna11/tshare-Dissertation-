package com.example.tshare.model
import java.io.Serializable
import java.util.*

data class User(
    var username: String? = null,
     var email: String? = null,
    var profilePicture: String? = null,
    var lastName: String? = null,
    var address: String? = null,
     var dob: String? = null,
     var phone: String? = null)
package com.example.tshare
import java.io.Serializable
import java.util.*

class User(
    private var username: String? = null,
    private var email: String? = null,
    private var profilePicture: String? = null,
    private var lastName: String? = null,
    private var address: String? = null,
    private var dob: String? = null,
    private var phone: String? = null




    ):Serializable{
    // Getter for username
    fun getUsername(): String? {
        return username
    }

    // Setter for username
    fun setUsername(newUsername: String?) {
        username = newUsername
    }

    // Getter for email
    fun getEmail(): String? {
        return email
    }

    // Setter for email
    fun setEmail(newEmail: String?) {
        email = newEmail
    }

    // Getter for profilePicture
    fun getProfilePicture(): String? {
        return profilePicture
    }

    // Setter for profilePicture
    fun setProfilePicture(newProfilePicture: String?) {
        profilePicture = newProfilePicture
    }

    fun getLastname(): String? {
        return lastName
    }

    // Setter for username
    fun setLastname(newLastname: String?) {
        lastName = newLastname
    }
    fun getAddress(): String? {
        return address
    }

    // Setter for username
    fun setAddress(newAddress: String?) {
        address = newAddress
    }

    fun getDob(): String? {
        return dob
    }

    fun setDob(newDob: String?) {
        dob = newDob
    }
    fun getPhone(): String? {
        return phone
    }

    // Setter for username
    fun setPhone(newPhone: String?) {
        phone = newPhone
    }
}


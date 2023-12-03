package com.example.tshare
import java.io.Serializable

class User(
    private var username: String? = null,
    private var email: String? = null,
    private var profilePicture: String? = null
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
}


package com.example.myapplication.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class UserRegistration {

    @SerializedName("status")
    @Expose
    internal var status: String? = null

    @SerializedName("message")
    @Expose
    internal var message: String? = null

    //.....
    @SerializedName("firstName")
    @Expose
    internal var name: String? = null

    @SerializedName("middleName")
    @Expose
    internal var middleName: String? = null

    @SerializedName("lastName")
    @Expose
    internal var lastName: String? = null

    @SerializedName("emailId")
    @Expose
    internal var email: String? = null

    @SerializedName("ldap")
    @Expose
    internal var ldap: String? = null

    @SerializedName("mobileNo")
    @Expose
    internal var mobile: Long? = null


    fun getStatus(): String? {
        return status
    }

    fun setStatus(status: String) {
        this.status = status
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(messages: String) {
        this.message = messages
    }

    // register....


    fun getName(): String? {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }


    fun getMobile(): Long? {
        return mobile
    }

    fun setMobile(mobile: Long) {
        this.mobile = mobile
    }


    fun getEmail(): String? {
        return email
    }

    fun setEmail(email: String) {
        this.email = email
    }

}

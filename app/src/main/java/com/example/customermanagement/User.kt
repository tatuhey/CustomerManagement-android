package com.example.customermanagement

class User (var id: Int, val name:String , val email: String, val mobile: String) {
    fun getIdText(): String {
        return "$id"
    }

    fun getNameText(): String {
        return "$name"
    }

    fun getEmailText(): String {
        return "$email"
    }

    fun getMobileText(): String {
        return "$mobile"
    }
}
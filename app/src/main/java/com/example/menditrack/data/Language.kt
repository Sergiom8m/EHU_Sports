package com.example.menditrack.data

enum class Language (val type: String, val code: String) {

    EN("English", "en"),
    ES("Español", "es"),
    EU("Euskera", "eu");

    companion object {
        fun getFromCode(code: String) = when (code) {
            EU.code -> EU
            EN.code -> EN
            ES.code -> ES
            else -> EN
        }
    }
}
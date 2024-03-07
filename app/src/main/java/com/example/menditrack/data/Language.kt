package com.example.menditrack.data

// Enumerate to store the available languages (each value identified by a type and a code)
enum class Language (val type: String, val code: String) {

    EN("English", "en"),
    ES("Español", "es"),
    EU("Euskera", "eu");

    // Function to get the language object by passing the code
    companion object {
        fun getFromCode(code: String) = when (code) {
            EU.code -> EU
            EN.code -> EN
            ES.code -> ES
            else -> EN
        }
    }
}
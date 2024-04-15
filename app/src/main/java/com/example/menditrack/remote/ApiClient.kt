package com.example.menditrack.remote

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.menditrack.data.SportActivity
import com.example.menditrack.data.User
import com.example.menditrack.utils.activityToPostActivity
import com.example.menditrack.utils.userToPostUser
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import javax.inject.Singleton

// Custom exception class for user existence
class UserExistsException : Exception()

// Data class representing the structure of activity data to be posted to the API
@Serializable
data class PostActivity(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("distance") val distance: Double,
    @SerialName("init_point") val initPoint: String,
    @SerialName("grade") val grade: Double,
    @SerialName("difficulty") val difficulty: String,
    @SerialName("type") val type: String,
    @SerialName("user_id") val user_id: String
)

// Data class representing the structure of user data to be posted to the API
@Serializable
data class PostUser(
    @SerialName("username") val username: String,
    @SerialName("password") val password: String,
)

// Singleton class responsible for making API requests
@Singleton
class ApiClient @Inject constructor() {

    // HTTP client instance for making requests
    private val httpClient = HttpClient(CIO) {

        // If return code is not a 2xx then throw an exception
        expectSuccess = true

        // Install JSON handler (allows to receive and send JSON data)
        install(ContentNegotiation) { json() }

        // Handle non 2xx status responses
        HttpResponseValidator {
            handleResponseExceptionWithRequest { exception, _ ->
                when {
                    // Handle unauthorized and conflict status codes
                    exception is ClientRequestException && exception.response.status == HttpStatusCode.Unauthorized -> Log.d("HTTP", exception.toString())
                    exception is ClientRequestException && exception.response.status == HttpStatusCode.Conflict -> Log.d("HTTP", exception.toString())
                    // For other exceptions, print stack trace and throw
                    else -> {
                        exception.printStackTrace()
                        Log.d("HTTP", exception.toString())
                        throw exception
                    }
                }
            }
        }
    }

    // Function to create a new user on remote DB
    @Throws(UserExistsException::class)
    suspend fun createUser(user: User) {
         httpClient.post("http://34.71.128.243:8000/users/") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToJsonElement(userToPostUser(user)))
        }
    }

    // Function to retrieve a list of users from remote DB
    @Throws(Exception::class)
    suspend fun getUsers(): List<PostUser> = runBlocking {
        val response = httpClient.get("http://34.71.128.243:8000/users/")
        response.body()
    }

    // Function to clear all users on remote DB
    @Throws(Exception::class)
    suspend fun clearUsers() {
        httpClient.delete("http://34.71.128.243:8000/users/")
    }

    // Function to create a new activity on rmeote DB
    @Throws(Exception::class)
    suspend fun createActivity(activity: SportActivity) {
        httpClient.post("http://34.71.128.243:8000/activities/"){
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToJsonElement(activityToPostActivity(activity)))
        }
    }

    // Function to retrieve a list of activities from remote DB
    @Throws(Exception::class)
    suspend fun getActivities(): List<PostActivity> = runBlocking {
        val response = httpClient.get("http://34.71.128.243:8000/activities/")
        response.body()
    }

    // Function to delete an activity on remote DB
    @Throws(Exception::class)
    suspend fun deleteActivity(activity: SportActivity) {
        httpClient.delete("http://34.71.128.243:8000/activities/${activity.id}/")
    }

    // Function to update an activity on remote DB
    @Throws(Exception::class)
    suspend fun updateActivity(activity: SportActivity) {
        httpClient.put("http://34.71.128.243:8000/activities/${activity.id}"){
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToJsonElement(activityToPostActivity(activity)))
        }
    }

    // Function to retrieve user image on remote DB
    suspend fun getUserImage(username: String): Bitmap? {
        val response = try {
            httpClient.get("http://34.71.128.243:8000/users/${username}/image")
        } catch (e: Exception) {
            null
        }
        if (response != null) {
            val image: ByteArray = response.body()
            return BitmapFactory.decodeByteArray(image, 0, image.size)
        }
        else{
            return response
        }
    }

    // Function to set user image on remote DB
    suspend fun setUserImage(username: String, image: Bitmap) {
        val stream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()
        httpClient.submitFormWithBinaryData(
            url = "http://34.71.128.243:8000/users/${username}/image",
            formData = formData {
                append("file", byteArray, Headers.build {
                    append(HttpHeaders.ContentType, "image/png")
                    append(HttpHeaders.ContentDisposition, "filename=profile_image.png")
                })
            }
        ) { method = HttpMethod.Put }
    }

    // Function to subscribe a device with a token on remote DB
    @Throws(Exception::class)
    suspend fun subscribe(token: String) {
        httpClient.post("http://34.71.128.243:8000/tokens/"){
            contentType(ContentType.Application.Json)
            setBody(mapOf("token" to token))
        }
    }
}
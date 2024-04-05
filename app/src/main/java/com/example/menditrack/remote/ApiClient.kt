package com.example.menditrack.remote

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
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import javax.inject.Inject
import javax.inject.Singleton


private val BASE_URL = "http://34.71.128.243:8000/"

class UserExistsException : Exception()


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


@Serializable
data class PostUser(
    val username: String,
    val password: String,
)


@Singleton
class ApiClient @Inject constructor() {

    private val httpClient = HttpClient(CIO) {

        // If return code is not a 2xx then throw an exception
        expectSuccess = true

        // Install JSON handler (allows to receive and send JSON data)
        install(ContentNegotiation) { json() }

        // Handle non 2xx status responses
        HttpResponseValidator {
            handleResponseExceptionWithRequest { exception, _ ->
                when {
                    exception is ClientRequestException && exception.response.status == HttpStatusCode.Unauthorized -> Log.d("HTTP", exception.toString())
                    exception is ClientRequestException && exception.response.status == HttpStatusCode.Conflict -> Log.d("HTTP", exception.toString())
                    else -> {
                        exception.printStackTrace()
                        Log.d("HTTP", exception.toString())
                        throw exception
                    }
                }
            }
        }
    }

    @Throws(UserExistsException::class)
    suspend fun createUser(user: User) {
         httpClient.post("http://34.71.128.243:8000/users/") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToJsonElement(userToPostUser(user)))
        }
    }

    @Throws(Exception::class)
    suspend fun getUsers(): List<PostUser> = runBlocking {
        val response = httpClient.get("http://34.71.128.243:8000/users/")
        response.body()
    }

    @Throws(Exception::class)
    suspend fun createActivity(activity: SportActivity) {
        httpClient.post("http://34.71.128.243:8000/activities/"){
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToJsonElement(activityToPostActivity(activity)))
        }
    }

    @Throws(Exception::class)
    suspend fun deleteActivity(activity: SportActivity) {
        httpClient.delete("http://34.71.128.243:8000/activities/${activity.id}/")
    }

    @Throws(Exception::class)
    suspend fun updateActivity(activity: SportActivity) {
        httpClient.put("http://34.71.128.243:8000/activities/${activity.id}"){
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToJsonElement(activityToPostActivity(activity)))
        }
    }

    @Throws(Exception::class)
    suspend fun getActivities(): List<PostActivity> = runBlocking {
        val response = httpClient.get("http://34.71.128.243:8000/activities")
        response.body()
    }


}
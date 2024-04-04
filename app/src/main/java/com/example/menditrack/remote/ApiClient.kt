package com.example.menditrack.remote

import android.util.Log
import com.example.menditrack.data.SportActivity
import com.example.menditrack.data.User
import com.example.menditrack.utils.userToPostUser
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import javax.inject.Inject
import javax.inject.Singleton


private val BASE_URL = "http://34.71.128.243:8000/"

class UserExistsException : Exception()


@Serializable
data class PostActivity(
    @SerialName("name") val name: String,
    @SerialName("distance") val distance: Double,
    @SerialName("initPoint") val initPoint: String,
    @SerialName("grade") val grade: Double,
    @SerialName("type") val type: String,
    @SerialName("user_id") val user_id: String
)
private fun activity_postActivity(activity: SportActivity): PostActivity{
    return PostActivity(
        activity.name,
        activity.distance,
        activity.initPoint,
        activity.grade,
        activity.type,
        activity.userId
    )
}


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
        Log.d("HTTP", user.toString())
        val response = httpClient.post("http://34.71.128.243:8000/users/") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToJsonElement(userToPostUser(user)))
        }
        Log.d("HTTP", response.status.toString())
    }

    @Throws(Exception::class)
    suspend fun getUsers(): List<PostUser> = runBlocking {
        val response = httpClient.get("http://34.71.128.243:8000/users")
        response.body()
    }

    @Throws(Exception::class)
    suspend fun download_user_data(username: String): List<PostActivity>? = runBlocking {
        Log.d("HTTP", username)
        val response = httpClient.get("http://34.71.128.243:8000/activities/$username")
        Log.d("HTTP", response.status.toString())
        response.body()
    }

}
package mbolg.tasker.data.api.auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi{
    @POST("/todo/api/users/")
    fun createUser(@Body body: AuthRequest): Call<AuthResponse>

    @POST("/todo/api/get_token/")
    fun getToken(@Body body: AuthRequest): Call<AuthResponse>
}

class AuthRequest(val email: String, val password: String)


class AuthResponse {

    @SerializedName("email")
    @Expose
    var email: String? = null
    @SerializedName("password")
    @Expose
    var password: String? = null
    @SerializedName("token")
    @Expose
    var token: String? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    override fun toString(): String {
        return "AuthResponse(email=$email, password=$password, token=$token, createdAt=$createdAt)"
    }
}
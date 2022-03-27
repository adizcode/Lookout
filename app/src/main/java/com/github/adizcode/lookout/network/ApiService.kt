package com.github.adizcode.lookout.network

import com.github.adizcode.lookout.model.data.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

const val BASE_URL = "https://api-lookout.herokuapp.com/api/"

interface ApiService {

    @POST("login")
    suspend fun loginStudent(@Body credentials: LoginCredentials): StudentLoginResponse

    @POST("login-teacher")
    suspend fun loginTeacher(@Body credentials: LoginCredentials): TeacherLoginResponse

    @POST("generate")
    suspend fun generateOtp(@Body email: TeacherEmail): GenerateOtpResponse

    @POST("verify-otp")
    suspend fun submitOtp(@Body otpBody: SubmitOtpBody): SubmitOtpResponse

    companion object {
        var apiService: ApiService? = null
        fun getInstance(): ApiService {
            if (apiService == null) {
                apiService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ApiService::class.java)
            }
            return apiService!!
        }
    }
}
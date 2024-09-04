package com.example.find_a_doctor
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


var gson = GsonBuilder()
    .setLenient()
    .create()
object RetrofitInstance {
    private const val BASE_URL = "https://findadoctorapi-a9gaghb5fdgjb0a3.eastus-01.azurewebsites.net/"
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }
}
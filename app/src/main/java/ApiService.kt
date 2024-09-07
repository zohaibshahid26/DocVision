package com.example.find_a_doctor

import AppointmentDTO
import BookAppointmentDTO
import DoctorDTO
import FavoriteDoctorDTO
import HospitalDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.http.Path

interface ApiService {

    // Hospital endpoints
    @GET("api/Hospital")
    suspend fun getHospitals(): List<HospitalDTO>

    @GET("api/Hospital/{id}")
    suspend fun getHospital(@Path("id") id: Int): HospitalDTO

    @POST("api/Hospital")
    suspend fun createHospital(@Body hospitalDTO: HospitalDTO): Response<HospitalDTO>

    @PUT("api/Hospital/{id}")
    suspend fun updateHospital(@Path("id") id: Int, @Body hospitalDTO: HospitalDTO): Response<Void>

    @DELETE("api/Hospital/{id}")
    suspend fun deleteHospital(@Path("id") id: Int): Response<Void>

    // Doctor endpoints
    @GET("api/Doctor")
    suspend fun getDoctors(): List<DoctorDTO>

    @GET("api/Doctor/{id}")
    suspend fun getDoctor(@Path("id") id: Int): DoctorDTO

    @POST("api/Doctor")
    suspend fun createDoctor(@Body doctorDTO: DoctorDTO): Response<DoctorDTO>

    @PUT("api/Doctor/{id}")
    suspend fun updateDoctor(@Path("id") id: Int, @Body doctorDTO: DoctorDTO): Response<Void>

    @DELETE("api/Doctor/{id}")
    suspend fun deleteDoctor(@Path("id") id: Int): Response<Void>

    // Appointment endpoints
    @POST("api/Appointments/book")
    suspend fun bookAppointment(@Body bookAppointmentDto: BookAppointmentDTO): Response<Void>

    @GET("api/Appointments/by-doctor/{doctorId}")
    suspend fun getAppointmentsByDoctor(@Path("doctorId") doctorId: Int): List<AppointmentDTO>

    @GET("api/Appointments/by-patient/{patientId}")
    suspend fun getAppointmentsByPatient(@Path("patientId") patientId: String): List<AppointmentDTO>

    @PUT("api/Appointments/cancel/{appointmentId}")
    suspend fun cancelAppointment(@Path("appointmentId") appointmentId: Int): Response<Void>

    @POST("api/FavoriteDoctor")
    suspend fun addFavoriteDoctor(@Body favoriteDoctorDTO: FavoriteDoctorDTO): Response<Void>

    @GET("api/FavoriteDoctor/{patientId}")
    suspend fun getFavoriteDoctors(@Path("patientId") patientId: String): List<FavoriteDoctorDTO>

}

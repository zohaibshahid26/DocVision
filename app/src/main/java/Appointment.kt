package com.example.find_a_doctor


data class Appointment(
    val id: Int,                // Unique identifier for the appointment
    val doctorId: Int,          // ID of the doctor associated with the appointment
    val userId: Int,            // ID of the user who made the appointment
    val availabilityId: Int,   // ID representing the availability slot

)

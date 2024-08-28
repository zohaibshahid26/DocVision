data class AppointmentDTO(
    val id: Int,
    val appointmentTime: String, // Use ISO 8601 format
    val patientId: String,
    val doctorName: String,
    val status: Boolean
)

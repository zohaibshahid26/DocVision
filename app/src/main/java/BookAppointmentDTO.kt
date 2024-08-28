data class BookAppointmentDTO(
    val appointmentTime: String, // Use ISO 8601 format for date-time strings (e.g., "2024-08-28T14:00:00")
    val patientId: String,
    val doctorId: Int
)

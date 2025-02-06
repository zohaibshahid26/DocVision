data class DoctorDTO(
    val id: Int,
    val name: String,
    val doctorImage: String, // URL or path to the doctor's image
    val specialization: String,
    val qualification: String,
    val experience: Int, // Number of years of experience
    val about: String,
    val email: String, // Added email field
    val isAvailable: Boolean, // Added availability field
    val appointmentTime: String, // Added appointment time field, assuming ISO 8601 format
    val hospital: HospitalDTO // Nested HospitalDTO object
)
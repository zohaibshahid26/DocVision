data class DoctorDTO(
    val id: Int,
    val name: String,
    val doctorImage: String, // URL or path to the doctor's image
    val specialization: String,
    val qualification: String,
    val experience: Int, // Number of years of experience
    val about: String,
    val email: String,
    val isAvailable: Boolean, // Availability of the doctor
    val hospital: HospitalDTO // Nested HospitalDTO object
)
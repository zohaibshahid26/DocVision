data class HospitalDTO(
    val id: Int,
    val name: String,
    val location: String,
    val locationUrl: String?,
    val openingHours: String?,
    val numberOfAvailableDoctors: Int,
    val rating: Double,
    val img: String?,
    val about: String?,
    val contactNo: String?
)

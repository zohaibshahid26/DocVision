data class Doctor(
    val id: Int,
    val hospitalId: Int,
    val image: Int, // Assuming image is a drawable resource ID
    val name: String,
    val specialization: String,
    val qualification: String,
    val experience: String,
    val reviews: String,
    val about: String,
    val availabilityId: Int
)

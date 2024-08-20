package com.example.find_a_doctor

import Doctor
import Hospital
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random

class HospitalProfileActivity : BaseActivity() {

    private lateinit var hospitalNameTextView: TextView
    private lateinit var hospitalLocationTextView: TextView
    private lateinit var hospitalOpeningHoursTextView: TextView
    private lateinit var hospitalAvailableDoctorsTextView: TextView
    private lateinit var hospitalImageView: ImageView
    private lateinit var hospitalAboutTextView: TextView
    private lateinit var callHelplineButton: Button
    private lateinit var mapDirectionsButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Find the content frame and inflate the specific layout
        val contentFrame: FrameLayout = findViewById(R.id.content_frame)
        layoutInflater.inflate(R.layout.activity_hospital_profile, contentFrame, true)

        // Set up the header title
        val title = intent.getStringExtra("TITLE") ?: "Hospital Profile"
        setHeaderTitle(title)

        Log.d("HospitalProfileActivity", "onCreate called")
        // Initialize views
        hospitalImageView = findViewById(R.id.hospitalImage)
        Log.d("HospitalProfileActivity", "Image view initialized")
        hospitalNameTextView = findViewById(R.id.hospitalName)
        Log.d("HospitalProfileActivity", "Name text view initialized")
        hospitalLocationTextView = findViewById(R.id.hospital_address)
        Log.d("HospitalProfileActivity", "Location text view initialized")
        hospitalOpeningHoursTextView = findViewById(R.id.opening_hours_time)
        Log.d("HospitalProfileActivity", "Opening hours text view initialized")
        hospitalAvailableDoctorsTextView = findViewById(R.id.available_doctors_count)
        Log.d("HospitalProfileActivity", "Available doctors text view initialized")

        hospitalAboutTextView = findViewById(R.id.aboutHospital)
        Log.d("HospitalProfileActivity", "About text view initialized")

        callHelplineButton = findViewById(R.id.btnCallHelpline)
        Log.d("HospitalProfileActivity", "Call helpline button initialized")
        mapDirectionsButton = findViewById(R.id.btnMapDirections)
        Log.d("HospitalProfileActivity", "Map directions button initialized")

        // Get the hospital ID from the intent
        val hospitalId = intent.getIntExtra("hospitalId", -1) // -1 or any default value

        // Fetch hospital details using the ID and update the UI
        fetchHospitalDetails(hospitalId)

        // Generate random doctors for the hospital
        val randomDoctors = generateRandomDoctors()

        // Set up the RecyclerView and adapter for doctors
        val doctorsRecyclerView: RecyclerView = findViewById(R.id.doctor_list)
        val doctorsAdapter = DoctorAdapter(this, randomDoctors)
        doctorsRecyclerView.layoutManager = LinearLayoutManager(this)
        doctorsRecyclerView.adapter = doctorsAdapter
    }

    private fun fetchHospitalDetails(hospitalId: Int?) {
        // Simulate fetching hospital details from a data source
        if (hospitalId != null) {

            Log.d("Fetching hospital details for ID: $hospitalId", "Fetching hospital details")
            // Example of hardcoded data, replace this with actual data retrieval logic
            val hospital = getHospitalById(hospitalId)

            // Update the UI with hospital details
            hospitalNameTextView.text = hospital.name
            hospitalLocationTextView.text = hospital.location
            hospitalOpeningHoursTextView.text = hospital.openingHours
            hospitalAvailableDoctorsTextView.text = hospital.availableDoctors.toString()


            // Load the hospital image (if you have a drawable or URL)
            hospitalImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.hospital)) // Replace with actual image loading logic
        }
    }

    private fun getHospitalById(hospitalId: Int): Hospital {
        // Replace this with your actual data retrieval logic
        // For demonstration, returning a dummy hospital
        return Hospital(
            id = hospitalId.toInt(),
            name = "Hospital $hospitalId",
            location = "Location $hospitalId",
            openingHours = "24 Hours",
            availableDoctors = (1..10).random(),

        )
    }


    private fun generateRandomDoctors(): List<Doctor> {
        return List(20) { index ->
            Doctor(
                id = index + 1,
                hospitalId = Random.nextInt(100, 200),
                image = when (Random.nextInt(1, 6)) {
                    1 -> R.drawable.doctor
                    2 -> R.drawable.doctor
                    3 -> R.drawable.doctor
                    4 -> R.drawable.doctor
                    5 -> R.drawable.doctor
                    else -> R.drawable.doctor
                },
                name = listOf(
                    "Dr. John Smith", "Dr. Emily Johnson", "Dr. Michael Brown", "Dr. Sarah Davis",
                    "Dr. William Wilson", "Dr. Olivia Martinez", "Dr. James Taylor", "Dr. Ava White",
                    "Dr. Benjamin Harris", "Dr. Mia Clark", "Dr. Noah Lewis", "Dr. Isabella Young",
                    "Dr. Lucas Walker", "Dr. Sophia Robinson", "Dr. Ethan King", "Dr. Charlotte Hall",
                    "Dr. Alexander Scott", "Dr. Amelia Adams", "Dr. Mason Nelson", "Dr. Harper Carter"
                )[Random.nextInt(20)],
                specialization = listOf(
                    "Cardiologist", "Neurologist", "Orthopedic Surgeon", "Pediatrician", "Gynecologist",
                    "Oncologist", "Dermatologist", "Pulmonologist", "Rheumatologist", "Urologist"
                )[Random.nextInt(10)],
                qualification = listOf(
                    "MBBS, MD", "MBBS, DM", "MBBS, MS", "MBBS, DCH", "MBBS, FRCS",
                    "MBBS, MCh", "MBBS, MRCP", "MBBS, MRCS", "MBBS, M.D.", "MBBS, DNB"
                )[Random.nextInt(10)],
                experience = "${Random.nextInt(1, 30)} years",
                reviews = String.format("%.2f / 5", Random.nextFloat() * 5),
            about = "Experienced in ${listOf(
                    "Cardiology", "Neurology", "Orthopedic Surgery", "Pediatrics", "Gynecology",
                    "Oncology", "Dermatology", "Pulmonology", "Rheumatology", "Urology"
                )[Random.nextInt(10)]}.",
                availabilityId = Random.nextInt(1, 5)
            )
        }
    }


    override fun customizeHeader() {
        // Customize header for HospitalProfileActivity if needed
    }
}
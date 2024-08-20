package com.example.find_a_doctor

import Hospital
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

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

    override fun customizeHeader() {
        // Customize header for HospitalProfileActivity if needed
    }
}
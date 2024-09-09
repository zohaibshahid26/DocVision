package com.example.find_a_doctor

import DoctorAdapter
import DoctorDTO
import HospitalDTO
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HospitalProfileActivity : BaseActivity() {

    private lateinit var hospitalNameTextView: TextView
    private lateinit var hospitalLocationTextView: TextView
    private lateinit var hospitalOpeningHoursTextView: TextView
    private lateinit var hospitalAvailableDoctorsTextView: TextView
    private lateinit var hospitalImageView: ImageView
    private lateinit var hospitalAboutTextView: TextView
    private lateinit var callHelplineButton: Button
    private lateinit var mapDirectionsButton: Button
    private lateinit var doctorsRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showLoading()

        // Find the content frame and inflate the specific layout
        val contentFrame: FrameLayout = findViewById(R.id.content_frame)
        layoutInflater.inflate(R.layout.activity_hospital_profile, contentFrame, true)

        // Set up the header title
        val title = intent.getStringExtra("TITLE") ?: "Hospital Profile"
        setHeaderTitle(title)

        // Initialize views
        hospitalImageView = findViewById(R.id.hospitalImage)
        hospitalNameTextView = findViewById(R.id.hospitalName)
        hospitalLocationTextView = findViewById(R.id.hospital_address)
        hospitalOpeningHoursTextView = findViewById(R.id.opening_hours_time)
        hospitalAvailableDoctorsTextView = findViewById(R.id.available_doctors_count)
        hospitalAboutTextView = findViewById(R.id.aboutHospital)
        callHelplineButton = findViewById(R.id.btnCallHelpline)
        mapDirectionsButton = findViewById(R.id.btnMapDirections)
        doctorsRecyclerView = findViewById(R.id.doctor_list)
        doctorsRecyclerView.layoutManager = LinearLayoutManager(this)


        // Get the hospital ID from the intent
        val hospitalId = intent.getIntExtra("hospitalId", -1)

        // Fetch hospital details and doctors using coroutines
        if (hospitalId != -1) {

            fetchHospitalDetails(hospitalId)
            fetchDoctorsForHospital(hospitalId)
            hideLoading()
        }
    }

    private fun fetchHospitalDetails(hospitalId: Int) {

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.getHospital(hospitalId)
                withContext(Dispatchers.Main) {
                    updateHospitalUI(response)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    e.printStackTrace()
                    // Handle error, show a message to the user
                }
            }
        }
    }

    private fun fetchDoctorsForHospital(hospitalId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.getDoctors() // Fetch all doctors or adjust endpoint as needed
                withContext(Dispatchers.Main) {
                    val doctors = response.filter { it.hospital.id == hospitalId }
                    updateDoctorsRecyclerView(doctors)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    e.printStackTrace()
                    // Handle error, show a message to the user
                }
            }
        }
    }

    private fun updateHospitalUI(hospitalDTO: HospitalDTO) {
        hospitalNameTextView.text = hospitalDTO.name
        hospitalLocationTextView.text = hospitalDTO.location
        hospitalOpeningHoursTextView.text = hospitalDTO.openingHours
        hospitalAvailableDoctorsTextView.text = hospitalDTO.numberOfAvailableDoctors.toString()
        hospitalAboutTextView.text = hospitalDTO.about

        // Set up the Call Helpline button click listener
        callHelplineButton.setOnClickListener {
            // Handle the call helpline button click here
            val phoneNumber = hospitalDTO.contactNo // Replace with the actual phone number
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phoneNumber")
            startActivity(intent)
        }

        // Set up the Get Map Directions button click listener
        mapDirectionsButton.setOnClickListener {
            // Handle the map directions button click here
            val mapUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=${hospitalDTO.locationUrl}")
            val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
            mapIntent.setPackage("com.google.android.apps.maps") // Specify Google Maps package
            startActivity(mapIntent)
        }

        // Load the hospital image using Glide
        Glide.with(this)
            .load(hospitalDTO.img) // URL or path to the image
            .placeholder(R.drawable.hospital) // Placeholder image
            .error(R.drawable.hospital) // Error image
            .into(hospitalImageView)
    }

    private fun updateDoctorsRecyclerView(doctorsDTO: List<DoctorDTO>) {
        val doctorsAdapter = DoctorAdapter(this, doctorsDTO, FavoriteDoctorDatabaseHelper(this))
        doctorsRecyclerView.adapter = doctorsAdapter
    }

    override fun customizeHeader() {
        // Customize header for HospitalProfileActivity if needed
    }
}

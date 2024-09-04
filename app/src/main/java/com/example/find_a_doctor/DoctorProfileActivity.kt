package com.example.find_a_doctor

import BookAppointmentDTO
import DoctorDTO
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DoctorProfileActivity : BaseActivity() {

    private lateinit var scrollView: ScrollView
    private lateinit var tabProfileDetails: TextView
    private lateinit var tabAvailability: TextView
    private lateinit var tabVideos: TextView
    private lateinit var tabReviews: TextView
    private lateinit var tabAbout: TextView
    private lateinit var tabDiffDxn: TextView

    private lateinit var profileDetailsSection: View
    private lateinit var availabilitySection: View
    private lateinit var videosSection: View
    private lateinit var reviewsSection: View
    private lateinit var aboutSection: View
    private lateinit var diffDxnSection: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val frameLayout: FrameLayout = findViewById(R.id.content_frame)
        layoutInflater.inflate(R.layout.activity_doctor_profile, frameLayout, true)
        val title = intent.getStringExtra("TITLE") ?: "Doctor Profile"
        setHeaderTitle(title)

        val doctorId = intent.getIntExtra("doctorId", 0)
        if (doctorId != 0) {
            fetchDoctorById(doctorId)
        } else {
            Toast.makeText(this, "Invalid doctor ID", Toast.LENGTH_SHORT).show()
        }



        scrollView = findViewById(R.id.scrollView)

        // Initialize tabs
        tabProfileDetails = findViewById(R.id.tabProfileDetails)
        tabAvailability = findViewById(R.id.tabAvailability)
        tabVideos = findViewById(R.id.tabVideos)
        tabReviews = findViewById(R.id.tabReviews)
        tabAbout = findViewById(R.id.tabAbout)
        tabDiffDxn = findViewById(R.id.tabDiffDxn)

        // Initialize sections
        profileDetailsSection = findViewById(R.id.profileDetailsSection)
        availabilitySection = findViewById(R.id.availabilitySection)
        videosSection = findViewById(R.id.videosSection)
        reviewsSection = findViewById(R.id.reviewsSection)
        aboutSection = findViewById(R.id.aboutSection)
        diffDxnSection = findViewById(R.id.diffDxnSection)

        // Set click listeners
        // Set click listeners
        tabProfileDetails.setOnClickListener { onTabClicked(it, profileDetailsSection) }
        tabAvailability.setOnClickListener { onTabClicked(it, availabilitySection) }
        tabVideos.setOnClickListener { onTabClicked(it, videosSection) }
        tabReviews.setOnClickListener { onTabClicked(it, reviewsSection) }
        tabAbout.setOnClickListener { onTabClicked(it, aboutSection) }
        tabDiffDxn.setOnClickListener { onTabClicked(it, diffDxnSection) }
    }

    private fun fetchDoctorDetails(doctorId: Int) {
        // Fetch the doctor details, example using Retrofit
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.getDoctor(doctorId)
                withContext(Dispatchers.Main) {
                    updateDoctorUI(response)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Failed to load doctor details", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun updateDoctorUI(doctor: DoctorDTO) {
        profileNameTextView.text = doctor.name
        profileSpecialtyTextView.text = doctor.specialization
        profileQualificationsTextView.text = doctor.qualification
        Glide.with(this)
            .load(doctor.doctorImage)
            .placeholder(R.drawable.ic_avatar)
            .error(R.drawable.ic_avatar)
            .into(profileImage)
    }


    private fun bookAppointment() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val doctorId = intent.getIntExtra("doctorId", 0)  // Assuming doctorId is passed via intent

            // Placeholder for appointment time; you might want to add a picker or another method to set this
            val appointmentTime = "2024-09-10T14:00:00" // ISO 8601 format

            val bookAppointmentDTO = BookAppointmentDTO(appointmentTime, userId, doctorId)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitInstance.api.bookAppointment(bookAppointmentDTO)
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Appointment booked successfully!", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(applicationContext, "Failed to book appointment: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(applicationContext, "Network error: Failed to connect", Toast.LENGTH_LONG).show()
                    }
                }
            }
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }



    private fun onTabClicked(clickedTab: View, section: View) {
        // Reset all tabs to default style
        resetTabStyles()

         // Highlight the clicked tab
        val tintColor = ColorStateList.valueOf(Color.parseColor("#ffff4444"))
        clickedTab.backgroundTintList = tintColor
        (clickedTab as TextView).setTextColor(Color.WHITE)

        // Scroll to the top of the section
        scrollView.post {
            scrollView.smoothScrollTo(0, section.top)
        }
    }

    private fun resetTabStyles() {
        val defaultBackgroundColor = Color.WHITE
        val defaultTextColor = Color.BLACK

        val defaultTintList = ColorStateList.valueOf(defaultBackgroundColor)

        tabProfileDetails.backgroundTintList = defaultTintList
        tabProfileDetails.setTextColor(defaultTextColor)

        tabAvailability.backgroundTintList = defaultTintList
        tabAvailability.setTextColor(defaultTextColor)

        tabVideos.backgroundTintList = defaultTintList
        tabVideos.setTextColor(defaultTextColor)

        tabReviews.backgroundTintList = defaultTintList
        tabReviews.setTextColor(defaultTextColor)

        tabAbout.backgroundTintList = defaultTintList
        tabAbout.setTextColor(defaultTextColor)

        tabDiffDxn.backgroundTintList = defaultTintList
        tabDiffDxn.setTextColor(defaultTextColor)
    }


    override fun customizeHeader() {

    }


}
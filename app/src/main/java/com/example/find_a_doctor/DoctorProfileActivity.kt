package com.example.find_a_doctor
import java.util.*
import BookAppointmentDTO
import DoctorDTO
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DoctorProfileActivity : AppCompatActivity() {

    private lateinit var scrollView: ScrollView
    private lateinit var tabProfileDetails: TextView
    private lateinit var tabAvailability: TextView

    private lateinit var tabReviews: TextView
    private lateinit var tabAbout: TextView


    private lateinit var profileDetailsSection: View
    private lateinit var availabilitySection: View

    private lateinit var reviewsSection: View
    private lateinit var aboutSection: View


    private lateinit var profileImage: ImageView
    private lateinit var profileNameTextView: TextView
    private lateinit var profileSpecialtyTextView: TextView
    private lateinit var profileQualificationsTextView: TextView
    private lateinit var profileExperienceTextView: TextView
    private lateinit var profileAboutTextView: TextView
    private lateinit var availabilityTime:TextView

    private lateinit var buttonCallHelpline: Button
    private lateinit var buttonBookAppointment: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_profile)

        val title = intent.getStringExtra("TITLE") ?: "Doctor Profile"
        setHeaderTitle(title)

        val doctorId = intent.getIntExtra("doctorId", 0)
        if (doctorId != 0) {
            fetchDoctorDetails(doctorId)
        } else {
            Toast.makeText(this, "Invalid doctor ID", Toast.LENGTH_SHORT).show()
        }

        // Initialize views
        scrollView = findViewById(R.id.scrollView)
        Log.d("DoctorProfileActivity", "ScrollView is initialized")

        tabProfileDetails = findViewById(R.id.tabProfileDetails)
        Log.d("DoctorProfileActivity", "tabProfileDetails is initialized")

        tabAvailability = findViewById(R.id.tabAvailability)
        Log.d("DoctorProfileActivity", "tabAvailability is initialized")


        tabReviews = findViewById(R.id.tabReviews)
        Log.d("DoctorProfileActivity", "tabReviews is initialized")

        tabAbout = findViewById(R.id.tabAbout)
        Log.d("DoctorProfileActivity", "tabAbout is initialized")


        profileDetailsSection = findViewById(R.id.profileDetailsSection)
        Log.d("DoctorProfileActivity", "profileDetailsSection is initialized")

        availabilitySection = findViewById(R.id.availabilitySection)
        Log.d("DoctorProfileActivity", "availabilitySection is initialized")



        reviewsSection = findViewById(R.id.reviewsSection)
        Log.d("DoctorProfileActivity", "reviewsSection is initialized")

        aboutSection = findViewById(R.id.aboutSection)
        Log.d("DoctorProfileActivity", "aboutSection is initialized")


        profileImage = findViewById(R.id.profileImage)
        Log.d("DoctorProfileActivity", "profileImage is initialized")

        profileNameTextView = findViewById(R.id.profileName)
        Log.d("DoctorProfileActivity", "profileNameTextView is initialized")

        profileSpecialtyTextView = findViewById(R.id.profileSpecialty)
        Log.d("DoctorProfileActivity", "profileSpecialtyTextView is initialized")

        profileQualificationsTextView = findViewById(R.id.profileQualifications)
        Log.d("DoctorProfileActivity", "profileQualificationsTextView is initialized")

        profileExperienceTextView = findViewById(R.id.profileExperience)
        Log.d("DoctorProfileActivity", "profileExperienceTextView is initialized")

        profileAboutTextView = findViewById(R.id.profileDetailsSection1) // This should be changed to a specific TextView if needed
        Log.d("DoctorProfileActivity", "profileAboutTextView is initialized")

        availabilityTime = findViewById(R.id.availability)

        buttonCallHelpline = findViewById(R.id.button2)
        Log.d("DoctorProfileActivity", "buttonCallHelpline is initialized")

        buttonBookAppointment = findViewById(R.id.button1)
        Log.d("DoctorProfileActivity", "buttonBookAppointment is initialized")

        // Set click listeners
        tabProfileDetails.setOnClickListener { onTabClicked(it, profileDetailsSection) }
        tabAvailability.setOnClickListener { onTabClicked(it, availabilitySection) }

        tabReviews.setOnClickListener { onTabClicked(it, reviewsSection) }
        tabAbout.setOnClickListener { onTabClicked(it, aboutSection) }

        buttonBookAppointment.setOnClickListener(){
            bookAppointment()
        }

    }

    private fun fetchDoctorDetails(doctorId: Int) {
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
        // Create SpannableString for each TextView to apply different styles
        val specialtyText = SpannableString("Specialty: ${doctor.specialization}")
        val qualificationsText = SpannableString("Qualification: ${doctor.qualification}")
        val experienceText = SpannableString("Experience: ${doctor.experience} years")

        // Apply bold style to the hardcoded text
        specialtyText.setSpan(StyleSpan(Typeface.BOLD), 0, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        qualificationsText.setSpan(
            StyleSpan(android.graphics.Typeface.BOLD),
            0,
            12,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        experienceText.setSpan(
            StyleSpan(android.graphics.Typeface.BOLD),
            0,
            11,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        availabilityTime.text = doctor.appointmentTime
        // Set the formatted text to TextViews
        profileSpecialtyTextView.text = specialtyText
        profileQualificationsTextView.text = qualificationsText
        profileExperienceTextView.text = experienceText
        profileAboutTextView.text = doctor.about

        Glide.with(this)
            .load(doctor.doctorImage)
            .placeholder(R.drawable.ic_avatar)
            .error(R.drawable.ic_avatar)
            .into(profileImage)

        // Example of dynamic content based on availability
        val availabilityText = if (doctor.isAvailable) "Available" else "Not Available"
        availabilitySection.findViewById<TextView>(R.id.availabilityContent).text = availabilityText

        if (!doctor.isAvailable) {
            buttonBookAppointment.isClickable = false
        }
    }
    fun bookAppointment() {
         val appointmentTime = findViewById<TextView>(R.id.availability).text.toString()
        // Proceed with using formattedAppointmentTime for your booking DTO
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val doctorId = intent.getIntExtra("doctorId", 0)  // Assuming doctorId is passed via intent

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
                        Toast.makeText(applicationContext, "Network error: Failed to connect ${e.message} ", Toast.LENGTH_LONG).show()
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



        tabReviews.backgroundTintList = defaultTintList
        tabReviews.setTextColor(defaultTextColor)

        tabAbout.backgroundTintList = defaultTintList
        tabAbout.setTextColor(defaultTextColor)


    }

    private fun setHeaderTitle(title: String) {
        // Set the header title (implement as needed)
    }
}

package com.example.find_a_doctor

import BookAppointmentDTO
import DoctorDTO
import android.app.AlertDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
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

class DoctorProfileActivity : BaseActivity() {

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
    private lateinit var availabilityTime: TextView

    private lateinit var buttonCallHelpline: Button
    private lateinit var buttonBookAppointment: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Find the content frame and inflate the specific layout
        val contentFrame: FrameLayout = findViewById(R.id.content_frame)
        layoutInflater.inflate(R.layout.activity_doctor_profile, contentFrame, true)

        val title = intent.getStringExtra("TITLE") ?: "Doctor Profile"
        setHeaderTitle(title)

        // Initialize views
        scrollView = findViewById(R.id.scrollView)
        tabProfileDetails = findViewById(R.id.tabProfileDetails)
        tabAvailability = findViewById(R.id.tabAvailability)
        tabReviews = findViewById(R.id.tabReviews)
        tabAbout = findViewById(R.id.tabAbout)

        profileDetailsSection = findViewById(R.id.profileDetailsSection)
        availabilitySection = findViewById(R.id.availabilitySection)
        reviewsSection = findViewById(R.id.reviewsSection)
        aboutSection = findViewById(R.id.aboutSection)

        profileImage = findViewById(R.id.profileImage)
        profileNameTextView = findViewById(R.id.profileName)
        profileSpecialtyTextView = findViewById(R.id.profileSpecialty)
        profileQualificationsTextView = findViewById(R.id.profileQualifications)
        profileExperienceTextView = findViewById(R.id.profileExperience)
        profileAboutTextView = findViewById(R.id.profileDetailsSection1)
        availabilityTime = findViewById(R.id.availability)

        buttonCallHelpline = findViewById(R.id.button2)
        buttonBookAppointment = findViewById(R.id.button1)

        // Set click listeners
        tabProfileDetails.setOnClickListener { onTabClicked(it, profileDetailsSection) }
        tabAvailability.setOnClickListener { onTabClicked(it, availabilitySection) }
        tabReviews.setOnClickListener { onTabClicked(it, reviewsSection) }
        tabAbout.setOnClickListener { onTabClicked(it, aboutSection) }


        buttonCallHelpline.setOnClickListener{
            showHelplineDialog()
        }

        buttonBookAppointment.setOnClickListener {
            bookAppointment()
        }

        // Fetch doctor details if the doctorId is valid
        val doctorId = intent.getIntExtra("doctorId", 0)
        if (doctorId != 0) {
            fetchDoctorDetails(doctorId)
        } else {
            Toast.makeText(this, "Invalid doctor ID", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchDoctorDetails(doctorId: Int) {
        showLoading() // Show loading indicator while fetching data

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.getDoctor(doctorId)
                withContext(Dispatchers.Main) {
                    updateDoctorUI(response)
                    hideLoading() // Hide loading indicator when data is loaded
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    //hideLoading() // Hide loading indicator in case of an error
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
            buttonBookAppointment.isEnabled = false
            buttonBookAppointment.alpha = 0.5f
            buttonBookAppointment.text = "Not Available"
        } else {
            buttonBookAppointment.setOnClickListener {
                bookAppointment()

                //add a delay

                Thread.sleep(1000)

                // Start the AppointmentActivity when booking an appointment
                val intent = Intent(this, AppointmentActivity::class.java)
                intent.putExtra("TITLE", "Appointments")
                startActivity(intent)
            }
        }
    }

    private fun bookAppointment() {
        val appointmentTime = findViewById<TextView>(R.id.availability).text.toString()
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
                        Toast.makeText(applicationContext, "Network error: ${e.message}", Toast.LENGTH_LONG).show()
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



    private fun showHelplineDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_helpline, null)
        val builder = AlertDialog.Builder(this, R.style.CustomDialogTheme)
            .setView(dialogView)
            .setCancelable(true)

        val dialog = builder.create()
        dialog.show()

        val callLayout1 = dialogView.findViewById<LinearLayout>(R.id.call_layout_1)
        val callLayout2 = dialogView.findViewById<LinearLayout>(R.id.call_layout_2)
        val number1TextView = dialogView.findViewById<TextView>(R.id.number_1)
        val number2TextView = dialogView.findViewById<TextView>(R.id.number_2)

        callLayout1.setOnClickListener {
            val phoneNumber1 = number1TextView.text.toString()
            makePhoneCall(phoneNumber1)
        }

        callLayout2.setOnClickListener {
            val phoneNumber2 = number2TextView.text.toString()
            makePhoneCall(phoneNumber2)
        }

        val closeButton: ImageView? = dialog.findViewById(R.id.close_button)
        closeButton?.setOnClickListener {
            dialog.dismiss()
        }
    }
    private fun makePhoneCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
        startActivity(intent)
    }
    override fun customizeHeader() {
        // Customize Header
    }
}

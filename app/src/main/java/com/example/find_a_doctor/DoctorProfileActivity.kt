package com.example.find_a_doctor

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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
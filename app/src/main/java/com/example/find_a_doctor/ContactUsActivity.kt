package com.example.find_a_doctor

import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class ContactUsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val frameLayout: FrameLayout = findViewById(R.id.content_frame)
        layoutInflater.inflate(R.layout.activity_contact_us, frameLayout, true)

        val title = intent.getStringExtra("TITLE") ?: "Contact Us"
        setHeaderTitle(title)


    }

    override fun customizeHeader() {
        // Customize header for DoctorActivity if needed
    }

}
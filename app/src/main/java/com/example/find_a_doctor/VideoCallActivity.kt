package com.example.find_a_doctor//package com.example.find_a_doctor
//
//import android.os.Bundle
//import android.os.Handler
//import android.os.Looper
//import android.widget.Button
//import android.widget.TextView
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import java.text.SimpleDateFormat
//import java.util.Date
//import java.util.Locale
//
//class VideoCallActivity : AppCompatActivity() {
//
//    private lateinit var doctorIdTextView: TextView
//    private lateinit var userIdTextView: TextView
//    private lateinit var dateTimeTextView: TextView
//    private lateinit var videoCallBtn: Button
//    private lateinit var audioCallBtn: Button
//
//    private lateinit var appointmentDateTime: Date
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_video_call)
//
//        // Initialize views
//        doctorIdTextView = findViewById(R.id.doctor_id_text_view)
//        userIdTextView = findViewById(R.id.user_id_text_view)
//        dateTimeTextView = findViewById(R.id.date_time_text_view)
//        videoCallBtn = findViewById(R.id.video_call_btn)
//        audioCallBtn = findViewById(R.id.audio_call_btn)
//
//        // Get data from Intent
//        val doctorId = intent.getStringExtra("doctorId") ?: "N/A"
//        val userId = intent.getStringExtra("userId") ?: "N/A"
//        val dateTime = intent.getStringExtra("dateTime") ?: "N/A"
//
//        // Set data to views
//        doctorIdTextView.text = "Doctor ID: $doctorId"
//        userIdTextView.text = "User ID: $userId"
//        dateTimeTextView.text = "Date & Time: $dateTime"
//
//        // Parse DateTime
//        val dateFormat = SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault())
//        appointmentDateTime = try {
//            dateFormat.parse(dateTime) ?: Date()
//        } catch (e: Exception) {
//            Date()
//        }
//
//        // Disable buttons initially
//        videoCallBtn.isEnabled = false
//        audioCallBtn.isEnabled = false
//
//        // Check if the current time is past the appointment time to enable buttons
//        checkAppointmentTime()
//
//        // Set up button actions
//        videoCallBtn.setOnClickListener {
//            startVideoCall()
//        }
//
//        audioCallBtn.setOnClickListener {
//            startAudioCall()
//        }
//    }
//
//    private fun startVideoCall() {
//        videoCallBtn.isEnabled = false
//        // Logic to start video call
//    }
//
//    private fun startAudioCall() {
//        audioCallBtn.isEnabled = false
//        // Logic to start audio call
//    }
//
//    private fun checkAppointmentTime() {
//        val handler = Handler(Looper.getMainLooper())
//        val runnable = object : Runnable {
//            override fun run() {
//                val currentTime = Date()
//                if (currentTime >= appointmentDateTime) {
//                    videoCallBtn.isEnabled = true
//                    audioCallBtn.isEnabled = true
//                } else {
//                    handler.postDelayed(this, 1000) // Check every second
//                }
//            }
//        }
//        handler.post(runnable)
//    }
//}
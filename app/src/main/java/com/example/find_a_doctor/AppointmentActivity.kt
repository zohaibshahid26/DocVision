package com.example.find_a_doctor

import AppointmentDTO
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.find_a_doctor.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppointmentActivity : BaseActivity() {

    private lateinit var appointmentRecyclerView: RecyclerView
    private lateinit var appointmentAdapter: AppointmentAdapter
    private lateinit var contentFrame: FrameLayout
    private lateinit var appointmentList: List<AppointmentDTO> // Store the list of appointments

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Find the content frame and inflate the specific layout
        contentFrame = findViewById(R.id.content_frame)
        layoutInflater.inflate(R.layout.activity_appointment, contentFrame, true)

        // Set up the header title
        val title = intent.getStringExtra("TITLE") ?: "Appointments"
        setHeaderTitle(title)

        // Initialize RecyclerView
        appointmentRecyclerView = findViewById(R.id.appointment_list)
        appointmentRecyclerView.layoutManager = LinearLayoutManager(this)

        appointmentList = listOf()

        appointmentAdapter = AppointmentAdapter(this, appointmentList)
        appointmentRecyclerView.adapter = appointmentAdapter

        fetchAppointments()
    }

    private fun fetchAppointments() {
        showLoading()  // Show progress bar

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    val userId = user!!.uid
                    // Fetch appointments for the current user
                    val response = RetrofitInstance.api.getAppointmentsByPatient(userId)

                    // filter appointments by status
                    val filteredAppointments = response.filter { it.status }

                    withContext(Dispatchers.Main) {
                        appointmentAdapter.updateData(filteredAppointments)
                        hideLoading()  // Hide progress bar
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Failed to load Appointment details", Toast.LENGTH_LONG).show()
                    hideLoading()  // Hide progress bar in case of error
                }
            }
        }
    }

    override fun customizeHeader() {
        // Customize header for AppointmentActivity if needed
        // For example, set a custom header title or color
    }
}

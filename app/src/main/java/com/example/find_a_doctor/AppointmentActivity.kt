package com.example.find_a_doctor

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.find_a_doctor.R

class AppointmentActivity : BaseActivity() {

    private lateinit var appointmentRecyclerView: RecyclerView
    private lateinit var appointmentAdapter: AppointmentAdapter
    private lateinit var contentFrame: FrameLayout
    private lateinit var appointmentList: List<Appointment> // Store the list of appointments

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
//        // Sample data for appointments
//        appointmentList = generateSampleAppointments(10)

        // Initialize the adapter with the sample appointments list
        appointmentAdapter = AppointmentAdapter(this, appointmentList)
        appointmentRecyclerView.adapter = appointmentAdapter

//        // Set up the button listeners
//        setupButtonListeners()
    }

    private fun getAppointmentsById(): List<Appointment> {
        val appointments = listOf(
            Appointment(1, 1, 1, 1),

            Appointment(2, 2, 2, 2),
            Appointment(3, 3, 3, 3),
            Appointment(4, 4, 4, 4),
            Appointment(5, 5, 5, 5),
        )

        return appointments
    }
    private fun generateSampleAppointments(count: Int): List<Appointment> {
        return List(count) { index ->
            Appointment(
                id = index + 1,
                doctorId = (index % 5) + 1, // Example doctor IDs
                userId = 1, // Example user ID
                availabilityId = (index % 10) + 1

            )
        }
    }

//    private fun setupButtonListeners() {
//        findViewById<Button>(R.id.btnReschedule).setOnClickListener {
//            handleReschedule()
//        }
//
//        findViewById<Button>(R.id.btnCancel).setOnClickListener {
//            handleCancel()
//        }
//    }

    private fun handleReschedule() {
        // Implement rescheduling logic
        // For example, navigate to another activity or show a dialog
        // Example: Intent to start a rescheduling activity
        // val intent = Intent(this, RescheduleActivity::class.java)
        // startActivity(intent)
    }

    private fun handleCancel() {
        // Implement cancel logic
        // For example, show a confirmation dialog or remove the appointment
        // Example: Show a confirmation dialog
        // AlertDialog.Builder(this)
        //     .setTitle("Cancel Appointment")
        //     .setMessage("Are you sure you want to cancel this appointment?")
        //     .setPositiveButton("Yes") { _, _ ->
        //         // Cancel appointment logic here
        //     }
        //     .setNegativeButton("No", null)
        //     .show()
    }


    override fun customizeHeader() {
        // Customize header for AppointmentActivity if needed
        // For example, set a custom header title or color
    }
}

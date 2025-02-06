// AppointmentAdapter.kt
package com.example.find_a_doctor

import AppointmentDTO
import android.content.Context
import android.content.Intent

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppointmentAdapter(
    private val context: Context,
    private var appointmentList: List<AppointmentDTO>
) : RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {

    inner class AppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appointmentTitle: TextView = itemView.findViewById(R.id.appointmentTitle)
        val appointmentDateTime: TextView = itemView.findViewById(R.id.appointmentDateTime)
        val btnViewDoctor: Button = itemView.findViewById(R.id.btnViewDoctor)
        val btnCancel: Button = itemView.findViewById(R.id.btnCancel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_appointment_profile, parent, false)
        return AppointmentViewHolder(view)
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointment = appointmentList[position]

        // Bind data to views
        holder.appointmentTitle.text = "Appointment with ${appointment.doctorName}"
        holder.appointmentDateTime.text = appointment.appointmentTime // Implement this method

        // Set click listeners
        holder.btnViewDoctor.setOnClickListener {

            // Handle the click event for "View Doctor" button
            val doctorId:Int = appointment.doctorId
            val intent = Intent(context, DoctorProfileActivity::class.java)
            intent.putExtra("doctorId",doctorId)
            intent.putExtra("TITLE","Doctor Profile")

            Log.d("AppointmentAdapter", "doctorId: $doctorId")
            context.startActivity(intent)
        }
        holder.btnCancel.setOnClickListener {
            val appointmentId:Int = appointment.id
            cancelAppointment(context,appointmentId)
        }
    }

    override fun getItemCount(): Int = appointmentList.size

    // Update data in the adapter
    fun updateData(newAppointmentList: List<AppointmentDTO>) {
        appointmentList = newAppointmentList
        notifyDataSetChanged()
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun cancelAppointment(context: Context, appointmentId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Call API to cancel the appointment
                val response = RetrofitInstance.api.cancelAppointment(appointmentId)

                // Log the raw response for debugging
                Log.d("AppointmentAdapter", "Response Code: ${response.code()}")
                Log.d("AppointmentAdapter", "Response Body: ${response.body()}")

                if (response.isSuccessful) {
                    val message = response.body() ?: "Appointment Cancelled"
                    Log.d("AppointmentAdapter", "Appointment Cancelled: $message")
                } else {
                    Log.e("AppointmentAdapter", "Failed to cancel appointment: ${response.errorBody()?.string()}")
                }

                // Update UI after the API call is successful
                withContext(Dispatchers.Main) {

                    Toast.makeText(context, "Appointment Cancelled", Toast.LENGTH_LONG).show()

                    // delete the current activity from the stack

                    (context as? AppointmentActivity)?.finish()

                    // start the AppointmentActivity again

                    val intent = Intent(context, AppointmentActivity::class.java)
                    intent.putExtra("TITLE", "Appointments")
                    context.startActivity(intent)
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Network error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "An error occurred: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }



}

package com.example.find_a_doctor

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.find_a_doctor.R

class AppointmentAdapter(
    private val context: Context,
    private var appointments: List<Appointment>
) : RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {

    // ViewHolder class to hold the views for each item
    class AppointmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val doctorImageView: ImageView = view.findViewById(R.id.doctorImage) // Updated ID
        val doctorNameTextView: TextView = view.findViewById(R.id.doctorName) // Updated ID
        val appointmentTimeTextView: TextView = view.findViewById(R.id.appointmentDateTime) // Updated ID
        val appointmentStatusTextView: TextView = view.findViewById(R.id.appointmentTypeValue) // Updated ID
        val rescheduleButton: Button = view.findViewById(R.id.btnReschedule) // Updated ID
        val cancelButton: Button = view.findViewById(R.id.btnCancel) // Updated ID
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_appointment, parent, false)
        return AppointmentViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointment = appointments[position]
        holder.doctorImageView.setImageResource(appointment.doctorId) // Assuming image is a drawable resource ID

//        holder.doctorNameTextView.text = appointment.doctorId
//        holder.appointmentTimeTextView.text = appointment.time
//        holder.appointmentStatusTextView.text = appointment.status

        holder.rescheduleButton.setOnClickListener {
            // Handle rescheduling logic here
        }

        holder.cancelButton.setOnClickListener {
            // Handle canceling logic here
        }
    }

    // Return the size of the dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return appointments.size
    }

    fun updateData(newAppointments: List<Appointment>) {
        appointments = newAppointments
        notifyDataSetChanged()
    }





}

package com.example.find_a_doctor

import Hospital
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.find_a_doctor.R

class HospitalAdapter(private val context: Context, private var hospitals: List<Hospital>) : RecyclerView.Adapter<HospitalAdapter.HospitalViewHolder>() {

    // ViewHolder class to hold the views for each item
    class HospitalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val HospitalIdTextView: TextView = view.findViewById(R.id.hospital_id)
        val hospitalNameTextView: TextView = view.findViewById(R.id.hospital_name)
        val hospitalLocationTextView: TextView = view.findViewById(R.id.hospital_address)
        val openingHoursTextView: TextView = view.findViewById(R.id.opening_hours_time)
        val availableDoctorsTextView: TextView = view.findViewById(R.id.available_doctors_count)
        val viewProfileButton: Button = view.findViewById(R.id.btn_view_profile) // Reference to the View Profile button
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HospitalViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_hospital, parent, false)
        return HospitalViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: HospitalViewHolder, position: Int) {
        val hospital = hospitals[position]
        holder.HospitalIdTextView.text = hospital.id.toString()
        holder.hospitalNameTextView.text = hospital.name
        holder.hospitalLocationTextView.text = hospital.location
        holder.openingHoursTextView.text = hospital.openingHours
        holder.availableDoctorsTextView.text = hospital.availableDoctors.toString()

        // Set up the View Profile button click listener
        holder.viewProfileButton.setOnClickListener {
            // Start the Profile Activity and pass the hospital's ID or other necessary data
            val intent = Intent(context, HospitalProfileActivity::class.java)
            intent.putExtra("hospitalId", hospital.id) // Assuming Hospital has an id field
            context.startActivity(intent)
        }
    }

    // Return the size of the dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return hospitals.size
    }

    fun updateData(newHospitals: List<Hospital>) {
        hospitals = newHospitals
        notifyDataSetChanged() // Notify the adapter to refresh the list
    }
}
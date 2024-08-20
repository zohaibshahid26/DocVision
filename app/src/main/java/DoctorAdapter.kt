package com.example.find_a_doctor

import Doctor
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DoctorAdapter(private val context: Context, private var doctors: List<Doctor>) : RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {

    // ViewHolder class to hold the views for each item
    class DoctorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val doctorImageView: ImageView = view.findViewById(R.id.doctor_image)
        val doctorNameTextView: TextView = view.findViewById(R.id.doctor_name)
        val doctorSpecializationTextView: TextView = view.findViewById(R.id.doctor_specialization)
        val doctorQualificationTextView: TextView = view.findViewById(R.id.doctor_qualification)
        val doctorExperienceTextView: TextView = view.findViewById(R.id.doctor_experience)
        val doctorReviewsTextView: TextView = view.findViewById(R.id.doctor_review_counts)

        val viewProfileButton: Button = view.findViewById(R.id.btn_view_profile) // Reference to the View Profile button
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_doctor, parent, false)
        return DoctorViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val doctor = doctors[position]
        holder.doctorImageView.setImageResource(doctor.image) // Assuming image is a drawable resource ID
        holder.doctorNameTextView.text = doctor.name
        holder.doctorSpecializationTextView.text = doctor.specialization
        holder.doctorQualificationTextView.text = doctor.qualification
        holder.doctorExperienceTextView.text = doctor.experience
        holder.doctorReviewsTextView.text = doctor.reviews

        // Set up the View Profile button click listener
        holder.viewProfileButton.setOnClickListener {
            // Start the Doctor Profile Activity and pass the doctor's ID or other necessary data
            val intent = Intent(context, DoctorProfileActivity::class.java)
            intent.putExtra("doctorId", doctor.id)
            intent.putExtra("TITLE", "Doctor Profile") // Pass the doctor's ID
            context.startActivity(intent)
        }
    }

    // Return the size of the dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return doctors.size
    }

    fun updateData(newDoctors: List<Doctor>) {
        doctors = newDoctors
        notifyDataSetChanged() // Notify the adapter to refresh the list
    }
}

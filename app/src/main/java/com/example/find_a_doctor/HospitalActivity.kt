package com.example.find_a_doctor

import Hospital
import HospitalAdapter
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HospitalActivity : AppCompatActivity() {
    private lateinit var hospitalRecyclerView: RecyclerView
    private lateinit var hospitalAdapter: HospitalAdapter
    private lateinit var horizontalList: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hospital)

        // Reference to the LinearLayout inside HorizontalScrollView
        horizontalList = findViewById(R.id.top_hospitals_horizontal_list)

        // Sample data for top hospitals
        val top_hospitals = listOf(
            Hospital("Hospital A", availableDoctors = 10, location = "Location A", openingHours = "0"),
            Hospital("Hospital B", availableDoctors = 8, location = "Location B", openingHours = "0"),
            Hospital("Hospital C", availableDoctors = 12, location = "Location C", openingHours = "0"),
            Hospital("Hospital D", availableDoctors = 5, location = "Location D", openingHours = "0"),
            Hospital("Hospital E", availableDoctors = 15, location = "Location E", openingHours = "0"),
            // Add more hospitals as needed
        )

        // Populate the horizontal list
        for (hospital in top_hospitals) {
            // Inflate the item layout
            val itemView = layoutInflater.inflate(R.layout.list_item_hospital_card, horizontalList, false)

            // Find views in the inflated layout
            val imageView = itemView.findViewById<ImageView>(R.id.item_image)
            val nameTextView = itemView.findViewById<TextView>(R.id.item_name)
            val doctorsTextView = itemView.findViewById<TextView>(R.id.item_doctors)
            val locationTextView = itemView.findViewById<TextView>(R.id.item_location)

            // Set the image (use the same drawable for simplicity)
            imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.hospital)) // Replace with your drawable

            // Set the text for each item
            nameTextView.text = hospital.name
            doctorsTextView.text = "Doctors: ${hospital.availableDoctors}"
            locationTextView.text = hospital.location

            // Add the item view to the horizontal list
            horizontalList.addView(itemView)
        }

        // Initialize RecyclerView
        hospitalRecyclerView = findViewById(R.id.hospital_list)
        hospitalRecyclerView.layoutManager = LinearLayoutManager(this)

        // Sample data for hospitals
        val hospitals = List(30) { index ->
            Hospital(
                name = "Hospital ${index + 1}",
                location = "Location ${index + 1}",
                openingHours = if (index % 2 == 0) "24 Hours" else "9 AM - 5 PM",
                availableDoctors = (1..10).random()
            )
        }

        // Set adapter for RecyclerView
        hospitalAdapter = HospitalAdapter(this, hospitals)
        hospitalRecyclerView.adapter = hospitalAdapter
    }
}
package com.example.find_a_doctor

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.activity.ComponentActivity

import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import java.sql.Array

@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.navigation_view)

        val menuButton: ImageButton = findViewById(R.id.menu_button)
        menuButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

//        // Find the LinearLayout within the HorizontalScrollView
//        val diseasesLayout: LinearLayout = findViewById(R.id.diseases)
//
//        // Example of adding items dynamically
//        for (i in 1..10) {
//            // Create a new TextView
//            val textView = TextView(this).apply {
//                text = "Disease $i"
//                setPadding(16, 16, 16, 16)
//                setBackgroundResource(R.drawable.bg_service) // Optional: Set a background
//                textSize = 12f
//            }
//
//            // Add the TextView to the LinearLayout
//            diseasesLayout.addView(textView)

        // Find the LinearLayout within the HorizontalScrollView
        val symptomsLayout: LinearLayout = findViewById(R.id.symptoms)
        val diseasesLayout: LinearLayout = findViewById(R.id.diseases)

        // Get LayoutInflater to inflate the item layout
        val inflater = LayoutInflater.from(this)

        // Example of adding symptoms items dynamically
        for (i in 1..6) {
            // Inflate the item layout for symptoms
            val itemView = inflater.inflate(R.layout.item_disease, symptomsLayout, false)

            // Find and configure the ImageView and TextView
            val imageView = itemView.findViewById<ImageView>(R.id.imageViewDisease)
            val textView = itemView.findViewById<TextView>(R.id.textViewDiseaseName)

            // Set data for the views
            imageView.setImageResource(R.drawable.ic_helpline) // Example drawable
            textView.text = "Symptom"

            // Add the inflated item layout to the LinearLayout
            symptomsLayout.addView(itemView)
        }

        // Example of adding items dynamically
        for (i in 1..10) {
            // Inflate the item layout
            val itemView = inflater.inflate(R.layout.item_disease, diseasesLayout, false)

            // Find and configure the ImageView and TextView
            val imageView = itemView.findViewById<ImageView>(R.id.imageViewDisease)
            val textView = itemView.findViewById<TextView>(R.id.textViewDiseaseName)

            // Set data for the views
            imageView.setImageResource(R.drawable.ic_helpline) // Example drawable
            textView.text = "Disease"

            // Add the inflated item layout to the LinearLayout
            diseasesLayout.addView(itemView)

        }

    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}
package com.example.find_a_doctor

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
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

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density + 0.5f).toInt() // Adding 0.5 for rounding to the nearest pixel
    }


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
        val imageContainer: LinearLayout = findViewById(R.id.image_container)
        val symptomsLayout: LinearLayout = findViewById(R.id.symptoms)
        val diseasesLayout: LinearLayout = findViewById(R.id.diseases)


        // Get LayoutInflater to inflate the item layout
        val inflater = LayoutInflater.from(this)



// Define the gap in pixels
        val gapInPixels = 16 // Or a fixed value

// Define image resources
        val imageResourceIds = listOf(R.drawable.promotion2, R.drawable.promotion, R.drawable.promotion2)
        val diseaseResourceIds = listOf(R.drawable.disease1, R.drawable.disease2, R.drawable.disease3,R.drawable.disease4, R.drawable.disease5, R.drawable.disease6,R.drawable.disease7, R.drawable.disease8, R.drawable.disease9,R.drawable.disease10)

        val symptomResourceId = diseaseResourceIds.shuffled()

        for (imageResId in imageResourceIds) {
            // Create and configure the ImageView
            val imageView = ImageView(this)
            imageView.layoutParams = LinearLayout.LayoutParams(
                dpToPx(350),
                LinearLayout.LayoutParams.MATCH_PARENT
            )


            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.setImageResource(imageResId)

            // Set margin for the image
            val params = imageView.layoutParams as LinearLayout.LayoutParams
            params.setMargins(gapInPixels, 0, gapInPixels, 0) // Left, Top, Right, Bottom margins
            imageView.layoutParams = params

            // Add the ImageView to the LinearLayout
            imageContainer.addView(imageView)

        }

        // Example of adding symptoms items dynamically
        for (i in symptomResourceId) {
            // Inflate the item layout for symptoms
            val itemView = inflater.inflate(R.layout.item_disease, symptomsLayout, false)

            // Find and configure the ImageView and TextView
            val imageView = itemView.findViewById<ImageView>(R.id.imageViewDisease)
            val textView = itemView.findViewById<TextView>(R.id.textViewDiseaseName)

            // Set data for the views
            imageView.setImageResource(i) // Example drawable
            textView.text = "Symptom"

            // Add the inflated item layout to the LinearLayout
            symptomsLayout.addView(itemView)
        }


        // Example of adding items dynamically
        for (i in diseaseResourceIds) {
            // Inflate the item layout
            val itemView = inflater.inflate(R.layout.item_disease, diseasesLayout, false)

            // Find and configure the ImageView and TextView
            val imageView = itemView.findViewById<ImageView>(R.id.imageViewDisease)
            val textView = itemView.findViewById<TextView>(R.id.textViewDiseaseName)

            // Set data for the views
            imageView.setImageResource(i) // Example drawable
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
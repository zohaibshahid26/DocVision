package com.example.find_a_doctor

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
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


        val menuButton: ImageButton = findViewById(R.id.menu_button)
        menuButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

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

        val helpline_text:LinearLayout = findViewById(R.id.helpline);

        helpline_text.setOnClickListener {
            showHelplineDialog()
        }

        val btnOpenHospital: LinearLayout = findViewById(R.id.btn_open_hospital) // The button in your current layout
        btnOpenHospital.setOnClickListener {
            // Intent to start the new activity
            val intent = Intent(this, HospitalActivity::class.java)
            startActivity(intent)
        }

    }

    private fun showHelplineDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_helpline, null)
        val builder = AlertDialog.Builder(this, R.style.CustomDialogTheme)
            .setView(dialogView)
            .setCancelable(true) // Allows the dialog to be dismissed by clicking outside or pressing back

        val dialog = builder.create()
        dialog.show()

        // Find LinearLayouts inside the dialog
        val callLayout1 = dialogView.findViewById<LinearLayout>(R.id.call_layout_1)
        val callLayout2 = dialogView.findViewById<LinearLayout>(R.id.call_layout_2)

        // Find TextViews to get phone numbers
        val number1TextView = dialogView.findViewById<TextView>(R.id.number_1)
        val number2TextView = dialogView.findViewById<TextView>(R.id.number_2)

        // Set OnClickListener to each LinearLayout
        callLayout1.setOnClickListener {
            val phoneNumber1 = number1TextView.text.toString()
            makePhoneCall(phoneNumber1)
        }

        callLayout2.setOnClickListener {
            val phoneNumber2 = number2TextView.text.toString()
            makePhoneCall(phoneNumber2)
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun makePhoneCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
        startActivity(intent)
    }

}
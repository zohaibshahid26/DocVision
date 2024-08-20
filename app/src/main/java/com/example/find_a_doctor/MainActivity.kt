package com.example.find_a_doctor

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : ComponentActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var overlay: View

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density + 0.5f).toInt() // Adding 0.5 for rounding to the nearest pixel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        progressBar = findViewById(R.id.progress_bar)
        overlay = findViewById(R.id.overlay)

        val menuButton: ImageButton = findViewById(R.id.menu_button)
        menuButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
//
        // Set up NavigationView
        val navigationView: NavigationView = findViewById(R.id.navigation_view)


        navigationView.setNavigationItemSelectedListener {
            // Handle navigation item clicks
            Log.d("navigationView","Inside")
            handleMenuItemClick(it.itemId)
            true
        }

        // Find the LinearLayout within the HorizontalScrollView
        val imageContainer: LinearLayout = findViewById(R.id.image_container)
        val symptomsLayout: LinearLayout = findViewById(R.id.symptoms)
        val diseasesLayout: LinearLayout = findViewById(R.id.diseases)

        // Get LayoutInflater to inflate the item layout
        val inflater = LayoutInflater.from(this)

        // Define the gap in pixels
        val gapInPixels = dpToPx(16) // Adjust the gap in pixels

        // Define image resources
        val imageResourceIds = listOf(R.drawable.promotion2, R.drawable.promotion, R.drawable.promotion2)
        val diseaseResourceIds = listOf(R.drawable.disease1, R.drawable.disease2, R.drawable.disease3, R.drawable.disease4, R.drawable.disease5, R.drawable.disease6, R.drawable.disease7, R.drawable.disease8, R.drawable.disease9, R.drawable.disease10)

        // Add images to imageContainer
        for (imageResId in imageResourceIds) {
            val imageView = ImageView(this)
            imageView.layoutParams = LinearLayout.LayoutParams(
                dpToPx(350),
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.setImageResource(imageResId)

            val params = imageView.layoutParams as LinearLayout.LayoutParams
            params.setMargins(gapInPixels, 0, gapInPixels, 0) // Left, Top, Right, Bottom margins
            imageView.layoutParams = params

            imageContainer.addView(imageView)
        }

        // Add symptoms items dynamically
        for (i in diseaseResourceIds.shuffled()) {
            val itemView = inflater.inflate(R.layout.item_disease, symptomsLayout, false)
            val imageView = itemView.findViewById<ImageView>(R.id.imageViewDisease)
            val textView = itemView.findViewById<TextView>(R.id.textViewDiseaseName)
            imageView.setImageResource(i)
            textView.text = "Symptom"
            symptomsLayout.addView(itemView)
        }

        // Add disease items dynamically
        for (i in diseaseResourceIds) {
            val itemView = inflater.inflate(R.layout.item_disease, diseasesLayout, false)
            val imageView = itemView.findViewById<ImageView>(R.id.imageViewDisease)
            val textView = itemView.findViewById<TextView>(R.id.textViewDiseaseName)
            imageView.setImageResource(i)
            textView.text = "Disease"
            diseasesLayout.addView(itemView)
        }

        val helplineText: LinearLayout = findViewById(R.id.helpline)
        helplineText.setOnClickListener {
            showHelplineDialog()
        }

        val btnOpenHospital: CardView = findViewById(R.id.btn_open_hospital)
        btnOpenHospital.setOnClickListener {
            overlay.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE

            val intent = Intent(this, HospitalActivity::class.java)
            intent.putExtra("TITLE", "All Hospitals")
            startActivity(intent)

            progressBar.postDelayed({
                progressBar.visibility = View.GONE
                overlay.visibility = View.GONE
            }, 1500)
        }
    }

    private fun showHelplineDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_helpline, null)
        val builder = AlertDialog.Builder(this, R.style.CustomDialogTheme)
            .setView(dialogView)
            .setCancelable(true)

        val dialog = builder.create()
        dialog.show()

        val callLayout1 = dialogView.findViewById<LinearLayout>(R.id.call_layout_1)
        val callLayout2 = dialogView.findViewById<LinearLayout>(R.id.call_layout_2)
        val number1TextView = dialogView.findViewById<TextView>(R.id.number_1)
        val number2TextView = dialogView.findViewById<TextView>(R.id.number_2)

        callLayout1.setOnClickListener {
            val phoneNumber1 = number1TextView.text.toString()
            makePhoneCall(phoneNumber1)
        }

        callLayout2.setOnClickListener {
            val phoneNumber2 = number2TextView.text.toString()
            makePhoneCall(phoneNumber2)
        }

        val closeButton: ImageView? = dialog.findViewById(R.id.close_button)
        closeButton?.setOnClickListener {
            dialog.dismiss()
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

    private fun handleMenuItemClick(itemId: Int) {
        when (itemId) {
            R.id.nav_home -> {
                // Handle the Home action
                showToast("Home selected")
                Log.d("nav_item_Clicked","Home Selected")
            }
            R.id.nav_profile -> {
                // Handle the Profile action
                showToast("Profile selected")
            }
            R.id.nav_appointment -> {
                // Handle the Appointments action
                showToast("Appointments selected")
            }
            R.id.nav_search_doctor -> {
                // Handle the Search Doctor action
                showToast("Search Doctor selected")
            }
            R.id.nav_search_hospital -> {
                // Handle the Search Hospital action
                showToast("Search Hospital selected")
            }
            R.id.contact_us -> {
                // Handle the Contact Us action
                showToast("Contact Us selected")
            }
            R.id.settings -> {
                // Handle the Settings action
                showToast("Settings selected")
            }
            R.id.logout -> {
                // Handle the Logout action
                showToast("Log out selected")
            }
            else -> {
                // Handle unknown menu item
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

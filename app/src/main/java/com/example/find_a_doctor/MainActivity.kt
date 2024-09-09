package com.example.find_a_doctor

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var overlay: View
    private lateinit var userNameTextView: TextView

    private lateinit var auth: FirebaseAuth


    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density + 0.5f).toInt() // Adding 0.5 for rounding to the nearest pixel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        auth = FirebaseAuth.getInstance()

        userNameTextView = findViewById(R.id.user_name)
        // Get the current user
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Set the TextView with the user's email or display name
            // You can use currentUser.displayName if available, or currentUser.email
            val username = currentUser.email
            //neglect the @gmail.com
            val index = username?.indexOf("@") ?: -1

            if (index != -1) {
                val usernameWithoutDomain = "Welcome Back, " + username?.substring(0, index)
                userNameTextView.text = usernameWithoutDomain ?: "No username available"
            } else {
                userNameTextView.text = username ?: "No username available"
            }
        } else {
            userNameTextView.text = "User not logged in"
        }

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

        val notificationButton: ImageButton = findViewById(R.id.notification_icon)
        notificationButton.setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
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

        // Define your symptoms array
        val symptoms = arrayOf(
            "Fever", "Cough", "Headache", "Nausea",
            "Fatigue", "Sore Throat", "Shortness of Breath",
            "Fatigue", "Sore Throat", "Shortness of Breath",
            "Muscle Pain", "Chills", "Runny Nose"
        )

        // Ensure diseaseResourceIds has exactly 10 items
        val shuffledResourceIds = diseaseResourceIds.shuffled()

        // Add symptoms items dynamically
        for ((index, symptomId) in shuffledResourceIds.withIndex()) {
            if (index < symptoms.size) {
                val itemView = inflater.inflate(R.layout.item_disease, symptomsLayout, false)
                val imageView = itemView.findViewById<ImageView>(R.id.imageViewDisease)
                val textView = itemView.findViewById<TextView>(R.id.textViewDiseaseName)

                imageView.setImageResource(symptomId)
                textView.text = symptoms[index]  // Set the corresponding symptom name

                symptomsLayout.addView(itemView)
            }
        }



        // List of disease names
        val diseaseNames = listOf(
            "Influenza",
            "Diabetes",
            "Hypertension",
            "Asthma",
            "Tuberculosis",
            "Pneumonia",
            "Cancer",
            "Arthritis",
            "Eczema",
            "Migraine"
        )

        for (i in diseaseResourceIds) {
            val itemView = inflater.inflate(R.layout.item_disease, diseasesLayout, false)
            val imageView = itemView.findViewById<ImageView>(R.id.imageViewDisease)
            val textView = itemView.findViewById<TextView>(R.id.textViewDiseaseName)

            // Set image resource
            imageView.setImageResource(i)

            // Generate a random index to pick a disease name
            val randomDiseaseName = diseaseNames[(diseaseNames.indices).random()]
            textView.text = randomDiseaseName

            // Add the item view to the layout
            diseasesLayout.addView(itemView)
        }

        val appointmentButton: LinearLayout = findViewById(R.id.appointments_btn)
        appointmentButton.setOnClickListener {
            overlay.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE
            val intent = Intent(this, AppointmentActivity::class.java)
            intent.putExtra("TITLE", "Appointments")
            startActivity(intent)
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


        val btnFavoriteDoctors: CardView = findViewById(R.id.btn_favorite_doctors)
        btnFavoriteDoctors.setOnClickListener {
            overlay.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE

            val intent = Intent(this, FavouriteDoctorActivity::class.java)
            intent.putExtra("TITLE", "Favourites")
            startActivity(intent)
        }



        val btnOpenDoctor: CardView = findViewById(R.id.btn_open_doctor)
        btnOpenDoctor.setOnClickListener {
            overlay.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE

            val intent = Intent(this, DoctorActivity::class.java)
            intent.putExtra("TITLE", "All Doctors")
            startActivity(intent)

            progressBar.postDelayed({
                progressBar.visibility = View.GONE
                overlay.visibility = View.GONE
            }, 500)
        }

        val btnOpenAppointment: LinearLayout = findViewById(R.id.appointments_section)
        btnOpenAppointment.setOnClickListener {
            overlay.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE

            val intent = Intent(this, AppointmentActivity::class.java)
            intent.putExtra("TITLE", "Appointments")
            startActivity(intent)

            progressBar.postDelayed({
                progressBar.visibility = View.GONE
                overlay.visibility = View.GONE
            }, 500)
        }


        val btnBlogs :CardView = findViewById(R.id.btn_blogs)
        btnBlogs.setOnClickListener {

            val Intent = Intent(this, BlogsActivity::class.java)
            startActivity(Intent)

        }

    }

    private fun deleteUserAccount() {
        val user = auth.currentUser
        user?.let {
            it.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show()
                        // Optionally, redirect to login screen or exit the app
                        finish() // Close the activity
                    } else {
                        Toast.makeText(this, "Failed to delete account: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } ?: run {
            Toast.makeText(this, "No user is currently logged in", Toast.LENGTH_SHORT).show()
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

            }

            R.id.nav_appointment -> {
                //Handle the Appointments action

                startActivity(Intent(this, AppointmentActivity::class.java))
                showToast("Appointments selected")
           }
                R.id.nav_search_doctor -> {
                    // Handle the Search Doctor action
                    startActivity(Intent(this, DoctorActivity::class.java))

                    showToast("Search Doctor selected")
                }
                R.id.nav_search_hospital -> {
                    // Handle the Search Hospital action
                    startActivity(Intent(this, HospitalActivity::class.java))
                    showToast("Search Hospital selected")
                }

//                R.id.contact_us -> {
//                    // Handle the Contact Us action
//                    startActivity(Intent(this, ContactUsActivity::class.java))
//                    showToast("Contact Us selected")
//                }
            R.id.settings -> {
                // Handle the Settings action

                showToast("Account Deleted")
                deleteUserAccount()
            }


                R.id.logout_button -> {
                    // Handle the Logout action
                    auth.signOut()
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                    showToast("Log out selected")
                }
//            R.id.logout -> {
//                // Handle the Logout action
//                // You might want to add logout logic here before starting another activity
//                startActivity(Intent(this, LoginActivity::class.java))
//                showToast("Log out selected")
//            }
                else -> {
                    // Handle unknown menu item

                    drawerLayout.closeDrawer(GravityCompat.START)
                }
            }
            // Close the drawer after selection
            drawerLayout.closeDrawer(GravityCompat.START)
        }



    override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        if (user == null) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

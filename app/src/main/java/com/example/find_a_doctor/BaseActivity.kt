package com.example.find_a_doctor

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    private lateinit var titleTextView: TextView
    private lateinit var backButton: ImageButton
    private lateinit var helpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base) // Set a base layout with header included

        initializeUI()
        customizeHeader()
    }

    private fun initializeUI() {
        // Initialize header views
        titleTextView = findViewById(R.id.title)
        backButton = findViewById(R.id.btn_back)
        helpButton = findViewById(R.id.btn_help)

        // Check if views are correctly initialized
        Log.d("BaseActivity", "TitleTextView initialized: ${::titleTextView.isInitialized}")
        Log.d("BaseActivity", "BackButton initialized: ${::backButton.isInitialized}")
        Log.d("BaseActivity", "HelpButton initialized: ${::helpButton.isInitialized}")

        // Set up click listeners
        backButton.setOnClickListener {
            Log.d("BaseActivity", "Back button clicked")
            finish()
        }

        helpButton.setOnClickListener {
            Log.d("BaseActivity", "Help button clicked")
            showHelplineDialog()
        }
    }

    abstract fun customizeHeader()

    fun setHeaderTitle(title: String) {
        Log.d("BaseActivity", "Setting header title: $title")
        titleTextView.text = title
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

    private fun makePhoneCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
        startActivity(intent)
    }
}

package com.example.find_a_doctor;
import android.content.Intent
import android.os.Bundle
import com.example.find_a_doctor.R
import android.widget.Button

import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        findViewById<Button>(R.id.loginButton).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        findViewById<Button>(R.id.signUpButton).setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}

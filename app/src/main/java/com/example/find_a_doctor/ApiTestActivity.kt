package com.example.find_a_doctor

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ApiTestActivity : Activity() {

    private lateinit var tvHospitals: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_test)

        tvHospitals = findViewById(R.id.tvHospitals)

        GlobalScope.launch {
            try {
                val hospitals = RetrofitInstance.api.getHospitals()
                val hospitalsText = hospitals.joinToString(separator = "\n\n") {
                    "Name: ${it.name}\nLocation: ${it.location}\n"
                }
                runOnUiThread {


                    tvHospitals.text = hospitalsText
                }
            } catch (e: Exception)
            {
                e.printStackTrace()
                runOnUiThread {
                    tvHospitals.text = "Error fetching data: ${e.message}"
                }
            }
        }
    }
}

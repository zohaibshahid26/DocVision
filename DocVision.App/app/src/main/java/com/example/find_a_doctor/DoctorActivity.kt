package com.example.find_a_doctor

import DoctorAdapter
import DoctorDTO
import android.content.Intent
import android.net.Uri
import android.net.http.HttpException
import android.os.Build
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.find_a_doctor.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DoctorActivity : BaseActivity() {

    private lateinit var doctorRecyclerView: RecyclerView
    private lateinit var doctorAdapter: DoctorAdapter
    private var originalDoctorList: List<DoctorDTO> = listOf() // Updated to use DTO

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Find the content frame and inflate the specific layout
        val contentFrame: FrameLayout = findViewById(R.id.content_frame)
        layoutInflater.inflate(R.layout.activity_doctor, contentFrame, true)

        // Set up the header title
        val title = intent.getStringExtra("TITLE") ?: "Doctors"
        setHeaderTitle(title)

        // Initialize RecyclerView
        doctorRecyclerView = findViewById(R.id.doctor_list)
        doctorRecyclerView.layoutManager = LinearLayoutManager(this)
        doctorAdapter = DoctorAdapter(this, originalDoctorList, FavoriteDoctorDatabaseHelper(this))
        doctorRecyclerView.adapter = doctorAdapter

        // Set up the SearchView
        setupSearchView()

        // Fetch and populate data
        fetchDoctors()
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun fetchDoctors() {
        showLoading()  // Show progress bar while loading data
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Fetch the list of doctors from the API
                val doctors = RetrofitInstance.api.getDoctors()

                // Populate the UI with fetched data
                withContext(Dispatchers.Main) {
                    originalDoctorList = doctors
                    doctorAdapter.updateData(originalDoctorList)
                    hideLoading()  // Hide progress bar when done
                }
            } catch (e: HttpException) {
                // Handle HTTP exceptions
                withContext(Dispatchers.Main) {
                    showError("Network error: ${e.message}")
                    //hideLoading()  // Hide progress bar in case of error
                }
            } catch (e: Exception) {
                // Handle other exceptions
                withContext(Dispatchers.Main) {
                    showError("An error occurred: ${e.message}")
                    //hideLoading()  // Hide progress bar in case of error
                }
            }
        }
    }

    private fun setupSearchView() {
        val searchView: SearchView = findViewById(R.id.search_doctor)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterDoctors(newText)
                return true
            }
        })
    }

    private fun filterDoctors(query: String?) {
        val filteredList = originalDoctorList.filter { doctor ->
            doctor.name.contains(query ?: "", ignoreCase = true) ||
                    doctor.specialization.contains(query ?: "", ignoreCase = true) ||
                    doctor.qualification.contains(query ?: "", ignoreCase = true)
        }

        // Update the adapter with the filtered list
        doctorAdapter.updateData(filteredList)

        // Show or hide the RecyclerView based on the search results
        doctorRecyclerView.visibility = if (filteredList.isNotEmpty()) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        // Handle error display (e.g., show a Toast or Snackbar)
        Toast.makeText(this, "Error Fetching Data", Toast.LENGTH_SHORT).show()
    }

    override fun customizeHeader() {
        // Customize header for DoctorActivity if needed
    }
}

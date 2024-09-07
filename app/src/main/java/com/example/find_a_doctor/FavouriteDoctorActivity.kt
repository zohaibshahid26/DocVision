package com.example.find_a_doctor

import DoctorAdapter
import DoctorDTO
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.find_a_doctor.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouriteDoctorActivity : BaseActivity() {

    private lateinit var doctorRecyclerView: RecyclerView
    private lateinit var doctorAdapter: DoctorAdapter
    private lateinit var progressBar: ProgressBar
    private var originaldoctorList: List<DoctorDTO> = listOf() // Updated to use DTO
    private lateinit var dbHelper: FavoriteDoctorDatabaseHelper

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Find the content frame and inflate the specific layout
        val contentFrame: FrameLayout = findViewById(R.id.content_frame)
        layoutInflater.inflate(R.layout.activity_favourite_doctor, contentFrame, true)

        // Set up the header title
        val title = intent.getStringExtra("TITLE") ?: "Default Title"
        setHeaderTitle(title)

        // Initialize database helper
        dbHelper = FavoriteDoctorDatabaseHelper(this)

        // Reference to the ProgressBar
        progressBar = findViewById(R.id.progress_bar)
        Log.d("doctorActivity", "Progress Bar: $progressBar")

        // Initialize RecyclerView
        doctorRecyclerView = findViewById(R.id.doctor_list)
        doctorRecyclerView.layoutManager = LinearLayoutManager(this)
        doctorAdapter = DoctorAdapter(this, originaldoctorList, dbHelper)
        doctorRecyclerView.adapter = doctorAdapter

        // Set up the SearchView
        setupSearchView()

        // Fetch and populate data
        fetchDoctorsFromLocalDb()
    }

    private fun fetchDoctorsFromLocalDb() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Show progress bar while loading data
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.VISIBLE
                }

                val favoriteDoctors = dbHelper.getAllFavoriteDoctors()

                // Populate the UI with fetched data
                withContext(Dispatchers.Main) {
                    originaldoctorList = favoriteDoctors
                    doctorAdapter.updateData(originaldoctorList)
                    progressBar.visibility = View.GONE // Hide progress bar when done
                }
            } catch (e: Exception) {
                // Handle exception
                showError("An error occurred: ${e.message}")
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
        val filteredList = originaldoctorList.filter { doctor ->
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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        // Refresh the doctor list when activity resumes
        fetchDoctorsFromLocalDb()
    }

    override fun customizeHeader() {
        // Customize header for doctorActivity if needed
    }
}


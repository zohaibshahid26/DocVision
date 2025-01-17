package com.example.find_a_doctor

import HospitalAdapter
import HospitalDTO
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.find_a_doctor.R
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HospitalActivity : BaseActivity() {

    private lateinit var hospitalRecyclerView: RecyclerView
    private lateinit var hospitalAdapter: HospitalAdapter
    private lateinit var horizontalList: LinearLayout
    private lateinit var horizontalScrollView: HorizontalScrollView
    private var originalHospitalList: List<HospitalDTO> = listOf() // Updated to use DTO

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Find the content frame and inflate the specific layout
        val contentFrame: FrameLayout = findViewById(R.id.content_frame)
        layoutInflater.inflate(R.layout.activity_hospital, contentFrame, true)

        // Set up the header title
        val title = intent.getStringExtra("TITLE") ?: "Hospitals"
        setHeaderTitle(title)

        // Reference to the HorizontalScrollView and LinearLayout inside it
        horizontalScrollView = findViewById(R.id.top_hospitals)
        horizontalList = findViewById(R.id.top_hospitals_horizontal_list)
        Log.d("HospitalActivity", "Horizontal List: $horizontalList")

        // Initialize RecyclerView
        hospitalRecyclerView = findViewById(R.id.hospital_list)
        hospitalRecyclerView.layoutManager = LinearLayoutManager(this)
        hospitalAdapter = HospitalAdapter(this, originalHospitalList)
        hospitalRecyclerView.adapter = hospitalAdapter

        // Set up the SearchView
        setupSearchView()

        // Fetch and populate data
        fetchHospitals()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun fetchHospitals() {
        showLoading() // Show loading indicator while fetching data
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Fetch the list of hospitals from the API
                val hospitals = RetrofitInstance.api.getHospitals()

                // Sort hospitals by number of available doctors in descending order
                val sortedHospitals = hospitals.sortedByDescending { it.numberOfAvailableDoctors }

                // Populate the UI with fetched data
                withContext(Dispatchers.Main) {
                    populateTopHospitals(sortedHospitals)
                    originalHospitalList = hospitals
                    hospitalAdapter.updateData(originalHospitalList)
                    hideLoading() // Hide loading indicator when done
                }
            } catch (e: HttpException) {
                // Handle HTTP exceptions
                withContext(Dispatchers.Main) {
                    showError("Network error: ${e.message}")
                    //hideLoading() // Hide loading indicator in case of an error
                }
            } catch (e: Exception) {
                // Handle other exceptions
                withContext(Dispatchers.Main) {
                    showError("An error occurred: ${e.message}")
                    //hideLoading() // Hide loading indicator in case of an error
                }
            }
        }
    }

    private fun populateTopHospitals(topHospitals: List<HospitalDTO>) {
        horizontalList.removeAllViews() // Clear existing views
        for (hospital in topHospitals.take(5)) { // Display only top 5 hospitals
            val itemView = layoutInflater.inflate(R.layout.list_item_hospital_card, horizontalList, false)
            val imageView = itemView.findViewById<ImageView>(R.id.item_image)
            val nameTextView = itemView.findViewById<TextView>(R.id.item_name)
            val doctorsTextView = itemView.findViewById<TextView>(R.id.item_doctors)
            val locationTextView = itemView.findViewById<TextView>(R.id.item_location)

            // Load the hospital image using Glide
            Glide.with(this)
                .load(hospital.img) // URL or path to the image
                .placeholder(R.drawable.hospital) // Placeholder image
                .error(R.drawable.hospital) // Error image
                .into(imageView)

            nameTextView.text = hospital.name
            doctorsTextView.text = "Doctors: ${hospital.numberOfAvailableDoctors}"
            locationTextView.text = hospital.location

            // Set click listener for the hospital card
            itemView.setOnClickListener {
                // Handle click events for the individual hospital item
                val intent = Intent(this, HospitalProfileActivity::class.java)
                intent.putExtra("hospitalId", hospital.id) // Pass the hospital's ID
                startActivity(intent)
            }

            horizontalList.addView(itemView)
        }
    }

    private fun setupSearchView() {
        val searchView: SearchView = findViewById(R.id.search_hospital)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterHospitals(newText)
                return true
            }
        })
    }

    private fun filterHospitals(query: String?) {
        val filteredList = originalHospitalList.filter { hospital ->
            hospital.name.contains(query ?: "", ignoreCase = true) ||
                    hospital.location.contains(query ?: "", ignoreCase = true)
        }

        // Update the adapter with the filtered list
        hospitalAdapter.updateData(filteredList)

        // Show or hide the RecyclerView based on the search results
        hospitalRecyclerView.visibility = if (filteredList.isNotEmpty()) View.VISIBLE else View.GONE

        // Manage visibility of the "Top Hospitals" section
        val isQueryEmpty = query.isNullOrEmpty()
        findViewById<TextView>(R.id.top_hospitals_text).visibility = if (isQueryEmpty) View.VISIBLE else View.GONE
        findViewById<HorizontalScrollView>(R.id.top_hospitals).visibility = if (isQueryEmpty) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        // Handle error display (e.g., show a Toast or Snackbar)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun customizeHeader() {
        // Customize header for HospitalActivity if needed
    }
}

package com.example.find_a_doctor
import Hospital
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.find_a_doctor.R

class HospitalActivity : BaseActivity() {

    private lateinit var hospitalRecyclerView: RecyclerView
    private lateinit var hospitalAdapter: HospitalAdapter
    private lateinit var horizontalList: LinearLayout
    private lateinit var originalHospitalList: List<Hospital> // Store the original list of hospitals

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Find the content frame and inflate the specific layout
        val contentFrame: FrameLayout = findViewById(R.id.content_frame)
        layoutInflater.inflate(R.layout.activity_hospital, contentFrame, true)

        // Set up the header title
        val title = intent.getStringExtra("TITLE") ?: "Default Title"
        setHeaderTitle(title)


        // Reference to the LinearLayout inside HorizontalScrollView
        horizontalList = findViewById(R.id.top_hospitals_horizontal_list)

        // Sample data for top hospitals
        val topHospitals = listOf(
            Hospital(1,"Hospital A", availableDoctors = 10, location = "Location A", openingHours = "0"),
            Hospital(2,"Hospital B", availableDoctors = 8, location = "Location B", openingHours = "0"),
            Hospital(3,"Hospital C", availableDoctors = 12, location = "Location C", openingHours = "0"),
            Hospital(4,"Hospital D", availableDoctors = 5, location = "Location D", openingHours = "0"),
            Hospital(5,"Hospital E", availableDoctors = 15, location = "Location E", openingHours = "0")
        )

        // Populate the horizontal list
        populateTopHospitals(topHospitals)

        // Initialize RecyclerView
        hospitalRecyclerView = findViewById(R.id.hospital_list)
        hospitalRecyclerView.layoutManager = LinearLayoutManager(this)

        // Sample data for hospitals
        originalHospitalList = generateSampleHospitals(30)

        // Initialize the adapter with the original list
        hospitalAdapter = HospitalAdapter(this, originalHospitalList)
        hospitalRecyclerView.adapter = hospitalAdapter

        // Set up the SearchView
        setupSearchView()
    }

    private fun populateTopHospitals(topHospitals: List<Hospital>) {
        for (hospital in topHospitals) {
            val itemView = layoutInflater.inflate(R.layout.list_item_hospital_card, horizontalList, false)
            val imageView = itemView.findViewById<ImageView>(R.id.item_image)
            val nameTextView = itemView.findViewById<TextView>(R.id.item_name)
            val doctorsTextView = itemView.findViewById<TextView>(R.id.item_doctors)
            val locationTextView = itemView.findViewById<TextView>(R.id.item_location)

            imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.hospital))
            nameTextView.text = hospital.name
            doctorsTextView.text = "Doctors: ${hospital.availableDoctors}"
            locationTextView.text = hospital.location

            horizontalList.addView(itemView)
        }
    }

    private fun generateSampleHospitals(count: Int): List<Hospital> {
        return List(count) { index ->
            Hospital(
                id = index + 1,
                name = "Hospital ${index + 1}",
                location = "Location ${index + 1}",
                openingHours = if (index % 2 == 0) "24 Hours" else "9 AM - 5 PM",
                availableDoctors = (1..10).random()
            )
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

    override fun customizeHeader() {
        // Customize header for HospitalActivity if needed
    }
}
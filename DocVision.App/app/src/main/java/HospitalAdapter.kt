import android.content.Context
import android.content.Intent
import android.net.Uri
import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.find_a_doctor.R
import HospitalDTO
import android.widget.Toast
import com.example.find_a_doctor.HospitalProfileActivity

class HospitalAdapter(private val context: Context, private var hospitals: List<HospitalDTO>) : RecyclerView.Adapter<HospitalAdapter.HospitalViewHolder>() {

    // ViewHolder class to hold the views for each item
    class HospitalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val hospitalIdTextView: TextView = view.findViewById(R.id.hospital_id)
        val hospitalNameTextView: TextView = view.findViewById(R.id.hospital_name)
        val hospitalLocationTextView: TextView = view.findViewById(R.id.hospital_address)
        val openingHoursTextView: TextView = view.findViewById(R.id.opening_hours_time)
        val availableDoctorsTextView: TextView = view.findViewById(R.id.available_doctors_count)
        val callButton: Button = view.findViewById(R.id.btn_call_helpline) // Reference to the Call Helpline button
        val viewProfileButton: Button = view.findViewById(R.id.btn_view_profile) // Reference to the View Profile button
        val imageView: ImageView = view.findViewById(R.id.hospital_icon) // ImageView for the hospital image
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HospitalViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_hospital, parent, false)
        return HospitalViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: HospitalViewHolder, position: Int) {
        val hospital = hospitals[position]
        holder.hospitalIdTextView.text = hospital.id.toString()
        holder.hospitalNameTextView.text = hospital.name
        holder.hospitalLocationTextView.text = hospital.location
        holder.openingHoursTextView.text = hospital.openingHours
        holder.availableDoctorsTextView.text = hospital.numberOfAvailableDoctors.toString()

        // Load the hospital image using Glide
        Glide.with(context)
            .load(hospital.img) // URL or path to the image
            .placeholder(R.drawable.hospital) // Placeholder image
            .error(R.drawable.hospital) // Error image
            .into(holder.imageView)

        // Set up the View Profile button click listener
        holder.viewProfileButton.setOnClickListener {
            // Start the Profile Activity and pass the hospital's ID or other necessary data
            val intent = Intent(context, HospitalProfileActivity::class.java)
            intent.putExtra("hospitalId", hospital.id) // Assuming HospitalDTO has an id field
            context.startActivity(intent)
        }

        // Set up the Call Helpline button click listener
        holder.callButton.setOnClickListener {
            val phoneNumber = hospital.contactNo
            if (phoneNumber != null && PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$phoneNumber")
                context.startActivity(intent)
            } else {
                // Show error message if phone number is invalid
                // You can use a Toast, Snackbar, or some other UI element
                // Example:
                Toast.makeText(context, "Invalid phone number", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Return the size of the dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return hospitals.size
    }

    fun updateData(newHospitals: List<HospitalDTO>) {
        hospitals = newHospitals
        notifyDataSetChanged() // Notify the adapter to refresh the list
    }
}

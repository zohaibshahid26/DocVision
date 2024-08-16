import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.find_a_doctor.R

class HospitalAdapter(private val context: Context, private val hospitals: List<Hospital>) : RecyclerView.Adapter<HospitalAdapter.HospitalViewHolder>() {

    // ViewHolder class to hold the views for each item
    class HospitalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val hospitalNameTextView: TextView = view.findViewById(R.id.hospital_name)
        val hospitalLocationTextView: TextView = view.findViewById(R.id.hospital_address)
        val openingHoursTextView: TextView = view.findViewById(R.id.opening_hours_time)
        val availableDoctorsTextView: TextView = view.findViewById(R.id.available_doctors_count)

        // Optionally, you can define buttons here if needed
        // val callHelplineButton: Button = view.findViewById(R.id.btn_call_helpline)
        // val viewProfileButton: Button = view.findViewById(R.id.btn_view_profile)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HospitalViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_hospital, parent, false)
        return HospitalViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: HospitalViewHolder, position: Int) {
        val hospital = hospitals[position]
        holder.hospitalNameTextView.text = hospital.name
        holder.hospitalLocationTextView.text = hospital.location
        holder.openingHoursTextView.text = hospital.openingHours
        holder.availableDoctorsTextView.text = hospital.availableDoctors.toString()

        // Optionally set up buttons with click listeners here
        // holder.callHelplineButton.setOnClickListener { /* Handle click */ }
        // holder.viewProfileButton.setOnClickListener { /* Handle click */ }
    }

    // Return the size of the dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return hospitals.size
    }
}
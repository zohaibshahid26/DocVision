import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.find_a_doctor.AppointmentActivity
import com.example.find_a_doctor.DoctorProfileActivity
import com.example.find_a_doctor.FavoriteDoctorDatabaseHelper
import com.example.find_a_doctor.R
import com.example.find_a_doctor.RetrofitInstance
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DoctorAdapter(
    private val context: Context,
    private var doctors: List<DoctorDTO>,
    private val dbHelper: FavoriteDoctorDatabaseHelper
) : RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {

    class DoctorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val doctorImageView: ImageView = view.findViewById(R.id.doctor_icon)
        val doctorNameTextView: TextView = view.findViewById(R.id.doctor_name)
        val doctorSpecializationTextView: TextView = view.findViewById(R.id.doctor_specialization)
        val doctorQualificationTextView: TextView = view.findViewById(R.id.doctor_qualification)
        val doctorExperienceTextView: TextView = view.findViewById(R.id.doctor_experience)
        val btnFavorite: ImageButton = view.findViewById(R.id.btn_favorite)
        val viewProfileButton: Button = view.findViewById(R.id.btn_view_profile)
        val bookAppointmentButton: Button = view.findViewById(R.id.btn_book_appointment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_doctor, parent, false)
        return DoctorViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val doctor = doctors[position]

        Glide.with(context)
            .load(doctor.doctorImage)
            .placeholder(R.drawable.doctor)
            .into(holder.doctorImageView)

        holder.doctorNameTextView.text = doctor.name
        holder.doctorSpecializationTextView.text = doctor.specialization
        holder.doctorQualificationTextView.text = doctor.qualification
        holder.doctorExperienceTextView.text = "${doctor.experience} years"

        holder.btnFavorite.setImageResource(
            if (doctor.id in getFavoriteDoctorIds()) R.drawable.star
            else R.drawable.favourite
        )

        holder.btnFavorite.setOnClickListener {
            if (doctor.id in getFavoriteDoctorIds()) {
                dbHelper.removeFavoriteDoctor(doctor.id)
                // Update the list immediately
                doctors = doctors.filter { it.id != doctor.id }
                notifyDataSetChanged()
            } else {
                dbHelper.addFavoriteDoctor(doctor)
                // Update the list immediately
                doctors = doctors + doctor
                notifyDataSetChanged()
            }
        }

        holder.viewProfileButton.setOnClickListener {
            val intent = Intent(context, DoctorProfileActivity::class.java)
            intent.putExtra("doctorId", doctor.id)
            intent.putExtra("TITLE", "Doctor Profile")
            context.startActivity(intent)
        }

        if (!doctor.isAvailable) {
            holder.bookAppointmentButton.isClickable = false
            holder.bookAppointmentButton.isEnabled = false
            holder.bookAppointmentButton.alpha = 0.5f
            holder.bookAppointmentButton.text = "Not Available"
        } else {
            holder.bookAppointmentButton.setOnClickListener {
                bookAppointment(context, doctor.id, doctor.appointmentTime)
                val intent = Intent(context, AppointmentActivity::class.java)
                intent.putExtra("TITLE", "Appointments")
                context.startActivity(intent)
            }
        }
    }

    private fun getFavoriteDoctorIds(): Set<Int> {
        return dbHelper.getAllFavoriteDoctors().map { it.id }.toSet()
    }

    private fun bookAppointment(context: Context, doctorId: Int, availabilityTime: String) {
        val appointmentTime = availabilityTime
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val bookAppointmentDTO = BookAppointmentDTO(appointmentTime, userId, doctorId)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitInstance.api.bookAppointment(bookAppointmentDTO)
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            Toast.makeText(context, "Appointment booked successfully", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, "Failed to book appointment: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Network error: Failed to connect ${e.message} ", Toast.LENGTH_LONG).show()
                    }
                }
            }
        } else {
            Toast.makeText(context, "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return doctors.size
    }

    fun updateData(newDoctors: List<DoctorDTO>) {
        doctors = newDoctors
        notifyDataSetChanged()
    }
}

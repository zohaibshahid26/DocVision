namespace FindADoctorApi.Model
{
    public class Appointment
    {
        public int Id { get; set; }
        public DateTime AppointmentTime { get; set; }
        public string PatientId { get; set; } // Assumes patient identifier is a string; adjust type if necessary
        public bool Status { get; set; } // Represents if the appointment is confirmed or not

        // Navigation property
        public int DoctorId { get; set; } // Foreign key for Doctor
        public Doctor Doctor { get; set; } // Navigation property to Doctor
    }
}

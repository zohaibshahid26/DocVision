namespace FindADoctorApi.Model
{
    public class Doctor
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string DoctorImage { get; set; } // URL or path to the doctor's image
        public string Specialization { get; set; }
        public string Qualification { get; set; }
        public int Experience { get; set; } // Number of years of experience
        public string About { get; set; } // Information about the doctor

        public string Email { get; set; }

        public DateTime AppointmentTime { get; set; } // Time of the appointment

        public bool IsAvailable { get; set; } // Availability of the doctor

        public Hospital Hospital { get; set; }

        public ICollection<Appointment> Appointments { get; set; }
    }
}

namespace FindADoctorApi.DTO
{
    public class DoctorDTO
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string DoctorImage { get; set; }
        public string Specialization { get; set; }
        public string Qualification { get; set; }
        public int Experience { get; set; }
        public string About { get; set; }
        public string Email { get; set; }
        public bool IsAvailable { get; set; }
        public DateTime AppointmentTime { get; set; } // Time of the appointment
        public HospitalDTO Hospital { get; set; }
    }
}

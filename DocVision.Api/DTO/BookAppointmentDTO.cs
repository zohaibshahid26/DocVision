namespace FindADoctorApi.DTO
{
    public class BookAppointmentDto
    {
        public DateTime AppointmentTime { get; set; }
        public string PatientId { get; set; } // Assuming patient is identified by an ID
        public int DoctorId { get; set; }
    }
}

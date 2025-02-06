namespace FindADoctorApi.DTO
{
    public class AppointmentDto
    {
        public int Id { get; set; }
        public DateTime AppointmentTime { get; set; }
        public string PatientId { get; set; }
        public string DoctorName { get; set; }
        public bool Status { get; set; } // Indicates if the appointment is completed

        public int DoctorId { get; set; }
    }
}

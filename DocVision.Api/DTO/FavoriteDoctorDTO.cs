namespace FindADoctorApi.DTO
{
    public class FavoriteDoctorDTO
    {
        public int Id { get; set; }
        public string PatientId { get; set; }
        public DoctorDTO Doctor { get; set; }
    }
}
namespace FindADoctorApi.Model
{
    public class FavoriteDoctor
    {
        public int Id { get; set; }
        public string PatientId { get; set; } 
        public Doctor Doctor { get; set; }
    }
}
namespace FindADoctorApi.DTO
{
    public class HospitalDTO
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Location { get; set; }
        public string LocationUrl { get; set; }
        public string OpeningHours { get; set; }
        public int NumberOfAvailableDoctors { get; set; }
        public double Rating { get; set; }
        public string Img { get; set; }
        public string About { get; set; }
        public string ContactNo { get; set; }
    }
}
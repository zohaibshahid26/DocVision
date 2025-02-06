using FindADoctorApi.Model;
using Microsoft.EntityFrameworkCore;
using System;
namespace FindADoctorApi.Data
{
    public class ApplicationDBContext : DbContext
    {
        public ApplicationDBContext(DbContextOptions<ApplicationDBContext> options) : base(options)
        {
        }

        public DbSet<Hospital> Hospitals { get; set; }
        public DbSet<Doctor> Doctors { get; set; }

        public DbSet<Appointment> Appointments { get; set; }

        public DbSet<FavoriteDoctor> FavoriteDoctors { get;set; }


    }
}

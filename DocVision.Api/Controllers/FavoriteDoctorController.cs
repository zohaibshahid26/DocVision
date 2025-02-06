using FindADoctorApi.Data;
using FindADoctorApi.DTO;
using FindADoctorApi.Model;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace FindADoctorApi.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class FavoriteDoctorController : ControllerBase
    {
        private readonly ApplicationDBContext _context;

        public FavoriteDoctorController(ApplicationDBContext context)
        {
            _context = context;
        }

        [HttpPost]
        public async Task<ActionResult<FavoriteDoctorDTO>> AddFavoriteDoctor([FromBody] FavoriteDoctorDTO favoriteDoctorDTO)
        {
            var doctor = await _context.Doctors.FindAsync(favoriteDoctorDTO.Doctor.Id);
            if (doctor == null)
            {
                return NotFound("Doctor not found");
            }

            var favoriteDoctor = new FavoriteDoctor
            {
                PatientId = favoriteDoctorDTO.PatientId,
                Doctor = doctor
            };

            _context.FavoriteDoctors.Add(favoriteDoctor);
            await _context.SaveChangesAsync();

            favoriteDoctorDTO.Id = favoriteDoctor.Id;

            return CreatedAtAction(nameof(GetFavoriteDoctors), new { patientId = favoriteDoctorDTO.PatientId }, favoriteDoctorDTO);
        }

        [HttpGet("{patientId}")]
        public async Task<ActionResult<IEnumerable<FavoriteDoctorDTO>>> GetFavoriteDoctors(string patientId)
        {
            var favoriteDoctors = await _context.FavoriteDoctors
                .Where(fd => fd.PatientId == patientId)
                .Include(fd => fd.Doctor)
                .ThenInclude(d => d.Hospital)
                .ToListAsync();

            var favoriteDoctorDTOs = favoriteDoctors.Select(fd => new FavoriteDoctorDTO
            {
                Id = fd.Id,
                PatientId = fd.PatientId,
                Doctor = new DoctorDTO
                {
                    Id = fd.Doctor.Id,
                    Name = fd.Doctor.Name,
                    DoctorImage = fd.Doctor.DoctorImage,
                    Specialization = fd.Doctor.Specialization,
                    Qualification = fd.Doctor.Qualification,
                    Experience = fd.Doctor.Experience,
                    About = fd.Doctor.About,
                    Email = fd.Doctor.Email,
                    IsAvailable = fd.Doctor.IsAvailable,
                    AppointmentTime = fd.Doctor.AppointmentTime,
                    Hospital = new HospitalDTO
                    {
                        Id = fd.Doctor.Hospital.Id,
                        Name = fd.Doctor.Hospital.Name,
                        Location = fd.Doctor.Hospital.Location,
                        LocationUrl = fd.Doctor.Hospital.LocationUrl,
                        OpeningHours = fd.Doctor.Hospital.OpeningHours,
                        NumberOfAvailableDoctors = fd.Doctor.Hospital.NumberOfAvailableDoctors,
                        Rating = fd.Doctor.Hospital.Rating,
                        Img = fd.Doctor.Hospital.Img,
                        About = fd.Doctor.Hospital.About,
                        ContactNo = fd.Doctor.Hospital.ContactNo
                    }
                }
            }).ToList();

            return Ok(favoriteDoctorDTOs);
        }
    }
}
using Microsoft.AspNetCore.Mvc;
using FindADoctorApi.Data;
using FindADoctorApi.DTO;
using FindADoctorApi.Model;
using Microsoft.EntityFrameworkCore;

namespace FindADoctorApi.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class DoctorController : ControllerBase
    {
        private readonly ApplicationDBContext _context;

        public DoctorController(ApplicationDBContext context)
        {
            _context = context;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<DoctorDTO>>> GetDoctors()
        {
            var doctors = await _context.Doctors.Include(d => d.Hospital).ToListAsync();
            var doctorDTOs = doctors.Select(d => new DoctorDTO
            {
                Id = d.Id,
                Name = d.Name,
                DoctorImage = d.DoctorImage,
                Specialization = d.Specialization,
                Qualification = d.Qualification,
                Experience = d.Experience,
                About = d.About,
                Email = d.Email,
                IsAvailable = d.IsAvailable,
                AppointmentTime = d.AppointmentTime,
                Hospital = new HospitalDTO
                {
                    Id = d.Hospital.Id,
                    Name = d.Hospital.Name,
                    Location = d.Hospital.Location,
                    LocationUrl = d.Hospital.LocationUrl,
                    OpeningHours = d.Hospital.OpeningHours,
                    NumberOfAvailableDoctors = d.Hospital.NumberOfAvailableDoctors,
                    Rating = d.Hospital.Rating,
                    Img = d.Hospital.Img,
                    About = d.Hospital.About,
                    ContactNo = d.Hospital.ContactNo
                }
            }).ToList();

            return Ok(doctorDTOs);
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<DoctorDTO>> GetDoctor(int id)
        {
            var doctor = await _context.Doctors.Include(d => d.Hospital).FirstOrDefaultAsync(d => d.Id == id);
            if (doctor == null)
            {
                return NotFound();
            }

            var doctorDTO = new DoctorDTO
            {
                Id = doctor.Id,
                Name = doctor.Name,
                DoctorImage = doctor.DoctorImage,
                Specialization = doctor.Specialization,
                Qualification = doctor.Qualification,
                Experience = doctor.Experience,
                About = doctor.About,
                Email = doctor.Email,
                IsAvailable = doctor.IsAvailable,
                AppointmentTime = doctor.AppointmentTime,
                Hospital = new HospitalDTO
                {
                    Id = doctor.Hospital.Id,
                    Name = doctor.Hospital.Name,
                    Location = doctor.Hospital.Location,
                    LocationUrl = doctor.Hospital.LocationUrl,
                    OpeningHours = doctor.Hospital.OpeningHours,
                    NumberOfAvailableDoctors = doctor.Hospital.NumberOfAvailableDoctors,
                    Rating = doctor.Hospital.Rating,
                    Img = doctor.Hospital.Img,
                    About = doctor.Hospital.About,
                    ContactNo = doctor.Hospital.ContactNo
                }
            };

            return Ok(doctorDTO);
        }

        [HttpPost]
        public async Task<ActionResult<DoctorDTO>> CreateDoctor([FromBody] DoctorDTO doctorDTO)
        {
            var hospital = await _context.Hospitals.FindAsync(doctorDTO.Hospital.Id);
            if (hospital == null)
            {
                return BadRequest("Invalid HospitalId");
            }

            var doctor = new Doctor
            {
                Name = doctorDTO.Name,
                DoctorImage = doctorDTO.DoctorImage,
                Specialization = doctorDTO.Specialization,
                Qualification = doctorDTO.Qualification,
                Experience = doctorDTO.Experience,
                About = doctorDTO.About,
                Email = doctorDTO.Email,
                IsAvailable = doctorDTO.IsAvailable,
                AppointmentTime = doctorDTO.AppointmentTime,
                Hospital = hospital
            };

            _context.Doctors.Add(doctor);
            await _context.SaveChangesAsync();

            doctorDTO.Id = doctor.Id;

            return CreatedAtAction(nameof(GetDoctor), new { id = doctor.Id }, doctorDTO);
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateDoctor(int id, [FromBody] DoctorDTO doctorDTO)
        {
            if (id != doctorDTO.Id)
            {
                return BadRequest();
            }

            var doctor = await _context.Doctors.Include(d => d.Hospital).FirstOrDefaultAsync(d => d.Id == id);
            if (doctor == null)
            {
                return NotFound();
            }

            doctor.Name = doctorDTO.Name;
            doctor.DoctorImage = doctorDTO.DoctorImage;
            doctor.Specialization = doctorDTO.Specialization;
            doctor.Qualification = doctorDTO.Qualification;
            doctor.Experience = doctorDTO.Experience;
            doctor.About = doctorDTO.About;
            doctor.Email = doctorDTO.Email;
            doctor.IsAvailable = doctorDTO.IsAvailable;
            doctor.AppointmentTime = doctorDTO.AppointmentTime;
            doctor.Hospital = await _context.Hospitals.FindAsync(doctorDTO.Hospital.Id) ?? doctor.Hospital;

            _context.Entry(doctor).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!DoctorExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return NoContent();
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteDoctor(int id)
        {
            var doctor = await _context.Doctors.FindAsync(id);
            if (doctor == null)
            {
                return NotFound();
            }

            _context.Doctors.Remove(doctor);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        private bool DoctorExists(int id)
        {
            return _context.Doctors.Any(e => e.Id == id);
        }
    }
}

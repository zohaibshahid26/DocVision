using FindADoctorApi.Data;
using FindADoctorApi.DTO;
using FindADoctorApi.Model;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace FindADoctorApi.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class HospitalController : ControllerBase
    {
        private readonly ApplicationDBContext _context;

        public HospitalController(ApplicationDBContext context)
        {
            _context = context;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<HospitalDTO>>> GetHospitals()
        {
            var hospitals = await _context.Hospitals.Include(h => h.Doctors).ToListAsync();
            var hospitalDTOs = hospitals.Select(h => new HospitalDTO
            {
                Id = h.Id,
                Name = h.Name,
                Location = h.Location,
                LocationUrl = h.LocationUrl,
                OpeningHours = h.OpeningHours,
                NumberOfAvailableDoctors = h.NumberOfAvailableDoctors,
                Rating = h.Rating,
                Img = h.Img,
                About = h.About,
                ContactNo = h.ContactNo
            }).ToList();

            return Ok(hospitalDTOs);
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<HospitalDTO>> GetHospital(int id)
        {
            var hospital = await _context.Hospitals.Include(h => h.Doctors).FirstOrDefaultAsync(h => h.Id == id);

            if (hospital == null)
            {
                return NotFound();
            }

            var hospitalDTO = new HospitalDTO
            {
                Id = hospital.Id,
                Name = hospital.Name,
                Location = hospital.Location,
                LocationUrl = hospital.LocationUrl,
                OpeningHours = hospital.OpeningHours,
                NumberOfAvailableDoctors = hospital.NumberOfAvailableDoctors,
                Rating = hospital.Rating,
                Img = hospital.Img,
                About = hospital.About,
                ContactNo = hospital.ContactNo
            };

            return Ok(hospitalDTO);
        }

        [HttpPost]
        public async Task<ActionResult<HospitalDTO>> CreateHospital([FromBody] HospitalDTO hospitalDTO)
        {
            var hospital = new Hospital
            {
                Name = hospitalDTO.Name,
                Location = hospitalDTO.Location,
                LocationUrl = hospitalDTO.LocationUrl,
                OpeningHours = hospitalDTO.OpeningHours,
                NumberOfAvailableDoctors = hospitalDTO.NumberOfAvailableDoctors,
                Rating = hospitalDTO.Rating,
                Img = hospitalDTO.Img,
                About = hospitalDTO.About,
                ContactNo = hospitalDTO.ContactNo
            };

            _context.Hospitals.Add(hospital);
            await _context.SaveChangesAsync();

            hospitalDTO.Id = hospital.Id;

            return CreatedAtAction(nameof(GetHospital), new { id = hospital.Id }, hospitalDTO);
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateHospital(int id, [FromBody] HospitalDTO hospitalDTO)
        {
            if (id != hospitalDTO.Id)
            {
                return BadRequest();
            }

            var hospital = await _context.Hospitals.FindAsync(id);
            if (hospital == null)
            {
                return NotFound();
            }

            hospital.Name = hospitalDTO.Name;
            hospital.Location = hospitalDTO.Location;
            hospital.LocationUrl = hospitalDTO.LocationUrl;
            hospital.OpeningHours = hospitalDTO.OpeningHours;
            hospital.NumberOfAvailableDoctors = hospitalDTO.NumberOfAvailableDoctors;
            hospital.Rating = hospitalDTO.Rating;
            hospital.Img = hospitalDTO.Img;
            hospital.About = hospitalDTO.About;
            hospital.ContactNo = hospitalDTO.ContactNo;

            _context.Entry(hospital).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!HospitalExists(id))
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
        public async Task<IActionResult> DeleteHospital(int id)
        {
            var hospital = await _context.Hospitals.FindAsync(id);
            if (hospital == null)
            {
                return NotFound();
            }

            _context.Hospitals.Remove(hospital);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        private bool HospitalExists(int id)
        {
            return _context.Hospitals.Any(e => e.Id == id);
        }
    }
}
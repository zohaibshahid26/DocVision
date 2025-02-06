using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using FindADoctorApi.Model;
using FindADoctorApi.Data;
using FindADoctorApi.DTO;

namespace FindADoctorApi.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class AppointmentsController : ControllerBase
    {
        private readonly ApplicationDBContext _context;

        public AppointmentsController(ApplicationDBContext context)
        {
            _context = context;
        }

        [HttpPost("book")]
        public async Task<IActionResult> BookAppointment([FromBody] BookAppointmentDto dto)
        {
            // Find the doctor by ID
            var doctor = await _context.Doctors.FindAsync(dto.DoctorId);
            if (doctor == null)
            {
                return NotFound("Doctor not found.");
            }

            // Create a new appointment
            var appointment = new Appointment
            {
                AppointmentTime = dto.AppointmentTime,
                PatientId = dto.PatientId,
                Doctor = doctor,
                Status = true
            };
            _context.Appointments.Add(appointment);
            await _context.SaveChangesAsync();

            return Ok("Appointment booked successfully.");
        }

        // Get appointments by doctor
        [HttpGet("by-doctor/{doctorId}")]
        public async Task<IActionResult> GetAppointmentsByDoctor(int doctorId)
        {
            var appointments = await _context.Appointments
                .Where(a => a.Doctor.Id == doctorId)
                .Select(a => new AppointmentDto
                {
                    Id = a.Id,
                    AppointmentTime = a.AppointmentTime,
                    PatientId = a.PatientId,
                    DoctorName = a.Doctor.Name,
                    Status = a.Status
                })
                .ToListAsync();

            if (!appointments.Any())
            {
                return NotFound("No appointments found for this doctor.");
            }

            return Ok(appointments);
        }

        // Get appointments by patient
        [HttpGet("by-patient/{patientId}")]
        public async Task<IActionResult> GetAppointmentsByPatient(string patientId)
        {
            var appointments  = await _context.Appointments
                .Where(a => a.PatientId == patientId)
                .Select(a => new AppointmentDto
                {
                    Id = a.Id,
                    AppointmentTime = a.AppointmentTime,
                    PatientId = a.PatientId,
                    DoctorName = a.Doctor.Name,
                    Status = a.Status,
                    DoctorId = a.DoctorId
                })
                .ToListAsync();

            if (!appointments.Any() || appointments == null)
            {
                return NotFound(appointments);
            }

            return Ok(appointments);
        }

        // Cancel an appointment
        [HttpPut("cancel/{appointmentId}")]
        public async Task<IActionResult> CancelAppointment(int appointmentId)
        {
            var appointment = await _context.Appointments.FindAsync(appointmentId);
            if (appointment == null)
            {
                return NotFound("Appointment not found.");
            }

            appointment.Status = false; // Mark appointment as cancel
            await _context.SaveChangesAsync();

            return Ok("Appointment canceled successfully.");
        }
    }
}

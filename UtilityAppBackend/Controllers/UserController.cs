using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Security.Claims;
using UtilityAppBackend.Services;

namespace UtilityAppBackend.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class UserController : ControllerBase
    {
        private readonly AuthService _authService;

        public UserController(AuthService authService)
        {
            _authService = authService;
        }

        private int GetUserId()
        {
            var claim = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;
            if (claim == null) throw new Exception("Unauthorized");
            return int.Parse(claim);
        }

        [Authorize]
        [HttpGet("profile")]
        public IActionResult GetProfile()
        {
            var user = _authService.GetById(GetUserId());
            if (user == null) return NotFound();

            return Ok(new ProfileDto
            {
                Id = user.Id,
                FirstName = user.FirstName,
                LastName = user.LastName,
                Email = user.Email,
                Phone = user.Phone,
                ProfileImageUrl = user.ProfileImageUrl,
                NotificationsEnabled = user.NotificationsEnabled
            });
        }

        [Authorize]
        [HttpPut("profile")]
        public IActionResult UpdateProfile([FromBody] UpdateProfileRequest request)
        {
            if (string.IsNullOrWhiteSpace(request.FirstName))
                return BadRequest("First name required");

            if (string.IsNullOrWhiteSpace(request.LastName))
                return BadRequest("Last name required");

            if (!request.Email.Contains("@"))
                return BadRequest("Invalid email");

            if (request.Phone.Length < 7)
                return BadRequest("Invalid phone");

            try
            {
                _authService.UpdateProfile(GetUserId(), request);
                return Ok();
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [Authorize]
        [HttpPut("profile/image")]
        public async Task<IActionResult> UploadProfileImage(IFormFile image)
        {
            if (image == null || image.Length == 0)
                return BadRequest("No image");
            
            if (image.Length > 5 * 1024 * 1024)
                return BadRequest("Image too large");

            var fileName = $"{Guid.NewGuid()}.jpg";
            var filePath = Path.Combine("wwwroot/images", fileName);

            Directory.CreateDirectory("wwwroot/images");

            using (var stream = new FileStream(filePath, FileMode.Create))
            {
                await image.CopyToAsync(stream);
            }

            var user = _authService.GetById(GetUserId());

            if (!string.IsNullOrEmpty(user.ProfileImageUrl))
            {
                var oldPath = Path.Combine("wwwroot", user.ProfileImageUrl.TrimStart('/'));
                if (System.IO.File.Exists(oldPath))
                    System.IO.File.Delete(oldPath);
            }
            _authService.UpdateProfileImage(GetUserId(), $"/images/{fileName}");

            return Ok();
        }

        [Authorize]
        [HttpPut("notifications")]
        public IActionResult UpdateNotifications([FromBody] UpdateNotificationRequest request)
        {
            _authService.UpdateNotifications(GetUserId(), request.Enabled);
            return Ok();
        }
    }
}
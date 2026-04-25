using Microsoft.AspNetCore.Mvc;
using UtilityAppBackend.DTOs;
using UtilityAppBackend.Services;
using Microsoft.AspNetCore.Authorization;

namespace UtilityAppBackend.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class AuthController : ControllerBase
    {
        private readonly AuthService _authService;

        public AuthController(AuthService authService)
        {
            _authService = authService;
        }

        // STEP 1
        [HttpPost("check-email")]
        public IActionResult CheckEmail(CheckEmailDto request)
        {
            var available = _authService.IsEmailAvailable(request.Email);

            if (!available)
                return BadRequest("Email already exists");

            return Ok("Email is available");
        }

        // STEP 2
        [HttpPost("register-step2")]
        public IActionResult Register(RegisterStep2Dto request)
        {
            try
            {
                var user = _authService.Register(request);
                return Ok(user);
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [Authorize]
        [HttpGet("test-auth")]
        public IActionResult TestAuth()
        {
            return Ok("You are authorized!");
        }

        // LOGIN
        [HttpPost("login")]
        public IActionResult Login(LoginDto request)
        {
            try
            {
                var user = _authService.Login(request.Email, request.Password);
                return Ok(user);
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        public class VerifyRequest {
            public string Email { get; set; }
        }

        // STEP 3 (temporary test)
        [HttpPost("verify")]
        public IActionResult Verify([FromBody] VerifyRequest request)
        {
            _authService.VerifyUser(request.Email);
            return Ok("User verified");
        }

        [HttpPost("forgot-password")]
        public IActionResult ForgotPassword([FromBody] ForgotPasswordRequest request)
        {
            try
            {
                _authService.SendResetCode(request.Email);
                return Ok();
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPost("verify-code")]
        public IActionResult VerifyCode([FromBody] VerifyCodeRequest request)
        {
            try
            {
                _authService.VerifyCode(request.Email, request.Code);
                return Ok();
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPost("reset-password")]
        public IActionResult ResetPassword([FromBody] ResetPasswordRequest request)
        {
            try
            {
                _authService.ResetPassword(request.Email, request.NewPassword);
                return Ok();
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }
    }
}
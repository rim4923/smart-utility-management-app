using UtilityAppBackend.Models;
using UtilityAppBackend.DTOs;
using System.Linq;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using Microsoft.IdentityModel.Tokens;
using System.Text;

namespace UtilityAppBackend.Services
{
    
    public class AuthService
    {
        private readonly AppDbContext _context;
        private readonly EmailService _emailService;
        public AuthService(AppDbContext context, EmailService emailService)
        {
            _context = context;
            _emailService = emailService;
        }
        public bool IsEmailAvailable(string email)
        {
            return !_context.Users.Any(u => u.Email == email);
        }

        public User Register(RegisterStep2Dto request)
        {
            if (!IsEmailAvailable(request.Email))
                throw new Exception("Email already exists");

            var user = new User
            {
                Email = request.Email,
                PasswordHash = BCrypt.Net.BCrypt.HashPassword(request.Password),

                FirstName = request.FirstName,
                LastName = request.LastName,
                Phone = request.Phone,
                Nationality = request.Nationality,

                IsVerified = false
            };

            _context.Users.Add(user);
            _context.SaveChanges();
            return user;
        }

        public LoginResponse  Login(string email, string password)
        {
            var user = _context.Users.FirstOrDefault(u => u.Email == email);

            if (user == null)
                throw new Exception("Email not found");

            if (!BCrypt.Net.BCrypt.Verify(password, user.PasswordHash))
                throw new Exception("Wrong password");

            if (!user.IsVerified)
                throw new Exception("User not verified yet");

            var token = GenerateJwtToken(user);

            return new LoginResponse
            {
                Token = token,
                Email = user.Email
            };
        }

        // STEP 3 (OCR later)
        public void VerifyUser(string email)
        {
            var user = _context.Users.FirstOrDefault(u => u.Email == email);

            if (user == null)
                throw new Exception("User not found");

            user.IsVerified = true;
            _context.SaveChanges();
        }

        public User GetById(int id)
        {
           return _context.Users.FirstOrDefault(u => u.Id == id);
        }

        public string GenerateJwtToken(User user)
        {
            var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes("THIS_IS_SUPER_SECRET_KEY_1234567890_ABCD"));
            var creds = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);

            var claims = new[]
            {
                new Claim(ClaimTypes.NameIdentifier, user.Id.ToString()),
                new Claim(ClaimTypes.Email, user.Email)
            };

            var token = new JwtSecurityToken(
                issuer: "UtilityApp",
                audience: "UtilityAppUsers",
                claims: claims,
                expires: DateTime.Now.AddHours(2),
                signingCredentials: creds
            );

            return new JwtSecurityTokenHandler().WriteToken(token);
        }

        public void UpdateProfile(int userId, UpdateProfileRequest request)
        {
            var exists = _context.Users
                .Any(u => u.Email == request.Email && u.Id != userId);

            if (exists)
                throw new Exception("Email already in use");

            var user = _context.Users.Find(userId);

            user.FirstName = request.FirstName;
            user.LastName = request.LastName;
            user.Email = request.Email;
            user.Phone = request.Phone;

            _context.SaveChanges();
        }
        public void UpdateProfileImage(int userId, string imageUrl)
        {
            var user = _context.Users.FirstOrDefault(u => u.Id == userId);
            if (user == null) throw new Exception("User not found");

            user.ProfileImageUrl = imageUrl;

            _context.SaveChanges();
        }
        public void UpdateNotifications(int userId, bool enabled)
        {
            var user = _context.Users.FirstOrDefault(u => u.Id == userId);
            if (user == null) throw new Exception("User not found");

            user.NotificationsEnabled = enabled;

            _context.SaveChanges();
        }
        private string GenerateCode()
        {
            var random = new Random();
            return random.Next(1000, 9999).ToString(); 
        }

        public void SendResetCode(string email)
        {
            var user = _context.Users.FirstOrDefault(u => u.Email == email);
            if (user == null) throw new Exception("User not found");

            var code = GenerateCode();

            user.ResetCode = code;
            user.ResetCodeExpiry = DateTime.UtcNow.AddMinutes(5);

            _context.SaveChanges();

            _emailService.SendEmail(
                email,
                "Password Reset Code",
                $"Your reset code is: {code}"
            );
        }

        public void VerifyCode(string email, string code)
        {
            var user = _context.Users.FirstOrDefault(u => u.Email == email);
            if (user == null) throw new Exception("User not found");

            if (user.ResetCode != code)
                throw new Exception("Invalid code");

            if (user.ResetCodeExpiry < DateTime.UtcNow)
                throw new Exception("Code expired");
        }

        public void ResetPassword(string email, string newPassword)
        {
            var user = _context.Users.FirstOrDefault(u => u.Email == email);
            if (user == null) throw new Exception("User not found");

            user.PasswordHash = BCrypt.Net.BCrypt.HashPassword(newPassword);

            user.ResetCode = null;
            user.ResetCodeExpiry = null;

            _context.SaveChanges();
        }
    }
}
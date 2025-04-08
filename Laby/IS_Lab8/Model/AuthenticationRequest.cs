using System.ComponentModel.DataAnnotations;

namespace IS_Lab8.Model
{
    public class AuthenticationRequest
    {
        [Required]
        public string Username { get; set; }
        [Required]
        public string Password { get; set; }
    }
}
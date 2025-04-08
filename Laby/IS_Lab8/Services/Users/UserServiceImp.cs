using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using System.Text;
using IS_Lab8.Entities;
using IS_Lab8.Model;
using Microsoft.Extensions.Configuration;
using Microsoft.IdentityModel.Tokens;

namespace IS_Lab8.Services.Users
{
    public class UserServiceImp : IUserService
    {
        private static List<User> users = new List<User>
        {
            new User{Id = 1, Username = "Andrzej", Password =
                "Andrzej", Roles = new List<Role>{ new Role { Role_ = "admin"
            }, new Role { Role_ = "user" } } },
            new User{Id = 2, Username = "Piotrek", Password =
                "Piotrek", Roles = new List<Role>{ new Role { Role_ =
                "number" }, new Role { Role_ = "user" } } },
            new User{Id = 3, Username = "Ania", Password =
                    "Ania", Roles = new List<Role>{new Role { Role_ = "user" } }
            }
        };

        private IConfiguration configuration;

        public UserServiceImp(IConfiguration configuration)
        {
            this.configuration = configuration;
        }

        public AuthenticationResponse Authenticate(AuthenticationRequest request)
        {
            User user = GetByUsername(request.Username);
            if (user == null || user.Password != request.Password)
            {
                return null;
            }

            string token = GenerateJwtToken(user);
            return new AuthenticationResponse(user, token);
        }

        public IEnumerable<User> GetUsers()
        {
            return users;
        }

        public User GetByUsername(string username)
        {
            return users.FirstOrDefault(u => u.Username == username);
        }

        public User GetById(int id)
        {
            return users.FirstOrDefault(x => x.Id == id);
        }

        public string GenerateJwtToken(User user)
        {
            var tokenHandler = new JwtSecurityTokenHandler();
            var key = Encoding.ASCII.GetBytes(configuration["Jwt:key"]);
            var claims = new List<Claim>();
            claims.Add(new Claim("id", user.Id.ToString()));
            foreach (var role in user.Roles)
            {
                claims.Add(new Claim(ClaimTypes.Role, role.Role_));
            }
            var tokenDescriptor = new SecurityTokenDescriptor
            {
                Subject = new ClaimsIdentity(claims.ToArray()),
                Expires = DateTime.Now.AddHours(1),
                SigningCredentials = new SigningCredentials(
                    new SymmetricSecurityKey(key),
                    SecurityAlgorithms.HmacSha256Signature
                )
            };
            var token = tokenHandler.CreateToken(tokenDescriptor);
            return tokenHandler.WriteToken(token);
        }
    }
}
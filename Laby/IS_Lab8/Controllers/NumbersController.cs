using System;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace IS_Lab8.Controllers
{
    [ApiController]
    [Route("/api/[controller]")]
    public class NumbersController : ControllerBase
    {
        [HttpGet("random")]
        [Authorize(Roles = "number", AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme)]
        public IActionResult GetRandom()
        {
            var primes = new[] {2, 3, 5, 7, 11, 13, 17, 19};
            int random  = primes[new Random().Next(0, primes.Length)];
            return Ok(random);
        }
    }
}
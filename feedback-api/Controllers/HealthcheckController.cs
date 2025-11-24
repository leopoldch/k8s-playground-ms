using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace glo4008.Feedback.Controllers
{
    [ApiController]
    [Route("health")]
    public class HealthcheckController : Controller
    {
       [HttpGet]
        public StatusCodeResult Health()
        {
            return StatusCode(StatusCodes.Status200OK);   
        }
    }
}
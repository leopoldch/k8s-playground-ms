using System.Threading.Tasks;
using glo4008.Feedback.Model;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace glo4008.Feedback.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class FeedbackController : ControllerBase
    {
        private readonly FeedbackContext _feedbackContext;

        public FeedbackController(FeedbackContext feedbackContext)
        {
            _feedbackContext = feedbackContext;
        }

        [HttpGet]
        public async Task<IActionResult> Get()
        {
            return Ok(await _feedbackContext.Feedback.ToListAsync());   
        }
        
        [HttpPost]
        public async Task<IActionResult> Post([FromBody] Model.Feedback feedback)
        {
            _feedbackContext.Feedback.Add(feedback);
            await _feedbackContext.SaveChangesAsync();
            
            return StatusCode(StatusCodes.Status201Created);
        }
    }
}

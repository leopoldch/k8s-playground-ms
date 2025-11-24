package com.glo4008.api;

import com.glo4008.api.dto.FeedbackDto;
import com.glo4008.api.dto.SentenceDto;
import com.glo4008.api.dto.PolarityDto;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@CrossOrigin(origins = "*")
@RestController()
@RequestMapping("/feedback")
public class FeedbackController {

  Logger logger = LogManager.getLogger(FeedbackController.class);

  @Value("${glo4008.feedback.api.url}")
  private String feedbackApiUrl;

  @PostMapping
  public ResponseEntity submitFeedback(@RequestBody FeedbackDto feedbackDto) {
    RestTemplate restTemplate = new RestTemplate();
    logger.trace(String.format("Routing request to feedback api at url %s", feedbackApiUrl));

    return restTemplate.postForEntity(feedbackApiUrl + "/feedback", feedbackDto, ResponseEntity.class);
  }
}



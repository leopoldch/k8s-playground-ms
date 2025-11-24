package com.glo4008.api;

import com.glo4008.api.dto.SentenceDto;
import com.glo4008.api.dto.PolarityDto;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@CrossOrigin(origins = "*")
@RestController()
@RequestMapping("/polarity")
public class PolarityController {

    Logger logger = LogManager.getLogger(PolarityController.class);

    @Value("${glo4008.logic.api.url}")
    private String logicApiUrl;

    @PostMapping
    public PolarityDto analyseSentence(@RequestBody SentenceDto sentenceDto) {
        RestTemplate restTemplate = new RestTemplate();
        logger.trace(String.format("Routing request to Logic api at url %s", logicApiUrl));

        return restTemplate.postForEntity(logicApiUrl + "/analyse/sentence",
                sentenceDto, PolarityDto.class)
                .getBody();
    }
}



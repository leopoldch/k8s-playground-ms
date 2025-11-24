package com.glo4008.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/health")
public class HealthcheckController {

  @GetMapping
  public ResponseEntity healthcheck() {
    return new ResponseEntity(HttpStatus.OK);
  }
}
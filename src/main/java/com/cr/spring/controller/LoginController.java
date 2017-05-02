package com.cr.spring.controller;

import com.cr.googleapi.Verifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by harisa on 5/1/17.
 */
@RestController
@RequestMapping("login")
public class LoginController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private Verifier verifier;

	@PostMapping(value="gapi", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<?> login(@RequestParam("token") String token) throws Exception {

		logger.info("Logging in using Google API token: " + token);
		String email = verifier.verify(token);
		return ResponseEntity.ok(email);
	}
}

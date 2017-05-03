package com.cr.spring.controller;

import com.cr.googleapi.Verifier;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileReader;

/**
 * Created by harisa on 5/1/17.
 */
@RestController
@RequestMapping("login")
public class LoginController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final String REDIRECT_URI = "http://localhost:8080";
	@Autowired
	private Verifier verifier;

	@PostMapping(value = "gapi", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<?> login(@RequestParam("token") String token) throws Exception {

		logger.info("Logging in using Google API token: " + token);
		String email = verifier.verify(token);
		return ResponseEntity.ok(email);
	}

	@PostMapping(value = "googleauthcode", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<?> loginUsingGoogleAuthCode(@RequestBody String authCode, HttpServletRequest request)
			throws Exception {

		logger.info("Logging in using Google authorization code: " + authCode);
		if (request.getHeader("X-Requested-With") == null) {
			// Without the `X-Requested-With` header, this request could be forged. Aborts.
			return ResponseEntity.badRequest().body("CORS error. Invalid origin.");
		}
		// Set path to the Web application client_secret_*.json file you downloaded from the
		// Google API Console: https://console.developers.google.com/apis/credentials
		// You can also find your Web application client ID and client secret from the
		// console and specify them directly when you create the GoogleAuthorizationCodeTokenRequest
		// object.
		String CLIENT_SECRET_FILE = "/Users/harisa/Documents/IdeaProjects/client_secret_383063704998" +
				"-6uvuprj3244hbj7cucgh1vvebk218b9p.apps.googleusercontent.com.json";

		// Exchange auth code for access token
		GoogleClientSecrets clientSecrets =
				GoogleClientSecrets.load(
						JacksonFactory.getDefaultInstance(), new FileReader(CLIENT_SECRET_FILE));

		GoogleTokenResponse tokenResponse =
				new GoogleAuthorizationCodeTokenRequest(
						new NetHttpTransport(),
						JacksonFactory.getDefaultInstance(),
						"https://www.googleapis.com/oauth2/v4/token",
						clientSecrets.getDetails().getClientId(),
						clientSecrets.getDetails().getClientSecret(),
						authCode,
						REDIRECT_URI)  // Specify the same redirect URI that you use with your web
						// app. If you don't have a web version of your app, you can
						// specify an empty string.
						.execute();

		// Use access token to call API
		/*String accessToken = tokenResponse.getAccessToken();
		GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
		Drive drive =
				new Drive.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
						.setApplicationName("Auth Code Exchange Demo")
						.build();
		File file = drive.files().get("appfolder").execute();*/

		// Get profile info from ID token
		GoogleIdToken idToken = tokenResponse.parseIdToken();
		GoogleIdToken.Payload payload = idToken.getPayload();
		String userId = payload.getSubject();  // Use this value as a key to identify a user.
		String email = payload.getEmail();
		boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
		String name = (String) payload.get("name");
		String pictureUrl = (String) payload.get("picture");
		String locale = (String) payload.get("locale");
		String familyName = (String) payload.get("family_name");
		String givenName = (String) payload.get("given_name");

		logger.info("Identified user " + name + " with email " + email);
		return ResponseEntity.ok(email);
	}
}

package com.cr.googleapi;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * Created by harisa on 5/1/17.
 */
@Component
public class Verifier {

	private static final String CLIENT_ID = "383063704998-6uvuprj3244hbj7cucgh1vvebk218b9p.apps.googleusercontent.com";
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public String verify(String token) throws Exception {

		HttpTransport transport = new NetHttpTransport();
		JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory).setAudience
				(Collections
						.singletonList
								(CLIENT_ID)).build();
		GoogleIdToken idToken = verifier.verify(token);
		if (idToken != null) {
			GoogleIdToken.Payload payload = idToken.getPayload();

			// Print user identifier
			String userId = payload.getSubject();
			logger.info("User ID: " + userId);

			// Get profile information from payload
			String email = payload.getEmail();
			boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
			String name = (String) payload.get("name");
			String pictureUrl = (String) payload.get("picture");
			String locale = (String) payload.get("locale");
			String familyName = (String) payload.get("family_name");
			String givenName = (String) payload.get("given_name");

			logger.info("Name: " + name);
			return email;

			// Use or store profile information
			// ...

		} else {
			throw new Exception("Invalid ID token.");
		}
	}
}

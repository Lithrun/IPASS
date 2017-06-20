package ipass.eventapp.webservices;

import java.security.Key;
import java.util.Calendar;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import ipass.eventapp.models.GebruikerService;
import ipass.eventapp.models.ServiceProvider;

@Path("/authentication")
public class AuthenticationResource {
	final static public Key key = MacProvider.generateKey();
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response authenticateUser(	@FormParam("emailadres") String emailadres,
										@FormParam("password") String wachtwoord) {
		System.out.println("Starting authentication...");
		try {
		// Authenticate the user against the database
		GebruikerService service = ServiceProvider.getGebruikerService();
		System.out.println(emailadres + "    " + wachtwoord);
		String role = service.getRoleByEmailAndWachtwoord(emailadres, wachtwoord);
		if (role == null) { throw new IllegalArgumentException("No user found!"); }
		int id = service.getIdByEmailAndWachtwoord(emailadres, wachtwoord);
		JsonObjectBuilder job = Json.createObjectBuilder();
			// Issue a token for the user
			Calendar expiration = Calendar.getInstance();
			expiration.add(Calendar.MINUTE, 30);
			String token = Jwts.builder()
				.setSubject(emailadres)
				.claim("role", role)
				.setExpiration(expiration.getTime())
				.signWith(SignatureAlgorithm.HS512, key)
				.compact();
			// Return the token on the response
			job.add("token", token);
			job.add("role", role);
			job.add("gebruikerid", id);
		return Response.ok(job.build().toString()).build();
	} catch (JwtException | IllegalArgumentException e) {
		return Response.status(Response.Status.UNAUTHORIZED).build();
	}
	}
}
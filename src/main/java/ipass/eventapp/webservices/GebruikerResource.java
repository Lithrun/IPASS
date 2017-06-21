package ipass.eventapp.webservices;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.hibernate.LazyInitializationException;

import ipass.eventapp.entity.Enquete;
import ipass.eventapp.entity.Evenement;
import ipass.eventapp.entity.Gebruiker;
import ipass.eventapp.entity.GebruikerEvenement;
import ipass.eventapp.entity.Vraag;
import ipass.eventapp.models.EnqueteService;
import ipass.eventapp.models.GebruikerService;
import ipass.eventapp.models.ServiceProvider;

@Path("/gebruikers")
public class GebruikerResource {
	
	public static JsonObjectBuilder gebruikerToJsonObjectBuilder(Gebruiker g, boolean evenementen){
		JsonObjectBuilder job = Json.createObjectBuilder();
		JsonArrayBuilder jab = Json.createArrayBuilder();
		job.add("id", g.getId());
		job.add("emailadres", g.getEmailadres());
		job.add("voornaam", g.getVoornaam());
		try {
			job.add("tussenvoegsel", g.getTussenvoegsel());
		} catch (NullPointerException npe) {
			job.add("tussenvoegsel", "");
		}
		job.add("achternaam", g.getAchternaam());
		job.add("type", g.getType());
		job.add("geslacht", g.getGeslacht());
		if (evenementen) {
			
			for (GebruikerEvenement ge : g.getGebruikerEvenementen() ) {
				JsonObjectBuilder jobstatus = EvenementResource.evenementToJsonObjectBuilder(ge.getEvenement(), true, false, false);
				jobstatus.add("gebruikerstatus", ge.getStatus());
				jab.add(jobstatus);
			}
			job.add("evenementen", jab);
			
		}		
		
		return job;
	}
	
	//http://localhost:4711/eventapp/restservices/gebruikers?evenementen=true
	@GET
	@RolesAllowed({"eventmanager"})
	@Produces("application/json")
	public Response getAllGebruikers(@QueryParam("evenementen") boolean evenementen) {
		try {
			System.out.println("Getting all gebruikers");
			GebruikerService service = ServiceProvider.getGebruikerService();
			List<Gebruiker> gebruikers = service.getAllGebruikers(evenementen);
			if (gebruikers == null) { throw new IllegalArgumentException("Geen gebruikers gevonden");}
			JsonArrayBuilder jab = Json.createArrayBuilder();
			for (Gebruiker g : gebruikers) {
				jab.add(gebruikerToJsonObjectBuilder(g, evenementen));
			}
			JsonArray array = jab.build();
			System.out.println(array.toString());
			return Response.ok(array.toString()).build();
		}catch (IllegalArgumentException e ) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
	}
	
	//http://localhost:4711/eventapp/restservices/gebruikers/1?evenementen=true
	@GET
	@Path("{id}")
	@RolesAllowed({"eventmanager","gebruiker"})
	@Produces("application/json")
	public Response getGebruikerById(@PathParam("id") int id, @QueryParam("evenementen") boolean evenementen) {
		try {
			System.out.println("Getting gebruiker by ID: " + id);
			GebruikerService service = ServiceProvider.getGebruikerService();
			Gebruiker gebruiker = service.getGebruikerById(id, evenementen);
			if (gebruiker == null) { throw new IllegalArgumentException("Geen gebruikers gevonden");}	
			JsonObject object = gebruikerToJsonObjectBuilder(gebruiker, evenementen).build();
			System.out.println(object.toString());
			return Response.ok(object.toString()).build();
		}catch (IllegalArgumentException e ) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
	}
	
	@PUT
	@Path("/gebruikerstatus")
	@RolesAllowed({"eventmanager","gebruiker"})
	public Response updateGebruikerStatusByGebruikerIdAndEvenementId(@QueryParam("gebruikerid") int gebruikerid, @QueryParam("evenementid") int evenementid, @QueryParam("gebruikerstatus") String gebruikerstatus) {
		try {
			System.out.println("Updating gebruiker with ID: " + gebruikerid);
			GebruikerService service = ServiceProvider.getGebruikerService();
			boolean resultaat = service.updateGebruikerEvenementStatus(gebruikerid, evenementid, gebruikerstatus);
			
			if (resultaat == true) {
				return Response.ok().build();
			} else {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
			
		}catch (IllegalArgumentException e ) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
	}

}

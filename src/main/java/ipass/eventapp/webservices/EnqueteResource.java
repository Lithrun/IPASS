package ipass.eventapp.webservices;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.hibernate.LazyInitializationException;

import ipass.eventapp.entity.Enquete;
import ipass.eventapp.entity.Evenement;
import ipass.eventapp.entity.Vraag;
import ipass.eventapp.models.EnqueteService;
import ipass.eventapp.models.EvenementService;
import ipass.eventapp.models.ServiceProvider;
import ipass.eventapp.persistence.EnqueteDAO;


@Path("/enquetes")
public class EnqueteResource {
		
	public static JsonObjectBuilder enqueteToJsonObjectBuilder(Enquete e, boolean vragen){
		JsonObjectBuilder job = Json.createObjectBuilder();
		JsonArrayBuilder jab = Json.createArrayBuilder();
		job.add("id", e.getId());
		job.add("titel", e.getTitel());
		
		if (vragen) {
			for (Vraag v : e.getVraagList()) {
				jab.add(VraagResource.vraagToJsonObjectBuilder(v));
			}
			job.add("vragen", jab);
		}
		
		
		return job;
	}
		
		@GET
		@RolesAllowed({"eventmanager","gebruiker"})
		@Produces("application/json")
		public Response getAllEnquetes(@QueryParam("vragen") boolean vragen) {
			try {
				System.out.println("Getting all enquetes");
				EnqueteService service = ServiceProvider.getEnqueteService();
				List<Enquete> enquetes = service.getAllEnquetes(vragen);
				if (enquetes == null) {throw new IllegalArgumentException("Geen enquetes gevonden");}
				JsonArrayBuilder jab = Json.createArrayBuilder();
				for (Enquete e : enquetes) {
					jab.add(enqueteToJsonObjectBuilder(e, vragen));
				}
				JsonArray array = jab.build();
				System.out.println(array.toString());
				return Response.ok(array.toString()).build();
			}catch (IllegalArgumentException e ) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
			
		}
		
		//http://localhost:4711/eventapp/restservices/enquetes/1?vragen=true
		@GET
		@Path("{id}")
	        @PermitAll
		//@RolesAllowed({"eventmanager","gebruiker"})
		@Produces("application/json")
		public Response getAllEnquetes(@PathParam("id") int id, @DefaultValue("true") @QueryParam("boolean") boolean vragen) {
			try {	
				System.out.println("Getting enquete by ID: " + id);
				EnqueteService service = ServiceProvider.getEnqueteService();
				Enquete enquete = service.getEnqueteById(id, vragen);
				if (enquete == null) {throw new IllegalArgumentException("Geen enquetes gevonden");}
				JsonObject object = enqueteToJsonObjectBuilder(enquete, vragen).build();
				System.out.println(object.toString());
				return Response.ok(object.toString()).build();
			}catch (IllegalArgumentException e ) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
			
		}
		
	
}

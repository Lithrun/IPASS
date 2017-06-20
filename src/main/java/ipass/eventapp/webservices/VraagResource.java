package ipass.eventapp.webservices;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import ipass.eventapp.entity.Enquete;
import ipass.eventapp.entity.Vraag;
import ipass.eventapp.models.EnqueteService;
import ipass.eventapp.models.ServiceProvider;
import ipass.eventapp.models.VraagService;

@Path("/vragen")
public class VraagResource {
	
	public static JsonObjectBuilder vraagToJsonObjectBuilder(Vraag v){
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("id",v.getId());
		job.add("vraag", v.getVraag());
		return job;
	}
		
	@GET
	//@RolesAllowed({"eventmanager","gebruiker"})
	@Produces("application/json")
	public Response getAllVragen() {
		try {
			System.out.println("Getting all vragen");
			VraagService service = ServiceProvider.getVraagService();
			List<Vraag> vragen = service.getAllVragen();
			if (vragen == null) { throw new IllegalArgumentException("Geen vragen gevonden");}
			JsonArrayBuilder jab = Json.createArrayBuilder();
			for (Vraag v : vragen) {
				jab.add(vraagToJsonObjectBuilder(v));
			}
			JsonArray array = jab.build();
			System.out.println(array.toString());
			return Response.ok(array.toString()).build();	
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
		
		@GET
		@Path("{id}")
		//@RolesAllowed({"eventmanager","gebruiker"})
		@Produces("application/json")
		public Response getVraagById(@PathParam("id") int id) {
			try {
				System.out.println("Getting vraag by ID: " + id);
				VraagService service = ServiceProvider.getVraagService();
				Vraag vraag = service.getVraagById(id);
				if (vraag == null) { throw new IllegalArgumentException("Geen vragen gevonden");}
				JsonObject object = vraagToJsonObjectBuilder(vraag).build();
				System.out.println(object.toString());
				return Response.ok(object.toString()).build();
			} catch (IllegalArgumentException e) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
		}
	

}

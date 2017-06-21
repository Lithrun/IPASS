package ipass.eventapp.webservices;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import ipass.eventapp.entity.Enquete;
import ipass.eventapp.entity.Evenement;
import ipass.eventapp.entity.EvenementEnquete;
import ipass.eventapp.entity.Gebruiker;
import ipass.eventapp.entity.GebruikerEvenement;
import ipass.eventapp.entity.Vraag;
import ipass.eventapp.models.EnqueteService;
import ipass.eventapp.models.EvenementService;
import ipass.eventapp.models.GebruikerService;
import ipass.eventapp.models.ServiceProvider;
import ipass.eventapp.models.VraagService;

@Path("/evenementen")
public class EvenementResource {

		public static JsonObjectBuilder evenementToJsonObjectBuilder(Evenement e, boolean enquete, boolean gebruikers, boolean enqueteantwoorden){
			JsonObjectBuilder job = Json.createObjectBuilder();
			JsonArrayBuilder jab = Json.createArrayBuilder();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			job.add("id", e.getId());
			job.add("naam", e.getNaam());
			job.add("informatie", e.getInformatie());
			try {
				job.add("bijzonderheden", e.getBijzonderheden());
			} catch (NullPointerException npe) {
				job.add("bijzonderheden", "");
			}
			job.add("status", e.getStatus());
			job.add("begintijd", df.format(e.getBegintijd()));
			job.add("eindtijd", df.format(e.getEindtijd()));
			job.add("plaats", e.getPlaats());
			job.add("straat", e.getStraat());
			job.add("huisnummer", e.getHuisnummer());
			job.add("postcode", e.getPostcode());
			job.add("land", e.getLand());
			if (enquete) {
				job.add("enqueteid", e.getEnquete().getId());
				job.add("enquetetitel", e.getEnquete().getTitel());
				job.add("enquetegeldigtot", df.format(e.getEnqueteGeldig()));
			}
			if (gebruikers) {
				
				for (GebruikerEvenement ge : e.getGebruikerEvenementen() ) {
					JsonObjectBuilder jobstatus = GebruikerResource.gebruikerToJsonObjectBuilder(ge.getGebruiker(), false);
					jobstatus.add("gebruikerstatus", ge.getStatus());
					jab.add(jobstatus);
				}
				job.add("gebruikers", jab);
				
			}
			if (enqueteantwoorden) {
				JsonArrayBuilder jab2 = Json.createArrayBuilder();
				ArrayList<Vraag> vragen = new ArrayList<Vraag>();
				int i = 0;
				for (EvenementEnquete ee : e.getEvenementEnquete()) {
					for (Vraag v : vragen){
						if (v.equals(ee.getVraag())) {
							// stop
							i = 1;
						}
					}
					if (i == 0) {
						vragen.add(ee.getVraag());
						JsonObjectBuilder job3 = Json.createObjectBuilder();
						job3.add("vraagid", ee.getVraag().getId());
						job3.add("vraag", ee.getVraag().getVraag());
						jab2.add(job3);
					}
					i = 0;
				}
				
				for (EvenementEnquete ee : e.getEvenementEnquete() ) {
					if (!ee.getAntwoord().equals("")) {
						JsonObjectBuilder job2 = Json.createObjectBuilder();
						
						job2.add("gebruikerid", ee.getGebruiker().getId());
						job2.add("vraagid", ee.getVraag().getId());
						job2.add("antwoord", ee.getAntwoord());
						jab.add(job2);
					}
					
					
				}
				job.add("vragen",jab2);
				job.add("antwoorden", jab);
				
			}
				
			return job;
		}
		
		//http://localhost:4711/eventapp/restservices/evenementen?enquete=false&gebruikers=false&enqueteantwoorden=false
		@GET
		@RolesAllowed({"eventmanager"})
		@Produces("application/json")
		public Response getAllEvenementen( @QueryParam("enquete") boolean enquete, @QueryParam("gebruikers") boolean gebruikers, @QueryParam("enqueteantwoorden") boolean enqueteantwoorden) {
			try {
				System.out.println("Getting all evenementen");
				EvenementService service = ServiceProvider.getEvenementService();
				List<Evenement> evenementen = service.getAllEvenementen(enquete, gebruikers, enqueteantwoorden);
				if (evenementen == null) { throw new IllegalArgumentException("Geen evenementen gevonden!");}
				JsonArrayBuilder jab = Json.createArrayBuilder();
				for (Evenement evenement : evenementen) {
					jab.add(evenementToJsonObjectBuilder(evenement, enquete, gebruikers, enqueteantwoorden));
				}
				JsonArray array = jab.build();
				System.out.println(array.toString());
				return Response.ok(array.toString()).build();
			} catch (IllegalArgumentException e) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
			
		}
		
		//http://localhost:4711/eventapp/restservices/evenementen/1?enquete=false&gebruikers=false&enqueteantwoorden=false
		@GET
		@Path("{id}")
		@RolesAllowed({"eventmanager","gebruiker"})
		@Produces("application/json")
		public Response getEvenementById(@PathParam("id") int id, @QueryParam("enquete") boolean enquete, @QueryParam("gebruikers") boolean gebruikers, @QueryParam("enqueteantwoorden") boolean enqueteantwoorden) {
			try {
				System.out.println("Getting evenement by ID: " + id);
				EvenementService service = ServiceProvider.getEvenementService();
				Evenement evenement = service.getEvenementById(id, enquete, gebruikers, enqueteantwoorden);
				if (evenement == null) { throw new IllegalArgumentException("Evenement bestaat niet!");}
				JsonObject object = evenementToJsonObjectBuilder(evenement, enquete, gebruikers, enqueteantwoorden).build();
				System.out.println(object.toString());
				return Response.ok(object.toString()).build();
			} catch (IllegalArgumentException e) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
			
		}
		
		@PUT
		@Path("/status")
		@RolesAllowed({"eventmanager"})
		public Response updateGebruikerStatusByGebruikerIdAndEvenementId(@QueryParam("evenementid") int evenementid, @QueryParam("status") String status) {
			try {
				System.out.println("Updating evenement with ID: " + evenementid);
				EvenementService service = ServiceProvider.getEvenementService();
				boolean resultaat = service.updateEvenementStatus(evenementid, status);
				if (resultaat == true) {
					return Response.ok().build();
				} else {
					return Response.status(Response.Status.NOT_FOUND).build();
				}
				
			}catch (IllegalArgumentException e ) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
			
		}
		
		@POST
		@Path("/enqueteresult")
		@RolesAllowed({"gebruiker"})
		public Response addEvenementEnquete(InputStream is) throws ParseException {
			try {
				Evenement evenement = inputStreamToEnqueteResult(is);
				EvenementService service = ServiceProvider.getEvenementService();
				boolean resultaat = service.addEvenement(evenement);
				if (resultaat == true) {
					return Response.ok().build();
				} else {
					return Response.status(Response.Status.NOT_FOUND).build();
				}
				
			}catch (IllegalArgumentException e ) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
		}
		
		@POST
		@RolesAllowed({"eventmanager"})
		public Response addEvenement(InputStream is) throws ParseException {
			try {
				System.out.println("Startin insert...");
				Evenement evenement = inputStreamToEvenement(is,true);
				EvenementService service = ServiceProvider.getEvenementService();
				boolean resultaat = service.addEvenement(evenement);
				if (resultaat == true) {
					return Response.ok().build();
				} else {
					return Response.status(Response.Status.NOT_FOUND).build();
				}
				
			}catch (IllegalArgumentException e ) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
			
		}
		
		@PUT
		@RolesAllowed({"eventmanager"})
		public Response updateEvenement(InputStream is) throws ParseException {
			try {
				System.out.println("Updating evenement...");
				Evenement evenement = inputStreamToEvenement(is,true);
				EvenementService service = ServiceProvider.getEvenementService();
				boolean resultaat = service.updateEvenement(evenement);
				if (resultaat == true) {
					return Response.ok().build();
				} else {
					return Response.status(Response.Status.NOT_FOUND).build();
				}
				
			}catch (IllegalArgumentException e ) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
			
		}
		
		private Evenement inputStreamToEvenement(InputStream is, boolean gebruikers) throws ParseException {
			//Evenement(Enquete enquete, String naam, String informatie, String bijzonderheden, String status,
			//Date begintijd, Date eindtijd, String plaats, String straat, String huisnummer, String postcode,
			//String land) 
			EnqueteService eservice = ServiceProvider.getEnqueteService();
			JsonObject o = Json.createReader(is).readObject();
			Enquete enquete = eservice.getEnqueteById(o.getInt("enqueteid"), false);
			Evenement evenement = new Evenement();
			try {
				evenement.setId(o.getInt("id"));
			} catch(Exception e) {
				// do nothing
			}
			evenement.setEnquete(enquete);
			evenement.setNaam(o.getString("naam"));
			evenement.setEnqueteGeldig(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(o.getString("enquetegeldigtot")));
			evenement.setInformatie(o.getString("informatie"));
			evenement.setBijzonderheden(o.getString("bijzonderheden"));
			evenement.setStatus("gepland");
			evenement.setBegintijd(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(o.getString("begintijd")));
			evenement.setEindtijd(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(o.getString("eindtijd")));
			evenement.setPlaats(o.getString("plaats"));
			evenement.setStraat(o.getString("straatnaam"));
			evenement.setHuisnummer(o.getString("huisnummer"));
			evenement.setPostcode(o.getString("postcode"));
			evenement.setLand(o.getString("land"));
			if (gebruikers) {
				List<GebruikerEvenement> gebruikerevenementen = new ArrayList<GebruikerEvenement>();
				GebruikerService gservice = ServiceProvider.getGebruikerService();
				for ( JsonValue i : o.getJsonArray("gebruikers")) {
					GebruikerEvenement ge = new GebruikerEvenement();
					ge.setGebruiker(gservice.getGebruikerById(Integer.parseInt(String.valueOf(i)), false));
					ge.setEvenement(evenement);
					ge.setStatus("uitgenodigd");
					gebruikerevenementen.add(ge);
				}
				evenement.setGebruikerEvenementen(gebruikerevenementen);
			}	
			
			return evenement;
			
		}
		
		private Evenement inputStreamToEnqueteResult(InputStream is) throws ParseException {
			
			VraagService vs = ServiceProvider.getVraagService();
			JsonObject o = Json.createReader(is).readObject();
			Evenement evenement = ServiceProvider.getEvenementService().getEvenementById(o.getInt("evenementid"), false, false, false);
			Gebruiker gebruiker = ServiceProvider.getGebruikerService().getGebruikerById(o.getInt("gebruikerid"), false);
			
			List<EvenementEnquete> ee = new ArrayList<EvenementEnquete>();
			System.out.println(o.getJsonArray("vragen").toString());
			for (JsonValue i : o.getJsonArray("vragen")) {
				JsonObject oo = (JsonObject) i;
				EvenementEnquete eetemp = new EvenementEnquete();
				Vraag v = vs.getVraagById(oo.getInt("id"));
				eetemp.setGebruiker(gebruiker);
				eetemp.setEvenement(evenement);
				eetemp.setVraag(v);
				eetemp.setAntwoord(oo.getString("antwoord"));
				ee.add(eetemp);
			}
			evenement.setEvenementEnquete(ee);
			return evenement;
		}
		
	
}

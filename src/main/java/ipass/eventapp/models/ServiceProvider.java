package ipass.eventapp.models;

import ipass.eventapp.persistence.*;

public class ServiceProvider {
	private static EnqueteService enqueteService = new EnqueteService();
	private static EnqueteDAO edao = new EnqueteDAO();
	
	private static EvenementService evenementService = new EvenementService();
	private static EvenementDAO evdao = new EvenementDAO();
	
	private static GebruikerService gebruikerService = new GebruikerService();
	private static GebruikerDAO gdao = new GebruikerDAO();
	
	private static VraagService vraagService = new VraagService();
	private static VraagDAO vdao = new VraagDAO();

	public static EnqueteService getEnqueteService() {
		enqueteService.setEnqueteDAO(edao);
		return enqueteService;
	}
	
	public static EvenementService getEvenementService() {
		evenementService.setEvenementDAO(evdao);
		return evenementService;
	}
	
	public static GebruikerService getGebruikerService() {
		gebruikerService.setGebruikerDAO(gdao);
		return gebruikerService;
	}
	
	public static VraagService getVraagService() {
		vraagService.setVraagDAO(vdao);
		return vraagService;
	}
	
}
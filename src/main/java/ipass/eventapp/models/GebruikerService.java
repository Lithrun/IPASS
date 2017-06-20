package ipass.eventapp.models;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import ipass.eventapp.entity.Enquete;
import ipass.eventapp.entity.Gebruiker;
import ipass.eventapp.entity.GebruikerEvenement;
import ipass.eventapp.persistence.EnqueteDAO;
import ipass.eventapp.persistence.GebruikerDAO;

public class GebruikerService {
	
	public static void main (String[] args) {
		GebruikerService service = new GebruikerService();
		GebruikerDAO gdao = new GebruikerDAO();
		service.setGebruikerDAO(gdao);
		service.updateGebruikerEvenementStatus(2, 1, "ingeschreven");
	}
	
	private GebruikerDAO gdao;

	public void setGebruikerDAO(GebruikerDAO gdao) {
		this.gdao = gdao;
	}
		
	public List<Gebruiker> getAllGebruikers(boolean evenementen) {
		Session session = gdao.getNewSession();
		Query query = session.createQuery("FROM Gebruiker");
		List<Gebruiker> gebruikers = gdao.selectGebruikers(session, query, evenementen);		
		return gebruikers;
	}
	
	public Gebruiker getGebruikerById(int id, boolean evenementen) {
		Session session = gdao.getNewSession();
		Query query = session.createQuery("FROM Gebruiker WHERE id=:id");
		query.setParameter("id", id);
		List<Gebruiker> gebruiker = gdao.selectGebruikers(session,query,evenementen);
		if (gebruiker == null ) {
			return null;
		}
		else {
			return gebruiker.get(0);
		}
	}
	
	public String getRoleByEmailAndWachtwoord(String emailadres, String wachtwoord) {
		Session session = gdao.getNewSession();
		Query query = session.createQuery("From Gebruiker WHERE emailadres=:emailadres AND wachtwoord=:wachtwoord");
		query.setParameter("emailadres", emailadres);
		query.setParameter("wachtwoord", wachtwoord);
		List<Gebruiker> gebruiker = gdao.selectGebruikers(session,query,false);
		if (gebruiker == null ) {
			return null;
		}
		else {
			return gebruiker.get(0).getType();
		}
	}
	
	public int getIdByEmailAndWachtwoord(String emailadres, String wachtwoord) {
		Session session = gdao.getNewSession();
		Query query = session.createQuery("From Gebruiker WHERE emailadres=:emailadres AND wachtwoord=:wachtwoord");
		query.setParameter("emailadres", emailadres);
		query.setParameter("wachtwoord", wachtwoord);
		List<Gebruiker> gebruiker = gdao.selectGebruikers(session,query,false);
		if (gebruiker == null ) {
			return 0;
		}
		else {
			return gebruiker.get(0).getId();
		}
	}
	
	public boolean updateGebruikerEvenementStatus(int gebruikerid, int evenementid, String status){
		Session session = gdao.getNewSession();
		Gebruiker gebruiker = getGebruikerById(gebruikerid, true);
		if (gebruiker == null) {
			return false;
		}
		for (GebruikerEvenement ge : gebruiker.getGebruikerEvenementen()) {
			if (evenementid == ge.getEvenement().getId()) {
				ge.setStatus(status);
			}
		}
		return gdao.updateGebruiker(session, gebruiker);
		
		
	}

}

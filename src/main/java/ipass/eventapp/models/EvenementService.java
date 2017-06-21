package ipass.eventapp.models;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import ipass.eventapp.entity.Evenement;
import ipass.eventapp.entity.EvenementEnquete;
import ipass.eventapp.entity.Gebruiker;
import ipass.eventapp.entity.GebruikerEvenement;
import ipass.eventapp.persistence.EvenementDAO;
import ipass.eventapp.persistence.GebruikerDAO;

public class EvenementService {
	
	private EvenementDAO edao;

	public void setEvenementDAO(EvenementDAO edao) {
		this.edao = edao;
	}
		
	public List<Evenement> getAllEvenementen(boolean enquete, boolean gebruikers, boolean enqueteantwoorden) {
		Session session = edao.getNewSession();
		Query query = session.createQuery("FROM Evenement");
		return edao.selectEvenementen(session, query,enquete, gebruikers, enqueteantwoorden);
	}
	
	public Evenement getEvenementById(int id, boolean enquete, boolean gebruikers, boolean enqueteantwoorden) {
		Session session = edao.getNewSession();
		Query query = session.createQuery("FROM Evenement WHERE id=:id");
		query.setParameter("id", id);
		List<Evenement> evenement = edao.selectEvenementen(session,query,enquete, gebruikers, enqueteantwoorden);
		if ( evenement == null ) {
			return null;
		}
		else {
			return evenement.get(0);
		}
	}
	
	public boolean updateEvenementStatus(int id, String status){
		Session session = edao.getNewSession();
		Evenement evenement = getEvenementById(id, true,true,true);
		if (evenement == null) {
			return false;
		}
		evenement.setStatus(status);
		return edao.updateEvenement(session, evenement);	
	}


	public boolean addEvenement(Evenement evenement){
		Session session = edao.getNewSession();
		return edao.addEvenement(session, evenement);
		
	}
	
	public boolean updateEvenement(Evenement evenement) {
		Session session = edao.getNewSession();
		Evenement oldevenement = getEvenementById(evenement.getId(),false,true,false);
		List<GebruikerEvenement> oldge = oldevenement.getGebruikerEvenementen(); // 3 4 5
		List<GebruikerEvenement> ge = evenement.getGebruikerEvenementen(); // 3 4 8 9
		int i = 0;	
		for (GebruikerEvenement g : oldge) {
			
			for (GebruikerEvenement gnew : ge) {
				if (g.getGebruiker().getId() == gnew.getGebruiker().getId()) {
					i = 1;
					break;
				}
			}
			
			if (i == 0) {
				System.out.println("Delete this");
				deleteGebruikerEvenement(g);
			}
			i = 0;
			
		}
		
		
		return edao.updateEvenement(session,  evenement);
	}
	
	public void deleteGebruikerEvenement(GebruikerEvenement ge) {
		Session session = edao.getNewSession();
		edao.deleteGebruikerEvenement(session, ge);
	}
	
}
package ipass.eventapp.models;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import ipass.eventapp.entity.Enquete;
import ipass.eventapp.persistence.EnqueteDAO;

public class EnqueteService {
	private EnqueteDAO edao;

	public void setEnqueteDAO(EnqueteDAO edao) {
		this.edao = edao;
	}
			
	public List<Enquete> getAllEnquetes(boolean vragen) {
		Session session = edao.getNewSession();
		Query query = session.createQuery("FROM Enquete");
		return edao.selectEnquetes(session, query,vragen);
	}
	
	public Enquete getEnqueteById(int id, boolean vragen) {
		Session session = edao.getNewSession();
		Query query = session.createQuery("FROM Enquete WHERE id=:id");
		query.setParameter("id", id);
		List<Enquete> enquete = edao.selectEnquetes(session,query,vragen);
		if (enquete == null) {
			return null;
		} else {
			return enquete.get(0);
		}
	}
		
	
	

}

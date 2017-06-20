package ipass.eventapp.models;


import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import ipass.eventapp.entity.Enquete;
import ipass.eventapp.entity.Vraag;
import ipass.eventapp.persistence.VraagDAO;

public class VraagService {
	
	private VraagDAO vdao;
	
	public void setVraagDAO(VraagDAO vdao){
		this.vdao = vdao;
	}
	
	public List<Vraag> getAllVragen() {
		Session session = vdao.getNewSession();
		Query query = session.createQuery("FROM Vraag");
		return vdao.selectVragen(session, query);
	}
	
	public Vraag getVraagById(int id) {
		Session session = vdao.getNewSession();
		Query query = session.createQuery("FROM Vraag WHERE id=:id");
		query.setParameter("id", id);
		List<Vraag> vraag = vdao.selectVragen(session,query);
		if (vraag == null) {
			return null;
		} else {
			return vraag.get(0);
		}
	}

}

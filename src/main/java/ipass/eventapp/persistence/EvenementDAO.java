package ipass.eventapp.persistence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import ipass.eventapp.entity.Enquete;
import ipass.eventapp.entity.Evenement;
import ipass.eventapp.entity.EvenementEnquete;
import ipass.eventapp.entity.Gebruiker;
import ipass.eventapp.entity.GebruikerEvenement;
import ipass.eventapp.entity.Vraag;

public class EvenementDAO extends BaseDAO {
	
	public Session getNewSession(){
		return super.getConnection().openSession();
	}
	
	public List<Evenement> selectEvenementen (Session session, Query query, boolean enquete, boolean gebruikers, boolean enqueteantwoorden) {
		
		 Transaction tx = null;
		 List<Evenement> evenementen = new ArrayList<Evenement>();
	     try{
	    	 tx = session.beginTransaction();
	    	 List results = query.list();
	    	 for (Iterator iterator = results.iterator(); iterator.hasNext();) {
	    		 Evenement evenement = (Evenement) iterator.next();
	    		 if (enquete) {
	    			 evenement.getEnquete();
	    		 }
	    		 if (gebruikers) {
	    			 for (GebruikerEvenement ge : evenement.getGebruikerEvenementen()){}
	    		 }
	    		 if (enqueteantwoorden) {
	    			 for (EvenementEnquete ee : evenement.getEvenementEnquete()){}
	    		 }
	    		 evenementen.add(evenement);
	    	 }
	    	 tx.commit();
	     }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	         session.close();
	         return null;
	     }
	     
	     session.close(); 
	     if (evenementen.isEmpty()) {
	    	 return null;
	     }
	     
	     return evenementen;
	    	 
	}
	
	public boolean updateEvenement (Session session, Evenement evenement) {
		 Transaction tx = null;
	     try{
	    	 tx = session.beginTransaction();
	    	 session.update(evenement);
	    	 tx.commit();
	     }catch (HibernateException | NullPointerException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	         session.close();
	         return false;
	     }
	     session.close();
	           
	     return true;
	}
	
	public boolean addEvenement (Session session, Evenement evenement) {
		 Transaction tx = null;
	     try{
	    	 tx = session.beginTransaction();
	    	 session.save(evenement);
	    	 tx.commit();
	     }catch (HibernateException | NullPointerException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	         session.close();
	         return false;
	     }
	     session.close();
	           
	     return true;
	}
	
	public boolean deleteGebruikerEvenement (Session session, GebruikerEvenement ge) {
		 Transaction tx = null;
	     try{
	    	 tx = session.beginTransaction();
	    	 session.remove(ge);
	    	 tx.commit();
	     }catch (HibernateException | NullPointerException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	         session.close();
	         return false;
	     }
	     session.close();
	           
	     return true;
	}

}

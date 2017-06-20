package ipass.eventapp.persistence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import ipass.eventapp.entity.Gebruiker;
import ipass.eventapp.entity.GebruikerEvenement;

public class GebruikerDAO extends BaseDAO{
	
	public Session getNewSession(){
		return super.getConnection().openSession();
	}

	public List<Gebruiker> selectGebruikers (Session session, Query query, boolean gebruikerevenementen) {
		
		 Transaction tx = null;
		 List<Gebruiker> gebruikers = new ArrayList<Gebruiker>();
	     try{
	    	 tx = session.beginTransaction();
	    	 List results = query.list();
	    	 for (Iterator iterator = results.iterator(); iterator.hasNext();) {
	    		 Gebruiker gebruiker = (Gebruiker) iterator.next();
	    		 if (gebruikerevenementen == true) {
	    			 for (GebruikerEvenement ge : gebruiker.getGebruikerEvenementen() ) {
	    				 //
	    			}
	    		 }
	    		 gebruikers.add(gebruiker);
	    	 }
	    	 tx.commit();
	     }catch (HibernateException | NullPointerException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	         session.close();
	         return null;
	     }
	     session.close();
	     
	     if (gebruikers.isEmpty()) {
	    	 return null;
	     }
	      
	     return gebruikers;
	    	 
	}
	
	public boolean updateGebruiker (Session session, Gebruiker gebruiker) {
		 Transaction tx = null;
	     try{
	    	 tx = session.beginTransaction();
	    	 session.update(gebruiker);
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

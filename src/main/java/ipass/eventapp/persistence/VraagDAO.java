package ipass.eventapp.persistence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import ipass.eventapp.entity.*;

public class VraagDAO extends BaseDAO {

	public Session getNewSession(){
		return super.getConnection().openSession();
	}

	public List<Vraag> selectVragen (Session session, Query query) {
		
		 Transaction tx = null;
		 List<Vraag> vragen = new ArrayList<Vraag>();
	     try{
	    	 tx = session.beginTransaction();
	    	 List results = query.list();
	    	 for (Iterator iterator = results.iterator(); iterator.hasNext();) {
	    		 Vraag vraag = (Vraag) iterator.next();
	    		 vragen.add(vraag);
	    	 }
	    	 tx.commit();
	     }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	         session.close();
	         return null;
	     }
	     session.close(); 
	     
	     if (vragen.isEmpty()) {
	    	 return null;
	     }
	     
	     return vragen;
	    	 
	}
	
	// OUDE METHODE
//	public void findAll (){
//		
//		 Session session = super.getConnection().openSession();
//		 Transaction tx = null;
//		 
//	     try{
//	    	 System.out.println("Executing function");
//	    	 tx = session.beginTransaction();
//	    	 List vragen = session.createQuery("FROM Vraag").list();
//	    	 System.out.println(vragen);
//	    	 for (Iterator iterator = vragen.iterator(); iterator.hasNext();) {
//	    		 Vraag vraag = (Vraag) iterator.next();
//	    		 System.out.println(vraag.getVraag());
//	    		 System.out.println(vraag.getId());
//	    		 for (Enquete e : vraag.getEnqueteList()) {
//	    			 System.out.println("Enquete naam: " + e.getTitel());
//	    			 System.out.println("Enquete datum: " + e.getEinddatum());
//	    		 }
//	    	 }
//	    	 tx.commit();
//	     }catch (HibernateException e) {
//	    	 System.out.println("Error");
//	         if (tx!=null) tx.rollback();
//	         e.printStackTrace(); 
//	      }finally {
//	         session.close(); 
//	      }
//	    	 
//	     }
		
	}
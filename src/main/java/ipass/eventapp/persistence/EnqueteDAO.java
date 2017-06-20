package ipass.eventapp.persistence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import ipass.eventapp.entity.Enquete;
import ipass.eventapp.entity.Vraag;

public class EnqueteDAO extends BaseDAO {
	
	public Session getNewSession(){
		return super.getConnection().openSession();
	}
	
	public List<Enquete> selectEnquetes (Session session, Query query, boolean vragen) {
		
		 Transaction tx = null;
		 List<Enquete> enquetes = new ArrayList<Enquete>();
	     try{
	    	 tx = session.beginTransaction();
	    	 List results = query.list();
	    	 for (Iterator iterator = results.iterator(); iterator.hasNext();) {
	    		 Enquete enquete = (Enquete) iterator.next();
	    		 if (vragen) {
	    			 for (Vraag v : enquete.getVraagList()) {}
	    		 }
	    		 enquetes.add(enquete);
	    	 }
	    	 tx.commit();
	     }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace();
	         session.close();
	         return null;
	     }
	     
	     session.close(); 
	     
	     if (enquetes.isEmpty()) {
	    	 return null;
	     }
	     return enquetes;
	    	 
	}
		
//OUDE METHODE, VERVANGEN DOOR selectEnquetes	
//	public List<Enquete> findAll (){
//		
//		 Session session = super.getConnection().openSession();
//		 Transaction tx = null;
//		 List<Enquete> enquetes = new ArrayList<Enquete>();
//	     try{
//	    	 System.out.println("Executing function");
//	    	 tx = session.beginTransaction();
//	    	 List results = session.createQuery("FROM Enquete").list();
//	    	 System.out.println(results);
//	    	 for (Iterator iterator = results.iterator(); iterator.hasNext();) {
//	    		 Enquete enquete = (Enquete) iterator.next();
//	    		 enquete.setVraagList(enquete.getVraagList());
//	    		 enquetes.add(enquete);
////	    		 System.out.println(enquete.getTitel());
////	    		 System.out.println(enquete.getEinddatum());
////	    		 for (Vraag vraag : enquete.getVraagList()){
////	    			 System.out.println("Vraag: " + vraag.getVraag());
////	    		 }
//	    	 }
//	    	 tx.commit();
//	     }catch (HibernateException e) {
//	    	 System.out.println("Error");
//	         if (tx!=null) tx.rollback();
//	         e.printStackTrace(); 
//	     }
//	     
//	     session.close(); 
//	     return enquetes;
//	    	 
//	     }
	
	}

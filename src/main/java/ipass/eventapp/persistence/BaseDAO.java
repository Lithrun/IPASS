package ipass.eventapp.persistence;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class BaseDAO {
	private SessionFactory factory = new Configuration().setProperty("hibernate.connection.url", System.getenv("DATABASE_URL")).configure().buildSessionFactory();
	
	protected final SessionFactory getConnection(){
		return factory ;
	}
	
}
package ipass.eventapp.persistence;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class BaseDAO {
	final Configuration cfg = new Configuration().setProperty("hibernate.connection.url", System.getenv("DATABASE_URL"));
	private SessionFactory factory = cfg.configure().buildSessionFactory();
	
	protected final SessionFactory getConnection(){
		return factory ;
	}
	
}
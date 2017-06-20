package ipass.eventapp.persistence;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class BaseDAO {
	
	private Configuration factory = new Configuration().configure();
	
	protected final SessionFactory getConnection(){
		System.out.println(System.getenv("DATABASE_URL"));
		factory.setProperty("hibernate.connection.url", System.getenv("DATABASE_URL"));
		return factory.buildSessionFactory() ;
	}
	
}
package co.com.soinsoftware.billing.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * @author Carlos Rodriguez
 * @since 08/06/2016
 * @version 1.0
 */
public class SessionController {
	
	private static SessionController instance;
	
	private SessionFactory factory;
	
	private Session session;
	
	public static SessionController getInstance() {
		if (instance == null) {
			instance = new SessionController();
		}
		return instance;
	}
	
	public Session openSession() {
		final SessionFactory sessionFactory = this.getSessionFactory();
		if (this.session == null || !this.session.isOpen()) {
			this.session = sessionFactory.openSession();
		}
		final Transaction transaction = this.session.getTransaction();
		if (!transaction.isActive()) {
			transaction.begin();
		}
		return this.session;
	}
	
	private SessionController() {
		super();
		this.getSessionFactory();
	}

	@SuppressWarnings("deprecation")
	private SessionFactory getSessionFactory() {
		if (this.factory == null) {
			this.factory = new Configuration().configure()
					.buildSessionFactory();
		}
		return factory;
	}
}
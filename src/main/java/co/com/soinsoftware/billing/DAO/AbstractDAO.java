package co.com.soinsoftware.billing.DAO;

import java.io.Serializable;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * @author Carlos Rodriguez
 * @since 26/05/2016
 * @version 1.0
 */
public abstract class AbstractDAO {

	private SessionFactory factory;

	public Query createQuery(final String queryStatement) {
		return this.openSession().createQuery(queryStatement.toString());
	}

	public void save(final Serializable object, final boolean isNew) {
		Session session = this.openSession();
		if (isNew) {
			session.save(object);
		} else {
			session.update(object);
		}
		session.getTransaction().commit();
	}

	private Session openSession() {
		final Session session = this.getSessionFactory().openSession();
		session.beginTransaction();
		return session;
	}

	@SuppressWarnings("deprecation")
	private SessionFactory getSessionFactory() {
		if (this.factory == null) {
			this.factory = new Configuration().configure()
					.buildSessionFactory();
		}
		return factory;
	}
	
	protected abstract String getSelectStatement();
}
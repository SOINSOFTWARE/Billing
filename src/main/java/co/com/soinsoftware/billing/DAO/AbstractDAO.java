package co.com.soinsoftware.billing.dao;

import java.io.Serializable;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * @author Carlos Rodriguez
 * @since 26/05/2016
 * @version 1.0
 */
public abstract class AbstractDAO {

	protected static final String TABLE_CONFIGURATION = "Configuration";
	protected static final String TABLE_RECEIPT = "Receipt";
	protected static final String TABLE_USER = "User";

	protected static final String SQL_AND = " and ";
	protected static final String SQL_EQUALS_WITH_PARAM = " = :";
	protected static final String SQL_FROM = " from ";
	protected static final String SQL_WHERE = " where ";

	private SessionFactory factory;
	
	private Session session;

	public Query createQuery(String queryStatement) {
		final Session session = this.openSession();
		return session.createQuery(queryStatement.toString());
	}

	public void save(Serializable object, boolean isNew) {
		final Session session = this.openSession();
		if (isNew) {
			session.save(object);
		}
		session.getTransaction().commit();
	}

	private Session openSession() {
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
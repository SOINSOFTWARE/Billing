package co.com.soinsoftware.billing.dao;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import co.com.soinsoftware.billing.entity.User;

/**
 * @author Carlos Rodriguez
 * @since 26/05/2016
 * @version 1.0
 */
public class UserDAO extends AbstractDAO {

	public User select(final String login, final String password) {
		User user = null;
		try {
			final Query query = this
					.createQuery(this.getSelectStatementLogin());
			query.setParameter("login", login);
			query.setParameter("password", password);
			user = (query.list().isEmpty()) ? null : (User) query.list().get(0);
		} catch (HibernateException ex) {
			System.out.println(ex);
		}
		return user;
	}

	public User select(final long identification) {
		User user = null;
		try {
			final Query query = this.createQuery(this
					.getSelectStatementIdentification());
			query.setParameter("identification", identification);
			user = (query.list().isEmpty()) ? null : (User) query.list().get(0);
		} catch (HibernateException ex) {
			System.out.println(ex);
		}
		return user;
	}
	
	public void save(final User user) {
		boolean isNew = (user.getId() == null) ? true : false;
		this.save(user, isNew);
	}

	@Override
	protected String getSelectStatement() {
		final StringBuilder query = new StringBuilder();
		query.append(" FROM User ");
		return query.toString();
	}

	private String getSelectStatementLogin() {
		final StringBuilder query = new StringBuilder(this.getSelectStatement());
		query.append(" WHERE login = :login AND password = :password ");
		return query.toString();
	}

	private String getSelectStatementIdentification() {
		final StringBuilder query = new StringBuilder(this.getSelectStatement());
		query.append(" WHERE identification = :identification ");
		return query.toString();
	}
}
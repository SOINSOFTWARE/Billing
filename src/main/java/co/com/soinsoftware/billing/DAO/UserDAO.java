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
	
	private static final String COLUMN_ID = "identification";
	private static final String COLUMN_LOGIN = "login";
	private static final String COLUMN_PASS = "password";

	public User select(final String login, final String password) {
		User user = null;
		try {
			final Query query = this
					.createQuery(this.getSelectStatementLogin());
			query.setParameter(COLUMN_LOGIN, login);
			query.setParameter(COLUMN_PASS, password);
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
			query.setParameter(COLUMN_ID, identification);
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
		query.append(SQL_FROM);
		query.append(TABLE_USER);
		return query.toString();
	}

	private String getSelectStatementLogin() {
		final StringBuilder query = new StringBuilder(this.getSelectStatement());
		query.append(SQL_WHERE);
		query.append(COLUMN_LOGIN);
		query.append(SQL_EQUALS_WITH_PARAM);
		query.append(COLUMN_LOGIN);
		query.append(SQL_AND);
		query.append(COLUMN_PASS);
		query.append(SQL_EQUALS_WITH_PARAM);
		query.append(COLUMN_PASS);
		return query.toString();
	}

	private String getSelectStatementIdentification() {
		final StringBuilder query = new StringBuilder(this.getSelectStatement());
		query.append(SQL_WHERE);
		query.append(COLUMN_ID);
		query.append(SQL_EQUALS_WITH_PARAM);
		query.append(COLUMN_ID);
		return query.toString();
	}
}
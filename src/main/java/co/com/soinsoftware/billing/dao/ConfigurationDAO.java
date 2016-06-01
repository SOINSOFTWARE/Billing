package co.com.soinsoftware.billing.dao;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import co.com.soinsoftware.billing.entity.Configuration;

/**
 * @author Carlos Rodriguez
 * @since 31/05/2016
 * @version 1.0
 */
public class ConfigurationDAO extends AbstractDAO {

	private static final String COLUMN_ID_COMPANY = "idcompany";

	public Configuration select(final int idCompany) {
		Configuration config = null;
		try {
			final Query query = this.createQuery(this
					.getSelectStatementCompany());
			query.setParameter(COLUMN_ID_COMPANY, idCompany);
			config = (query.list().isEmpty()) ? null : (Configuration) query
					.list().get(0);
		} catch (HibernateException ex) {
			System.out.println(ex);
		}
		return config;
	}

	public void save(final Configuration configuration) {
		boolean isNew = (configuration.getId() == null) ? true : false;
		this.save(configuration, isNew);
	}

	@Override
	protected String getSelectStatement() {
		final StringBuilder query = new StringBuilder();
		query.append(SQL_FROM);
		query.append(TABLE_CONFIGURATION);
		return query.toString();
	}

	private String getSelectStatementCompany() {
		final StringBuilder query = new StringBuilder(this.getSelectStatement());
		query.append(SQL_WHERE);
		query.append(COLUMN_ID_COMPANY);
		query.append(SQL_EQUALS_WITH_PARAM);
		query.append(COLUMN_ID_COMPANY);
		return query.toString();
	}
}

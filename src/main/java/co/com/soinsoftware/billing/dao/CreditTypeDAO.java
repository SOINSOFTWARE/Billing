package co.com.soinsoftware.billing.dao;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import co.com.soinsoftware.billing.entity.Credittype;

/**
 * @author Carlos Rodriguez
 * @since 05/07/2016
 * @version 1.0
 */
public class CreditTypeDAO extends AbstractDAO {

	@SuppressWarnings("unchecked")
	public Set<Credittype> select() {
		Set<Credittype> creditTypeSet = null;
		try {
			final Query query = this.createQuery(this
					.getSelectStatementEnabled());
			creditTypeSet = (query.list().isEmpty()) ? null
					: new HashSet<Credittype>(query.list());
		} catch (HibernateException ex) {
			System.out.println(ex);
		}
		return creditTypeSet;
	}

	public void save(final Credittype creditType) {
		boolean isNew = (creditType.getId() == null) ? true : false;
		this.save(creditType, isNew);
	}

	@Override
	protected String getSelectStatement() {
		final StringBuilder query = new StringBuilder();
		query.append(SQL_FROM);
		query.append(TABLE_CREDITTYPE);
		return query.toString();
	}

	private String getSelectStatementEnabled() {
		final StringBuilder query = new StringBuilder(this.getSelectStatement());
		query.append(SQL_WHERE);
		query.append(COLUMN_ENABLED);
		query.append(" = 1 ");
		return query.toString();
	}
}

package co.com.soinsoftware.billing.dao;

import java.io.Serializable;

import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author Carlos Rodriguez
 * @since 26/05/2016
 * @version 1.0
 */
public abstract class AbstractDAO {
	
	protected static final String COLUMN_ENABLED = "enabled";

	protected static final String TABLE_CONFIGURATION = "Configuration";
	protected static final String TABLE_CREDITTYPE = "Credittype";
	protected static final String TABLE_RECEIPT = "Receipt";
	protected static final String TABLE_USER = "User";

	protected static final String SQL_AND = " and ";
	protected static final String SQL_EQUALS_WITH_PARAM = " = :";
	protected static final String SQL_FROM = " from ";
	protected static final String SQL_MONTH_FUNC = " month ";
	protected static final String SQL_SELECT = " select ";
	protected static final String SQL_WHERE = " where ";
	protected static final String SQL_YEAR_FUNC = " year ";

	public Query createQuery(final String queryStatement) {
		final SessionController controller = SessionController.getInstance();
		final Session session = controller.openSession();
		return session.createQuery(queryStatement);
	}

	public void save(final Serializable object, final boolean isNew) {
		final SessionController controller = SessionController.getInstance();
		final Session session = controller.openSession();
		if (isNew) {
			session.save(object);
		}
		session.getTransaction().commit();
	}

	protected abstract String getSelectStatement();
}
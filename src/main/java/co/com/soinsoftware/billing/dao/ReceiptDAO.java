package co.com.soinsoftware.billing.dao;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import co.com.soinsoftware.billing.entity.Receipt;

/**
 * @author Carlos Rodriguez
 * @since 01/06/2016
 * @version 1.0
 */
public class ReceiptDAO extends AbstractDAO {

	private static final String COLUMN_DATE = "receiptdate";
	private static final String COLUMN_ID_USER = "iduser";
	private static final String COLUMN_NUMBER = "number";
	private static final String PARAM_YEAR = "year";
	private static final String PARAM_MONTH = "month";

	public Receipt select(final long number) {
		Receipt receipt = null;
		try {
			final Query query = this.createQuery(this
					.getSelectStatementNumber());
			query.setParameter(COLUMN_NUMBER, number);
			receipt = (query.list().isEmpty()) ? null : (Receipt) query.list()
					.get(0);
		} catch (HibernateException ex) {
			System.out.println(ex);
		}
		return receipt;
	}

	@SuppressWarnings("unchecked")
	public Set<Receipt> select(final int year, final int month,
			final boolean enabled) {
		Set<Receipt> receiptSet = new HashSet<>();
		try {
			final Query query = this.createQuery(this
					.getSelectStatementDate(enabled));
			query.setParameter(PARAM_YEAR, year);
			query.setParameter(PARAM_MONTH, month);
			receiptSet = new HashSet<>(query.list());
		} catch (HibernateException ex) {
			System.out.println(ex);
		}
		return receiptSet;
	}

	@SuppressWarnings("unchecked")
	public Set<Receipt> select(final int year, final int month,
			final int idUser, final boolean enabled) {
		Set<Receipt> receiptSet = new HashSet<>();
		try {
			final Query query = this.createQuery(this
					.getSelectStatementDateAndUser(enabled));
			query.setParameter(PARAM_YEAR, year);
			query.setParameter(PARAM_MONTH, month);
			query.setParameter(COLUMN_ID_USER, idUser);
			receiptSet = new HashSet<>(query.list());
		} catch (HibernateException ex) {
			System.out.println(ex);
		}
		return receiptSet;
	}

	public void save(final Receipt receipt) {
		boolean isNew = (receipt.getId() == null) ? true : false;
		this.save(receipt, isNew);
	}

	public BigDecimal selectTotal(final Integer idUser) {
		BigDecimal value = new BigDecimal(0);
		try {
			final Query query = this.createQuery(this
					.getSelectStatementTotalAmount(idUser));
			if (idUser != null) {
				query.setParameter(COLUMN_ID_USER, idUser);
			}
			value = (query.list().isEmpty()) ? null : (BigDecimal) query.list()
					.get(0);
		} catch (HibernateException ex) {
			System.out.println(ex);
		}
		return value;
	}

	public BigDecimal selectVoluntarySave(final Integer idUser) {
		BigDecimal value = new BigDecimal(0);
		try {
			final Query query = this.createQuery(this
					.getSelectStatementVoluntarySave(idUser));
			if (idUser != null) {
				query.setParameter(COLUMN_ID_USER, idUser);
			}
			value = (query.list().isEmpty()) ? null : (BigDecimal) query.list()
					.get(0);
		} catch (HibernateException ex) {
			System.out.println(ex);
		}
		return value;
	}

	@Override
	protected String getSelectStatement() {
		final StringBuilder query = new StringBuilder();
		query.append(SQL_FROM);
		query.append(TABLE_RECEIPT);
		return query.toString();
	}

	private String getSelectStatementEnabled(final boolean enabled) {
		final String enabledStr = (enabled) ? "1" : "0";
		final StringBuilder query = new StringBuilder(this.getSelectStatement());
		query.append(SQL_WHERE);
		query.append(COLUMN_ENABLED);
		query.append(" = ");
		query.append(enabledStr);
		return query.toString();
	}

	private String getSelectStatementDate(final boolean enabled) {
		final StringBuilder query = new StringBuilder(
				this.getSelectStatementEnabled(enabled));
		query.append(SQL_AND);
		query.append(SQL_YEAR_FUNC);
		query.append("(");
		query.append(COLUMN_DATE);
		query.append(")");
		query.append(SQL_EQUALS_WITH_PARAM);
		query.append(PARAM_YEAR);
		query.append(SQL_AND);
		query.append(SQL_MONTH_FUNC);
		query.append("(");
		query.append(COLUMN_DATE);
		query.append(")");
		query.append(SQL_EQUALS_WITH_PARAM);
		query.append(PARAM_MONTH);
		return query.toString();
	}

	private String getSelectStatementDateAndUser(final boolean enabled) {
		final StringBuilder query = new StringBuilder(
				this.getSelectStatementDate(enabled));
		query.append(SQL_AND);
		query.append(COLUMN_ID_USER);
		query.append(SQL_EQUALS_WITH_PARAM);
		query.append(COLUMN_ID_USER);
		return query.toString();
	}

	private String getSelectStatementNumber() {
		final StringBuilder query = new StringBuilder(this.getSelectStatement());
		query.append(SQL_WHERE);
		query.append(COLUMN_NUMBER);
		query.append(SQL_EQUALS_WITH_PARAM);
		query.append(COLUMN_NUMBER);
		return query.toString();
	}

	private String getSelectStatementTotalAmount(final Integer idUser) {
		final StringBuilder query = new StringBuilder();
		query.append(SQL_SELECT);
		query.append("sum(itm.value) as val");
		query.append(SQL_FROM);
		query.append("Receipt as rcp, Item as itm");
		query.append(SQL_WHERE);
		query.append("rcp.id = itm.id.idreceipt");
		query.append(SQL_AND);
		query.append("rcp.enabled = 1");
		if (idUser != null) {
			query.append(SQL_AND);
			query.append("rcp.userByIduser.id");
			query.append(SQL_EQUALS_WITH_PARAM);
			query.append(COLUMN_ID_USER);
		}

		return query.toString();
	}

	private String getSelectStatementVoluntarySave(final Integer idUser) {
		final StringBuilder query = new StringBuilder();
		query.append(SQL_SELECT);
		query.append("sum(itm.value) as val");
		query.append(SQL_FROM);
		query.append("Receipt as rcp, Item as itm");
		query.append(SQL_WHERE);
		query.append("rcp.id = itm.id.idreceipt");
		query.append(SQL_AND);
		query.append("itm.id.iditemconcept = 1");
		query.append(SQL_AND);
		query.append("rcp.enabled = 1");
		if (idUser != null) {
			query.append(SQL_AND);
			query.append("rcp.userByIduser.id");
			query.append(SQL_EQUALS_WITH_PARAM);
			query.append(COLUMN_ID_USER);
		}

		return query.toString();
	}
}
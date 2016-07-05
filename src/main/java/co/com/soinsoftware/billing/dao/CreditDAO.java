package co.com.soinsoftware.billing.dao;

import co.com.soinsoftware.billing.entity.Credit;

/**
 * @author Carlos Rodriguez
 * @since 05/07/2016
 * @version 1.0
 */
public class CreditDAO extends AbstractDAO {

	public void save(final Credit credit) {
		this.save(credit, true);
	}

	@Override
	protected String getSelectStatement() {
		// TODO Auto-generated method stub
		return null;
	}
}
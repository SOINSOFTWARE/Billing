package co.com.soinsoftware.billing.dao;

import co.com.soinsoftware.billing.entity.Item;

/**
 * @author Carlos Rodriguez
 * @since 01/06/2016
 * @version 1.0
 */
public class ItemDAO extends AbstractDAO {

	public void save(final Item item) {
		this.save(item, true);
	}

	@Override
	protected String getSelectStatement() {
		// TODO Auto-generated method stub
		return null;
	}
}
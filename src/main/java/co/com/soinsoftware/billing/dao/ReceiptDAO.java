package co.com.soinsoftware.billing.dao;

import co.com.soinsoftware.billing.entity.Receipt;

/**
 * @author Carlos Rodriguez
 * @since 01/06/2016
 * @version 1.0
 */
public class ReceiptDAO extends AbstractDAO {
	
	public void save(final Receipt receipt) {
		boolean isNew = (receipt.getId() == null) ? true : false;
		this.save(receipt, isNew);
	}

	@Override
	protected String getSelectStatement() {
		// TODO Auto-generated method stub
		return null;
	}
}
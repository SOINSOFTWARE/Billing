package co.com.soinsoftware.billing.dao;

import co.com.soinsoftware.billing.entity.Itemconcept;

/**
 * @author Carlos Rodriguez
 * @since 15/06/2016
 * @version 1.0
 */
public class ItemConceptDAO extends AbstractDAO {

	public void save(final Itemconcept itemConcept) {
		boolean isNew = (itemConcept.getId() == null) ? true : false;
		this.save(itemConcept, isNew);
	}

	@Override
	protected String getSelectStatement() {
		// TODO Auto-generated method stub
		return null;
	}
}
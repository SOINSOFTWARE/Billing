package co.com.soinsoftware.billing.bll;

import co.com.soinsoftware.billing.dao.ItemConceptDAO;
import co.com.soinsoftware.billing.entity.Itemconcept;

/**
 * @author Carlos Rodriguez
 * @since 15/06/2016
 * @version 1.0
 */
public class ItemConceptBLL {

	private static ItemConceptBLL instance;

	private final ItemConceptDAO dao;

	public static ItemConceptBLL getInstance() {
		if (instance == null) {
			instance = new ItemConceptBLL();
		}
		return instance;
	}

	public void save(final Itemconcept itemConcept) {
		this.dao.save(itemConcept);
	}

	private ItemConceptBLL() {
		super();
		this.dao = new ItemConceptDAO();
	}
}
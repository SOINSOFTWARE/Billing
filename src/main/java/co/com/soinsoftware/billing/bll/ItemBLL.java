package co.com.soinsoftware.billing.bll;

import co.com.soinsoftware.billing.dao.ItemDAO;
import co.com.soinsoftware.billing.entity.Item;

/**
 * @author Carlos Rodriguez
 * @since 01/06/2016
 * @version 1.0
 */
public class ItemBLL {

	private static ItemBLL instance;

	private final ItemDAO dao;

	public static ItemBLL getInstance() {
		if (instance == null) {
			instance = new ItemBLL();
		}
		return instance;
	}

	public void save(final Item item) {
		this.dao.save(item);
	}

	private ItemBLL() {
		super();
		this.dao = new ItemDAO();
	}
}
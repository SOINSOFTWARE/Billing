package co.com.soinsoftware.billing.bll;

import co.com.soinsoftware.billing.dao.ReceiptDAO;
import co.com.soinsoftware.billing.entity.Receipt;

/**
 * @author Carlos Rodriguez
 * @since 01/06/2016
 * @version 1.0
 */
public class ReceiptBLL {

	private static ReceiptBLL instance;

	private final ReceiptDAO dao;

	public static ReceiptBLL getInstance() {
		if (instance == null) {
			instance = new ReceiptBLL();
		}
		return instance;
	}

	public void save(final Receipt receipt) {
		this.dao.save(receipt);
	}

	private ReceiptBLL() {
		super();
		this.dao = new ReceiptDAO();
	}
}
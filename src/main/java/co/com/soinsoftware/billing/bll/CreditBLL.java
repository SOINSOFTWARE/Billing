package co.com.soinsoftware.billing.bll;

import co.com.soinsoftware.billing.dao.CreditDAO;
import co.com.soinsoftware.billing.entity.Credit;

/**
 * @author Carlos Rodriguez
 * @since 01/06/2016
 * @version 1.0
 */
public class CreditBLL {

	private static CreditBLL instance;

	private final CreditDAO dao;

	public static CreditBLL getInstance() {
		if (instance == null) {
			instance = new CreditBLL();
		}
		return instance;
	}

	public void save(final Credit credit) {
		this.dao.save(credit);
	}

	private CreditBLL() {
		super();
		this.dao = new CreditDAO();
	}
}
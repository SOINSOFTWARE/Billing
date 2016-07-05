package co.com.soinsoftware.billing.bll;

import java.util.Set;

import co.com.soinsoftware.billing.dao.CreditTypeDAO;
import co.com.soinsoftware.billing.entity.Credittype;

/**
 * @author Carlos Rodriguez
 * @since 05/07/2016
 * @version 1.0
 */
public class CreditTypeBLL {

	private static CreditTypeBLL instance;

	private final CreditTypeDAO dao;

	public static CreditTypeBLL getInstance() {
		if (instance == null) {
			instance = new CreditTypeBLL();
		}
		return instance;
	}

	public Set<Credittype> select() {
		return this.dao.select();
	}

	private CreditTypeBLL() {
		super();
		this.dao = new CreditTypeDAO();
	}
}
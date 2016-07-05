package co.com.soinsoftware.billing.controller;

import java.util.Set;

import co.com.soinsoftware.billing.bll.CreditBLL;
import co.com.soinsoftware.billing.bll.CreditTypeBLL;
import co.com.soinsoftware.billing.entity.Credit;
import co.com.soinsoftware.billing.entity.Credittype;

/**
 * @author Carlos Rodriguez
 * @since 05/07/2016
 * @version 1.0
 */
public class CreditController {
	
	private final CreditTypeBLL creditTypeBLL;
	
	private final CreditBLL creditBLL;

	public CreditController() {
		super();
		this.creditTypeBLL = CreditTypeBLL.getInstance();
		this.creditBLL = CreditBLL.getInstance();
	}
	
	public Set<Credittype> selectCreditType() {
		return this.creditTypeBLL.select();
	}
	
	public void saveCredit(final Credit credit) {
		this.creditBLL.save(credit);
	}
}
package co.com.soinsoftware.billing.controller;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
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
	
	public Object[][] buildCreditData(final Set<Credit> creditSet) {
		final Locale locale = new Locale("es", "CO");
        final NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
		final SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
		final List<Credit> creditList = new ArrayList<>(creditSet);
		Collections.sort(creditList);
		final int itemSize = creditList.size();
		final Object[][] data = new Object[itemSize][2];
		int index = 0;
		for (final Credit credit : creditList) {
			data[index][0] = format.format(credit.getCreation());
			data[index][1] = formatter.format(credit.getValue().doubleValue());
			index++;
		}
		return data;
	}
}
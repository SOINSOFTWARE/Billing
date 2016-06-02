package co.com.soinsoftware.billing.bll;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import co.com.soinsoftware.billing.dao.ReceiptDAO;
import co.com.soinsoftware.billing.entity.Receipt;
import co.com.soinsoftware.billing.entity.User;

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
	
	public Receipt select(final long number) {
		return this.dao.select(number);
	}
	
	public List<Receipt> select(final int year, final int month, final User client) {
		Set<Receipt> receiptSet = new HashSet<>();
		if (client == null) {
			receiptSet = this.dao.select(year, month);
		} else {
			receiptSet = this.dao.select(year, month, client.getId());
		}
		return this.sortedReceiptList(receiptSet);
	}

	public void save(final Receipt receipt) {
		this.dao.save(receipt);
	}

	private ReceiptBLL() {
		super();
		this.dao = new ReceiptDAO();
	}
	
	private List<Receipt> sortedReceiptList(final Set<Receipt> receiptSet) {
		List<Receipt> receiptList = new ArrayList<>();
		if (receiptSet != null) {
			receiptList = new ArrayList<>(receiptSet);
			Collections.sort(receiptList);
		}
		return receiptList;
	}
}
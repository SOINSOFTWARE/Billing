package co.com.soinsoftware.billing.bll;

import java.math.BigDecimal;
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
		final Receipt receipt = this.dao.select(number);
		if (receipt != null) {
			receipt.fillNonDbFields();
		}
		return receipt;
	}

	public List<Receipt> select(final int year, final int month,
			final User client, final boolean enabled) {
		Set<Receipt> receiptSet = new HashSet<>();
		if (client == null) {
			receiptSet = this.dao.select(year, month, enabled);
		} else {
			receiptSet = this.dao.select(year, month, client.getId(), enabled);
		}
		return this.sortedReceiptList(receiptSet);
	}

	public void save(final Receipt receipt) {
		this.dao.save(receipt);
	}

	public BigDecimal selectTotal(final User client) {
		final Integer idUser = (client == null) ? null : client.getId();
		return this.dao.selectTotal(idUser);
	}

	private ReceiptBLL() {
		super();
		this.dao = new ReceiptDAO();
	}

	private List<Receipt> sortedReceiptList(final Set<Receipt> receiptSet) {
		List<Receipt> receiptList = new ArrayList<>();
		if (receiptSet != null) {
			receiptList = new ArrayList<>(receiptSet);
			this.fillNonDbFields(receiptList);
			Collections.sort(receiptList);
		}
		return receiptList;
	}

	private void fillNonDbFields(final List<Receipt> receiptList) {
		for (final Receipt receipt : receiptList) {
			receipt.fillNonDbFields();
		}
	}
}
package co.com.soinsoftware.billing.controller;

import co.com.soinsoftware.billing.view.JFReportReceipt;
import co.com.soinsoftware.billing.view.JFReceipt;
import co.com.soinsoftware.billing.view.JFUser;

/**
 * @author Carlos Rodriguez
 * @since 30/05/2016
 * @version 1.0
 */
public class MenuController {

	private final JFReportReceipt reportFrame;

	private final JFUser userFrame;

	private final JFReceipt receiptFrame;

	public MenuController(final JFReportReceipt reportFrame, final JFUser userFrame,
			final JFReceipt receiptFrame) {
		super();
		this.reportFrame = reportFrame;
		this.userFrame = userFrame;
		this.receiptFrame = receiptFrame;
	}

	public JFReportReceipt getReportFrame() {
		return reportFrame;
	}

	public JFUser getUserFrame() {
		return userFrame;
	}

	public JFReceipt getReceiptFrame() {
		return receiptFrame;
	}
}
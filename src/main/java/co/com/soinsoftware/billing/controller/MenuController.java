package co.com.soinsoftware.billing.controller;

import co.com.soinsoftware.billing.view.JFReceiptDeactivation;
import co.com.soinsoftware.billing.view.JFReportReceipt;
import co.com.soinsoftware.billing.view.JFReceipt;
import co.com.soinsoftware.billing.view.JFUser;
import co.com.soinsoftware.billing.view.JFViewUser;

/**
 * @author Carlos Rodriguez
 * @since 30/05/2016
 * @version 1.0
 */
public class MenuController {

	private final JFReportReceipt reportFrame;

	private final JFUser userFrame;

	private final JFReceipt receiptFrame;

	private final JFViewUser viewUserFrame;

	private final JFReceiptDeactivation deactivateReceiptFrame;

	public MenuController(final JFReportReceipt reportFrame,
			final JFUser userFrame, final JFReceipt receiptFrame,
			final JFViewUser viewUserFrame,
			final JFReceiptDeactivation deactivateReceiptFrame) {
		super();
		this.reportFrame = reportFrame;
		this.userFrame = userFrame;
		this.receiptFrame = receiptFrame;
		this.viewUserFrame = viewUserFrame;
		this.deactivateReceiptFrame = deactivateReceiptFrame;
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

	public JFViewUser getViewUserFrame() {
		return viewUserFrame;
	}

	public JFReceiptDeactivation getDeactivateReceiptFrame() {
		return deactivateReceiptFrame;
	}
}
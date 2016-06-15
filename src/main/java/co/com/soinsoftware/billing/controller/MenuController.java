package co.com.soinsoftware.billing.controller;

import co.com.soinsoftware.billing.view.JFItemConcept;
import co.com.soinsoftware.billing.view.JFItemConceptDeactivation;
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

	private final JFItemConcept itemConceptFrame;

	private final JFItemConceptDeactivation deactivateItemConceptFrame;

	public MenuController(final JFReportReceipt reportFrame,
			final JFUser userFrame, final JFReceipt receiptFrame,
			final JFViewUser viewUserFrame,
			final JFReceiptDeactivation deactivateReceiptFrame,
			final JFItemConcept itemConceptFrame,
			final JFItemConceptDeactivation deactivateItemConceptFrame) {
		super();
		this.reportFrame = reportFrame;
		this.userFrame = userFrame;
		this.receiptFrame = receiptFrame;
		this.viewUserFrame = viewUserFrame;
		this.deactivateReceiptFrame = deactivateReceiptFrame;
		this.itemConceptFrame = itemConceptFrame;
		this.deactivateItemConceptFrame = deactivateItemConceptFrame;
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

	public JFItemConcept getItemConceptFrame() {
		return itemConceptFrame;
	}

	public JFItemConceptDeactivation getDeactivateItemConceptFrame() {
		return deactivateItemConceptFrame;
	}
}
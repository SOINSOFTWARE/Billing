package co.com.soinsoftware.billing.controller;

import co.com.soinsoftware.billing.view.JFMain;
import co.com.soinsoftware.billing.view.JFReceipt;
import co.com.soinsoftware.billing.view.JFUser;

/**
 * @author Carlos Rodriguez
 * @since 30/05/2016
 * @version 1.0
 */
public class MenuController {

	private final JFMain mainFrame;

	private final JFUser userFrame;

	private final JFReceipt receiptFrame;

	public MenuController(final JFMain mainFrame, final JFUser userFrame,
			final JFReceipt receiptFrame) {
		super();
		this.mainFrame = mainFrame;
		this.userFrame = userFrame;
		this.receiptFrame = receiptFrame;
	}

	public JFMain getMainFrame() {
		return mainFrame;
	}

	public JFUser getUserFrame() {
		return userFrame;
	}

	public JFReceipt getReceiptFrame() {
		return receiptFrame;
	}
}
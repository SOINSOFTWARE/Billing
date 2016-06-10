package co.com.soinsoftware.billing.report;

import java.util.List;

import co.com.soinsoftware.billing.entity.Receipt;

public class ThreadGenerator extends Thread {

	private Receipt receipt;

	private List<Receipt> receiptList;

	public ThreadGenerator(final Receipt receipt) {
		super();
		this.receipt = receipt;
		this.setPriority(Thread.MAX_PRIORITY);
		this.setName("Report Generator");
	}

	public ThreadGenerator(final List<Receipt> receiptList) {
		super();
		this.receiptList = receiptList;
	}

	@Override
	public void run() {
		System.out.println("Starting report generation");
		if (this.receipt != null) {
			this.generateReceiptReport();
		} else if (this.receiptList != null) {
			this.generateReceiptListReport();
		}
		System.out.println("Finishing report generation");
	}

	public void generateReceiptReport() {
		final ReceiptReport receiptReport = new ReceiptReport(this.receipt);
		receiptReport.generate();
	}

	public void generateReceiptListReport() {
		final ReceiptListReport receiptSetReport = new ReceiptListReport(
				this.receiptList);
		receiptSetReport.generate();
	}
}
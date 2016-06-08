package co.com.soinsoftware.billing.report;

import co.com.soinsoftware.billing.entity.Receipt;

public class ThreadGenerator extends Thread {

	private final Receipt receipt;

	public ThreadGenerator(final Receipt receipt) {
		super();
		this.receipt = receipt;
	}

	@Override
	public void run() {
		System.out.println("Starting report generation");
		final Generator generator = new Generator(this.receipt);
		generator.generate();
		System.out.println("Finishing report generation");
	}
}
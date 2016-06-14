package co.com.soinsoftware.billing.report;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import co.com.soinsoftware.billing.entity.Company;
import co.com.soinsoftware.billing.entity.Receipt;

public class ReceiptListReport {

	private static final String REPORT_NAME = "/receiptList.jasper";

	private static final String PARAM_TITLE = "Title";
	private static final String PARAM_NIT = "NIT";

	private static final String LABEL_NIT = "NIT: ";

	private final List<Receipt> receiptList;

	public ReceiptListReport(final List<Receipt> receiptList) {
		super();
		this.receiptList = receiptList;
	}

	public boolean generate() {
		boolean generated = false;
		try {
			System.out.println("Loading jasper file");
			final JasperReport jasReport = this.loadJasperReport();
			final Map<String, Object> parameters = this.createParameters();
			final JRBeanCollectionDataSource dataSource = this
					.createDataSource();
			System.out.println("Filling report");
			final JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasReport, parameters, dataSource);
			System.out.println("Starting show");
			JasperViewer.viewReport(jasperPrint, false);
		} catch (JRException ex) {
			System.out.println(ex);
		}
		return generated;
	}

	private JasperReport loadJasperReport() throws JRException {
		final InputStream resourceIS = this.getClass().getResourceAsStream(
				REPORT_NAME);
		return (JasperReport) JRLoader.loadObject(resourceIS);
	}

	private Map<String, Object> createParameters() {
		final Company company = this.getFirstReceipt().getCompany();
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(PARAM_TITLE, company.getName());
		parameters.put(PARAM_NIT, LABEL_NIT + company.getNit());
		return parameters;
	}

	private JRBeanCollectionDataSource createDataSource() {
		final List<Receipt> receiptList = new ArrayList<Receipt>(
				this.receiptList);
		Collections.sort(receiptList);
		return new JRBeanCollectionDataSource(receiptList);
	}

	private Receipt getFirstReceipt() {
		return this.receiptList.iterator().next();
	}
}
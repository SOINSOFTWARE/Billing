package co.com.soinsoftware.billing.report;

import java.io.InputStream;
import java.text.SimpleDateFormat;
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
import co.com.soinsoftware.billing.entity.Item;
import co.com.soinsoftware.billing.entity.Receipt;
import co.com.soinsoftware.billing.entity.User;

public class ReceiptReport {

	private final Receipt receipt;

	public ReceiptReport(final Receipt receipt) {
		super();
		this.receipt = receipt;
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
				"/billing.jasper");
		return (JasperReport) JRLoader.loadObject(resourceIS);
	}

	private Map<String, Object> createParameters() {
		final Company company = this.receipt.getCompany();
		final SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
		final User client = this.receipt.getUserByIduser();
		client.fillNonDbFields();
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("Title", company.getName());
		parameters.put("NIT", "NIT: " + company.getNit());
		parameters.put("ReceiptNO",
				"RECIBO DE CAJA NO " + this.receipt.getNumber());
		parameters.put("ReceiptDate",
				"Fecha: " + format.format(this.receipt.getReceiptdate()));
		parameters.put("ClientId", "Cliente " + client.getIdentification());
		parameters.put("ClientName", client.getFullName().toUpperCase());
		return parameters;
	}

	private JRBeanCollectionDataSource createDataSource() {
		final List<Item> itemList = new ArrayList<Item>(
				this.receipt.getItemSet());
		Collections.sort(itemList);
		return new JRBeanCollectionDataSource(itemList);
	}
}
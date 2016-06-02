package co.com.soinsoftware.billing.report;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.sbt;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilders;
import net.sf.dynamicreports.report.builder.subtotal.AggregationSubtotalBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import co.com.soinsoftware.billing.entity.Company;
import co.com.soinsoftware.billing.entity.Item;
import co.com.soinsoftware.billing.entity.Receipt;
import co.com.soinsoftware.billing.entity.User;

public class Generator {

	private final Receipt receipt;

	private final JasperReportBuilder report;

	public Generator(final Receipt receipt) {
		super();
		this.receipt = receipt;
		this.report = DynamicReports.report();
	}

	public boolean generate() {
		boolean generated = false;
		try {
			this.buildTitleSection();
			this.buildPageHeaderSection();
			this.buildDetailSection();
			this.setDataSource();
			report.show(false);
			generated = true;
		} catch (DRException ex) {
			System.out.println(ex);
		}
		return generated;
	}

	private void buildTitleSection() {
		final StyleBuilder style = this
				.getNewStyle()
				.bold()
				.setTextAlignment(HorizontalTextAlignment.CENTER,
						VerticalTextAlignment.TOP);
		final Company company = this.receipt.getCompany();
		this.report.title(
				cmp.verticalList().add(
						this.getTextField(company.getName(), style),
						this.getTextField("NIT: " + company.getNit(), style)),
				cmp.verticalGap(30));
	}

	private void buildPageHeaderSection() {
		final SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
		final StyleBuilder style = this
				.getNewStyle()
				.bold()
				.setTextAlignment(HorizontalTextAlignment.CENTER,
						VerticalTextAlignment.TOP);
		final User client = this.receipt.getUserByIduser();
		final StringBuilder nameBuilder = new StringBuilder();
		nameBuilder.append(client.getName().toUpperCase());
		nameBuilder.append(" ");
		nameBuilder.append(client.getLastname().toUpperCase());
		final String receiptDate = "Fecha: "
				+ format.format(this.receipt.getReceiptdate());
		final String clientId = "Cliente " + client.getIdentification();

		this.report
				.pageHeader(
						cmp.verticalList()
								.add(this.getTextField("RECIBO DE CAJA NO "
										+ this.receipt.getNumber(), style),
										this.getTextField(receiptDate, style),
										this.getTextField(clientId, style),
										this.getTextField(
												nameBuilder.toString(), style)),
						cmp.verticalGap(30));
	}

	private void buildDetailSection() {
		final StyleBuilder styleBold = this
				.getNewStyle()
				.bold()
				.setTextAlignment(HorizontalTextAlignment.CENTER,
						VerticalTextAlignment.TOP);
		final StyleBuilder style = this.getNewStyle().setTextAlignment(
				HorizontalTextAlignment.CENTER, VerticalTextAlignment.TOP);

		final TextColumnBuilder<String> colConcept = col.column("Concepto",
				"conceptName", type.stringType());
		final TextColumnBuilder<BigDecimal> colValue = col.column("Valor",
				"value", type.bigDecimalType());
		AggregationSubtotalBuilder<String> sumClass = sbt.text(
				"Total recibido", colConcept).setStyle(styleBold);
		AggregationSubtotalBuilder<BigDecimal> sumNoteValue = sbt.sum(colValue)
				.setStyle(styleBold);

		this.report.columns(colConcept, colValue)
				.setColumnTitleStyle(styleBold).setColumnStyle(style)
				.subtotalsAtSummary(sumNoteValue).subtotalsAtSummary(sumClass);
	}

	private StyleBuilder getNewStyle() {
		return new StyleBuilders().style();
	}

	private TextFieldBuilder<String> getTextField(final String text,
			final StyleBuilder style) {
		return cmp.text(text).setStyle(style);
	}

	private void setDataSource() {
		final List<Item> itemList = new ArrayList<Item>(
				this.receipt.getItemSet());
		Collections.sort(itemList);
		final JRDataSource dataSource = new JRBeanCollectionDataSource(itemList);
		this.report.setDataSource(dataSource);
	}
}
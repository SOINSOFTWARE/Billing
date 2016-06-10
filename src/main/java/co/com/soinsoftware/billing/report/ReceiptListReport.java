package co.com.soinsoftware.billing.report;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.sbt;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.math.BigDecimal;
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
import co.com.soinsoftware.billing.entity.Receipt;

public class ReceiptListReport {

	private final List<Receipt> receiptList;

	private final JasperReportBuilder report;

	public ReceiptListReport(final List<Receipt> receiptList) {
		super();
		this.receiptList = receiptList;
		this.report = DynamicReports.report();
	}

	public boolean generate() {
		boolean generated = false;
		try {
			this.buildTitleSection();
			this.buildDetailSection();
			this.setDataSource();
			System.out.println("Before show");
			report.show(false);
			System.out.println("After show");
			generated = true;
		} catch (DRException ex) {
			System.out.println(ex);
		}
		return generated;
	}

	private void buildTitleSection() {
		System.out.println("Adding title section");
		final StyleBuilder styleBold = this
				.getNewStyle()
				.bold()
				.setTextAlignment(HorizontalTextAlignment.CENTER,
						VerticalTextAlignment.TOP);
		final Company company = this.getFirstReceipt().getCompany();
		this.report.title(
				cmp.verticalList()
						.add(this.getTextField(company.getName(), styleBold),
								this.getTextField("NIT: " + company.getNit(),
										styleBold)), cmp.verticalGap(30));
	}

	private void buildDetailSection() {
		System.out.println("Adding detail section");
		final StyleBuilder styleBold = this
				.getNewStyle()
				.bold()
				.setTextAlignment(HorizontalTextAlignment.CENTER,
						VerticalTextAlignment.TOP)
				.setBorder(DynamicReports.stl.pen1Point());
		final StyleBuilder styleNormal = this
				.getNewStyle()
				.setTextAlignment(HorizontalTextAlignment.CENTER,
						VerticalTextAlignment.TOP)
				.setBorder(DynamicReports.stl.pen1Point());

		final TextColumnBuilder<Long> colIdentificacion = col.column(
				"Identificacion", "userByIduser.identification",
				type.longType());
		final TextColumnBuilder<String> colName = col.column("Nombre",
				"userByIduser.fullName", type.stringType());
		final TextColumnBuilder<String> colDate = col.column("Fecha",
				"formatedReceiptDate", type.stringType());
		final TextColumnBuilder<Long> colReceipt = col.column("Recibo",
				"number", type.longType());
		final TextColumnBuilder<BigDecimal> colValue = col.column("Valor",
				"value", type.bigDecimalType());
		AggregationSubtotalBuilder<String> sumColIden = sbt.text("",
				colIdentificacion).setStyle(styleBold);
		AggregationSubtotalBuilder<String> sumColName = sbt.text("", colName)
				.setStyle(styleBold);
		AggregationSubtotalBuilder<String> sumColDate = sbt.text("", colDate)
				.setStyle(styleBold);
		AggregationSubtotalBuilder<String> sumColReceipt = sbt.text(
				"Total recibido", colReceipt).setStyle(styleBold);
		AggregationSubtotalBuilder<BigDecimal> sumColValue = sbt.sum(colValue)
				.setStyle(styleBold);

		this.report
				.columns(colIdentificacion, colName, colDate, colReceipt,
						colValue).setColumnTitleStyle(styleBold)
				.setColumnStyle(styleNormal).subtotalsAtSummary(sumColValue)
				.subtotalsAtSummary(sumColIden).subtotalsAtSummary(sumColName)
				.subtotalsAtSummary(sumColDate)
				.subtotalsAtSummary(sumColReceipt);
	}

	private StyleBuilder getNewStyle() {
		return new StyleBuilders().style();
	}

	private TextFieldBuilder<String> getTextField(final String text,
			final StyleBuilder style) {
		return cmp.text(text).setStyle(style);
	}

	private void setDataSource() {
		System.out.println("Adding data source");
		final List<Receipt> receiptList = new ArrayList<Receipt>(
				this.receiptList);
		Collections.sort(receiptList);
		final JRDataSource dataSource = new JRBeanCollectionDataSource(
				receiptList);
		this.report.setDataSource(dataSource);
	}

	private Receipt getFirstReceipt() {
		return this.receiptList.iterator().next();
	}
}
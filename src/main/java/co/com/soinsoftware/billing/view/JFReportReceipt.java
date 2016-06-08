package co.com.soinsoftware.billing.view;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumn;

import co.com.soinsoftware.billing.controller.MenuController;
import co.com.soinsoftware.billing.controller.ReceiptController;
import co.com.soinsoftware.billing.controller.UserController;
import co.com.soinsoftware.billing.entity.Receipt;
import co.com.soinsoftware.billing.entity.User;
import co.com.soinsoftware.billing.report.ThreadGenerator;

/**
 * @author Carlos Rodriguez
 * @since 30/05/2016
 * @version 1.0
 */
public class JFReportReceipt extends JFrame implements ActionListener {

	private static final long serialVersionUID = -5816892555495529552L;

	private static final String TITLE = "Facturador - Ver Recibos";

	private static final String MSG_EMPTY_ID = "El campo identificación debe ser completado para realizar la busqueda";

	private static final String MSG_EMPTY_RECEIPT = "El campo número de recibo debe ser completado para realizar la busqueda";

	private static final String MSG_NO_CLIENT = "No existen usuarios con el número de cedula especificado";

	private static final String MSG_NO_RECORDS = "No se encontraron registros";

	private static final String MSG_NO_VALID_YEAR = "El año a ser consultado no posee datos.";

	private static final String MSG_NO_YEAR = "Digite un año valido. Ejemplo: "
			+ Calendar.getInstance().get(Calendar.YEAR);

	private static final String MSG_PRINT_STARTED = "El proceso de impresión fue iniciado, en unos segundos su recibo podrá ser impreso.";

	private static final String[] RECEIPT_COLUMN_NAMES = { "Identificación",
			"Nombre", "Fecha", "Recibo", "Valor" };

	private static final String[] ITEM_COLUMN_NAMES = { "Concepto", "Valor" };

	private final User loggedUser;

	private final UserController userController;

	private final ReceiptController receiptController;

	private JPanel panel;

	private JFormattedTextField jtfYear;

	private JList<String> jlsMonth;

	private JFormattedTextField jtfIdentification;

	private JTextField jtfName;

	private JTextField jtfLastName;

	private JTable jtbReceipts;

	private JScrollPane jspReceiptsTable;

	private JLabel jlbTotalMonth;

	private JButton jbtSearchUser;

	private JButton jbtSearch;

	private JButton jbtClean;

	private JButton jbtPrint;

	private JLabel jlbReceiptDate;

	private JLabel jlbReceiptNumber;

	private JLabel jlbReceiptClientId;

	private JLabel jlbReceiptClientName;

	private JTable jtbReceiptItems;

	private JScrollPane jspReceiptItemsTable;

	private JFormattedTextField jtfReceiptNumber;

	private JLabel jlbViewReceiptNumber;

	private JButton jbtViewReceipt;

	private User client;

	private Receipt receipt;

	public JFReportReceipt(final User loggedUser) {
		super();
		this.loggedUser = loggedUser;
		this.userController = new UserController();
		this.receiptController = new ReceiptController();
		this.setBackground(ViewUtils.GREY);
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle(this.loggedUser.getCompany().getName() + " - " + TITLE);
		this.panel = this.createPanel();
		this.setContentPane(this.panel);
	}

	public void addController(final MenuController controller) {
		final JMenuBar menuBar = new JMBAppMenu(controller);
		this.setJMenuBar(menuBar);
	}

	public void refresh() {
		this.client = null;
		this.receipt = null;
		this.cleanAction();
	}

	@Override
	public void actionPerformed(final ActionEvent evt) {
		final Object source = evt.getSource();
		if (source.equals(this.jbtSearchUser)) {
			this.searchClientAction();
		} else if (source.equals(this.jbtClean)) {
			this.cleanAction();
		} else if (source.equals(this.jbtSearch)) {
			this.searchAction();
		} else if (source.equals(this.jbtViewReceipt)) {
			this.searchReceiptAction();
		} else if (source.equals(this.jbtPrint)) {
			this.printAction();
		}
	}

	private JPanel createPanel() {
		final JPanel panel = new JPanel();
		panel.setBackground(ViewUtils.GREY);
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.setLayout(null);

		final JLabel jlbTitle = ViewUtils.createJLabel("VER RECIBOS", 30, 10);
		final JLabel jlbYear = ViewUtils.createJLabel("Año:", 30, 40);
		final JLabel jlbMonth = ViewUtils.createJLabel("Mes:", 30, 100);
		final JLabel jlbIdentification = ViewUtils.createJLabel(
				"Identificación:", 30, 220);
		final JLabel jlbName = ViewUtils.createJLabel("Nombre(s):", 30, 280);
		final JLabel jlbLastName = ViewUtils.createJLabel("Apellido(s):", 30,
				340);

		this.jtfYear = ViewUtils.createJFormatedTextField(null, 30, 60);
		this.jtfYear.setText(String.valueOf(Calendar.getInstance().get(
				Calendar.YEAR)));
		this.jlsMonth = ViewUtils.createJList(null, this.getMonthData(), 30,
				120);
		this.jlsMonth.setSelectedIndex(Calendar.getInstance().get(
				Calendar.MONTH));
		JScrollPane jspMonth = new JScrollPane(this.jlsMonth);
		jspMonth.setBounds(30, 120, 186, 80);
		this.jtfIdentification = ViewUtils.createJFormatedTextField(null, 30,
				240);
		this.jbtSearchUser = ViewUtils.createJButton("Buscar", KeyEvent.VK_B,
				226, 240);
		this.jbtSearchUser.addActionListener(this);
		this.jtfName = ViewUtils.createJTextField(null, 30, 300);
		this.jtfName.setEditable(false);
		this.jtfLastName = ViewUtils.createJTextField(null, 30, 360);
		this.jtfLastName.setEditable(false);
		this.jbtClean = ViewUtils.createJButton("Limpiar", KeyEvent.VK_L, 30,
				390);
		this.jbtClean.addActionListener(this);
		this.jbtSearch = ViewUtils
				.createJButton("Ver", KeyEvent.VK_S, 127, 390);
		this.jbtSearch.addActionListener(this);

		panel.add(jlbTitle);
		panel.add(jlbYear);
		panel.add(jlbMonth);
		panel.add(jlbIdentification);
		panel.add(jlbName);
		panel.add(jlbLastName);
		panel.add(this.jtfYear);
		panel.add(jspMonth);
		panel.add(this.jtfIdentification);
		panel.add(this.jbtSearchUser);
		panel.add(this.jtfName);
		panel.add(this.jtfLastName);
		panel.add(this.jbtClean);
		panel.add(this.jbtSearch);
		this.buildViewReceiptInfo(panel);
		this.buildReceiptInfo(panel);
		return panel;
	}

	private void buildViewReceiptInfo(final JPanel panel) {
		this.jlbViewReceiptNumber = ViewUtils.createJLabel("Recibo de caja NO",
				425, 260);
		this.jtfReceiptNumber = ViewUtils.createJFormatedTextField(null, 425,
				280);
		this.jbtViewReceipt = ViewUtils.createJButton("Ver recibo",
				KeyEvent.VK_D, 621, 280);
		this.jbtViewReceipt.addActionListener(this);

		panel.add(this.jlbViewReceiptNumber);
		panel.add(this.jtfReceiptNumber);
		panel.add(this.jbtViewReceipt);
		this.setViewReceiptFieldsVisibility(false);
	}

	private void buildReceiptInfo(final JPanel panel) {
		this.jlbReceiptNumber = ViewUtils.createJLabel("RECIBO DE CAJA NO",
				425, 320);
		this.jlbReceiptDate = ViewUtils.createJLabel("Fecha: ", 425, 350);
		this.jlbReceiptClientId = ViewUtils.createJLabel("Cliente", 425, 370);
		this.jlbReceiptClientName = ViewUtils.createJLabel("Nombre", 425, 390);
		this.jbtPrint = ViewUtils.createJButton("Imprimir", KeyEvent.VK_I, 425,
				580);
		this.jbtPrint.addActionListener(this);

		panel.add(jlbReceiptNumber);
		panel.add(jlbReceiptDate);
		panel.add(jlbReceiptClientId);
		panel.add(jlbReceiptClientName);
		panel.add(jbtPrint);
		this.setReceiptFieldsVisibility(false);
	}

	private void setViewReceiptFieldsVisibility(final boolean visible) {
		this.jlbViewReceiptNumber.setVisible(visible);
		this.jtfReceiptNumber.setVisible(visible);
		this.jbtViewReceipt.setVisible(visible);
	}

	private void setReceiptFieldsVisibility(final boolean visible) {
		this.jlbReceiptNumber.setVisible(visible);
		this.jlbReceiptDate.setVisible(visible);
		this.jlbReceiptClientId.setVisible(visible);
		this.jlbReceiptClientName.setVisible(visible);
		this.jbtPrint.setVisible(visible);
		if (this.jspReceiptItemsTable != null) {
			this.jspReceiptItemsTable.setVisible(visible);
		}
	}

	private String[] getMonthData() {
		return new String[] { "Enero", "Febrero", "Marzo", "Abril", "Mayo",
				"Junio", "Julio", "Agosto", "Septiembre", "Octubre",
				"Noviembre", "Diciembre" };
	}

	private void searchClientAction() {
		final int infoMsg = JOptionPane.INFORMATION_MESSAGE;
		final String identificationStr = this.jtfIdentification.getText()
				.replace(".", "").replace(",", "");
		if (!identificationStr.equals("")) {
			final long identification = Long.parseLong(identificationStr);
			client = this.userController.selectUser(identification);
			if (client == null) {
				this.client = null;
				this.jtfIdentification.setText("");
				ViewUtils.showMessage(this, MSG_NO_CLIENT, TITLE, infoMsg);
			}
		} else {
			this.client = null;
			ViewUtils.showMessage(this, MSG_EMPTY_ID, TITLE, infoMsg);
		}
		this.updateTextFields();
	}

	private void cleanAction() {
		final Calendar calendar = Calendar.getInstance();
		this.client = null;
		this.jtfYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));
		this.jlsMonth.setSelectedIndex(calendar.get(Calendar.MONTH));
		this.jtfIdentification.setText("");
		this.cleanReceiptData();
	}

	private void searchAction() {
		this.cleanReceiptData();
		if (validateYear() && !this.jlsMonth.isSelectionEmpty()) {
			final String yearStr = this.jtfYear.getText().replace(".", "")
					.replace(",", "");
			final int year = Integer.parseInt(yearStr);
			final int month = this.jlsMonth.getSelectedIndex() + 1;
			final List<Receipt> receiptList = this.receiptController.select(
					year, month, this.client);
			if (receiptList != null && receiptList.size() > 0) {
				this.fillReceiptTable(receiptList);
				this.setViewReceiptFieldsVisibility(true);
			} else {
				ViewUtils.showMessage(this, MSG_NO_RECORDS, TITLE,
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	private void searchReceiptAction() {
		final int infoMsg = JOptionPane.INFORMATION_MESSAGE;
		this.receipt = null;
		final String receiptNumber = this.jtfReceiptNumber.getText().replace(
				".", "");
		if (!receiptNumber.equals("")) {
			this.receipt = this.receiptController.select(Long
					.parseLong(receiptNumber));
			if (this.receipt != null) {
				this.fillReceiptData();
			} else {
				ViewUtils.showMessage(this, MSG_NO_RECORDS, TITLE, infoMsg);
			}
		} else {
			ViewUtils.showMessage(this, MSG_EMPTY_RECEIPT, TITLE, infoMsg);
		}
	}

	private void printAction() {
		if (this.jbtPrint.isVisible() && this.receipt != null) {
			ViewUtils.showMessage(this, MSG_PRINT_STARTED, TITLE,
					JOptionPane.INFORMATION_MESSAGE);
			final ThreadGenerator generator = new ThreadGenerator(this.receipt);
			generator.start();
		}
	}

	private boolean validateYear() {
		boolean isValid = false;
		final int infoMsg = JOptionPane.INFORMATION_MESSAGE;
		final String yearStr = this.jtfYear.getText().replace(".", "")
				.replace(",", "");
		if (!yearStr.equals("")) {
			final long year = Long.parseLong(yearStr);
			final int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			if (year >= 2000 && year <= currentYear) {
				isValid = true;
			} else {
				ViewUtils.showMessage(this, MSG_NO_VALID_YEAR, TITLE, infoMsg);
			}
		} else {
			ViewUtils.showMessage(this, MSG_NO_YEAR, TITLE, infoMsg);
		}
		return isValid;
	}

	private void updateTextFields() {
		if (this.client == null) {
			this.updateTextFields("", "");
		} else {
			this.updateTextFields(this.client.getName(),
					this.client.getLastname());
		}
	}

	private void updateTextFields(final String name, final String lastName) {
		this.jtfName.setText(name);
		this.jtfLastName.setText(lastName);
	}

	private void fillReceiptTable(final List<Receipt> receiptList) {
		final Object[][] data = this.receiptController
				.buildReceiptData(receiptList);
		this.jtbReceipts = new JTable(data, RECEIPT_COLUMN_NAMES);
		this.jtbReceipts.setFillsViewportHeight(true);
		this.jtbReceipts.setEnabled(false);
		for (int i = 0; i < 2; i++) {
			final TableColumn column = this.jtbReceipts.getColumnModel()
					.getColumn(i);
			column.setResizable(false);
			if (i == 1) {
				column.setPreferredWidth(200);
			} else {
				column.setPreferredWidth(100);
			}
		}

		this.jspReceiptsTable = new JScrollPane(this.jtbReceipts);
		this.jspReceiptsTable.setBounds(425, 10, 600, 200);
		final BigDecimal total = this.getTotalMonth(data);
		final String totalStr = "Total del mes: $" + total.toString();
		if (this.jlbTotalMonth == null) {
			this.jlbTotalMonth = ViewUtils.createJLabel(totalStr, 425, 220);
		} else {
			this.jlbTotalMonth.setText(totalStr);
			this.jlbTotalMonth.setVisible(true);
		}
		this.panel.add(this.jspReceiptsTable);
		this.panel.add(this.jlbTotalMonth);
	}

	private BigDecimal getTotalMonth(final Object[][] data) {
		BigDecimal total = new BigDecimal(0);
		for (int i = 0; i < data.length; i++) {
			final BigDecimal value = (BigDecimal) data[i][4];
			total = total.add(value);
		}
		return total;
	}

	private void fillReceiptData() {
		if (this.receipt != null) {
			final User client = this.receipt.getUserByIduser();
			final SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
			final StringBuilder nameBuilder = new StringBuilder();
			nameBuilder.append(client.getName().toUpperCase());
			nameBuilder.append(" ");
			nameBuilder.append(client.getLastname().toUpperCase());
			this.jlbReceiptNumber.setText("RECIBO DE CAJA NO "
					+ this.receipt.getNumber());
			this.jlbReceiptDate.setText("Fecha: "
					+ format.format(this.receipt.getReceiptdate()));
			this.jlbReceiptClientId.setText("Cliente "
					+ client.getIdentification());
			this.jlbReceiptClientName.setText(nameBuilder.toString());

			this.jtbReceiptItems = new JTable(
					this.receiptController.buildItemConceptData(this.receipt),
					ITEM_COLUMN_NAMES);
			this.jtbReceiptItems.setFillsViewportHeight(true);
			this.jtbReceiptItems.setEnabled(false);
			for (int i = 0; i < 2; i++) {
				final TableColumn column = this.jtbReceiptItems
						.getColumnModel().getColumn(i);
				column.setResizable(false);
				if (i == 0) {
					column.setPreferredWidth(230);
				} else {
					column.setPreferredWidth(70);
				}
			}

			this.jspReceiptItemsTable = new JScrollPane(this.jtbReceiptItems);
			this.jspReceiptItemsTable.setBounds(425, 410, 300, 150);
			this.panel.add(this.jspReceiptItemsTable);
			this.setReceiptFieldsVisibility(true);
		}
	}

	private void cleanReceiptData() {
		this.updateTextFields();
		this.setReceiptFieldsVisibility(false);
		this.jtfReceiptNumber.setText("");
		this.setViewReceiptFieldsVisibility(false);
		this.removeReceiptTable();
	}

	private void removeReceiptTable() {
		if (this.jtbReceipts != null) {
			this.panel.remove(this.jspReceiptsTable);
			this.jlbTotalMonth.setVisible(false);
			this.panel.revalidate();
			this.repaint();
		}
	}
}
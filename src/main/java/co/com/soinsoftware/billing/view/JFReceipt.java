package co.com.soinsoftware.billing.view;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
import co.com.soinsoftware.billing.entity.Item;
import co.com.soinsoftware.billing.entity.Itemconcept;
import co.com.soinsoftware.billing.entity.Receipt;
import co.com.soinsoftware.billing.entity.User;

/**
 * @author Carlos Rodriguez
 * @since 31/05/2016
 * @version 1.0
 */
public class JFReceipt extends JFrame implements ActionListener {

	private static final long serialVersionUID = 9030665802815908970L;

	private static final String TITLE = "Facturador - Recibo";

	private static final String MSG_EMPTY_ID = "El campo identificación debe ser completado para realizar la busqueda";

	private static final String MSG_NO_CLIENT = "No existen usuarios con el número de cedula especificado";

	private static final String MSG_NO_CLIENT_SELECTED = "El pago no puede ser efectuado, debe seleccionar un usuario realizando la busqueda usando el número de identificación";

	private static final String MSG_NO_VALUE_SELECTED = "El pago no puede ser efectuado, debe indicar el valor pagado por el usuario";

	private static final String MSG_START_PRINT = "¿Está seguro que desea imprimir el recibo?, una vez iniciado el proceso el recibo no puede ser modificado.";

	private static final String MSG_PRINT_STARTED = "El proceso de impresión fue iniciado, en unos segundos su recibo podrá ser impreso.";

	private static final String[] COLUMN_NAMES = { "Concepto", "Valor" };

	private final User loggedUser;

	private final UserController userController;

	private final ReceiptController receiptController;

	private JFormattedTextField jtfIdentification;

	private JTextField jtfName;

	private JTextField jtfLastName;

	private JFormattedTextField jtfValue;

	private JPanel panel;

	private JButton jbtSearch;

	private JButton jbtClean;

	private JButton jbtPay;

	private JButton jbtPrint;

	private JLabel jlbReceiptTitle;

	private JLabel jlbReceiptNit;

	private JLabel jlbReceiptDate;

	private JLabel jlbReceiptNumber;

	private JLabel jlbReceiptClientId;

	private JLabel jlbReceiptClientName;

	private JTable jtbReceiptItems;

	private JScrollPane jspReceiptTable;

	private User client;

	private Receipt receipt;

	public JFReceipt(final User loggedUser) {
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
		this.jtfIdentification.setText("");
		this.updateTextFields();
	}

	public void actionPerformed(final ActionEvent evt) {
		final Object source = evt.getSource();
		if (source.equals(this.jbtSearch)) {
			this.searchAction();
		} else if (source.equals(this.jbtClean)) {
			this.cleanAction();
		} else if (source.equals(this.jbtPay)) {
			this.payAction();
		} else if (source.equals(this.jbtPrint)) {
			this.printAction();
		}
	}

	private JPanel createPanel() {
		final JPanel panel = new JPanel();
		panel.setBackground(ViewUtils.GREY);
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.setLayout(null);

		final JLabel jlbTitle = ViewUtils.createJLabel("NUEVO RECIBO", 30, 10);
		final JLabel jlbIdentification = ViewUtils.createJLabel(
				"Identificación:", 30, 40);
		final JLabel jlbName = ViewUtils.createJLabel("Nombre(s):", 30, 100);
		final JLabel jlbLastName = ViewUtils.createJLabel("Apellido(s):", 30,
				160);
		final JLabel jlbValue = ViewUtils.createJLabel("Valor:", 30, 220);

		this.jtfIdentification = ViewUtils.createJFormatedTextField(null, 30,
				60);
		this.jbtSearch = ViewUtils.createJButton("Buscar", KeyEvent.VK_B, 226,
				60);
		this.jbtSearch.addActionListener(this);
		this.jtfName = ViewUtils.createJTextField(null, 30, 120);
		this.jtfName.setEditable(false);
		this.jtfLastName = ViewUtils.createJTextField(null, 30, 180);
		this.jtfLastName.setEditable(false);
		this.jtfValue = ViewUtils.createJFormatedTextField(null, 30, 240);
		this.jtfValue.setEnabled(false);
		this.jbtClean = ViewUtils.createJButton("Limpiar", KeyEvent.VK_L, 30,
				270);
		this.jbtClean.addActionListener(this);
		this.jbtPay = ViewUtils.createJButton("Pagar", KeyEvent.VK_P, 127, 270);
		this.jbtPay.addActionListener(this);

		panel.add(jlbTitle);
		panel.add(jlbIdentification);
		panel.add(jlbName);
		panel.add(jlbLastName);
		panel.add(jlbValue);
		panel.add(this.jtfIdentification);
		panel.add(this.jbtSearch);
		panel.add(this.jtfName);
		panel.add(this.jtfLastName);
		panel.add(this.jtfValue);
		panel.add(this.jbtClean);
		panel.add(this.jbtPay);
		this.buildReceiptInfo(panel);
		return panel;
	}

	private void buildReceiptInfo(final JPanel panel) {
		final String name = this.loggedUser.getCompany().getName();
		final String nit = "NIT: " + this.loggedUser.getCompany().getNit();
		final Date currentDate = new Date();
		final SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");

		this.jlbReceiptTitle = ViewUtils.createJLabel(name, 625, 10);
		this.jlbReceiptNit = ViewUtils.createJLabel(nit, 625, 40);
		this.jlbReceiptNumber = ViewUtils.createJLabel("RECIBO DE CAJA NO",
				625, 100);
		this.jlbReceiptDate = ViewUtils.createJLabel(
				"Fecha: " + format.format(currentDate), 625, 130);
		this.jlbReceiptClientId = ViewUtils.createJLabel("Cliente", 625, 160);
		this.jlbReceiptClientName = ViewUtils.createJLabel("Nombre", 625, 190);
		this.jbtPrint = ViewUtils.createJButton("Imprimir", KeyEvent.VK_I, 625,
				460);
		this.jbtPrint.addActionListener(this);

		panel.add(jlbReceiptTitle);
		panel.add(jlbReceiptNit);
		panel.add(jlbReceiptNumber);
		panel.add(jlbReceiptDate);
		panel.add(jlbReceiptClientId);
		panel.add(jlbReceiptClientName);
		panel.add(jbtPrint);
		this.setReceiptFieldsVisibility(false);
	}

	private void searchAction() {
		final int infoMsg = JOptionPane.INFORMATION_MESSAGE;
		final String identificationStr = this.jtfIdentification.getText()
				.replace(".", "");
		if (!identificationStr.equals("")) {
			final long identification = Long.parseLong(identificationStr);
			client = this.userController.selectUser(identification);
			if (client != null) {
				this.jtfValue.setEnabled(true);
			} else {
				this.client = null;
				ViewUtils.showMessage(this, MSG_NO_CLIENT, TITLE, infoMsg);
			}
		} else {
			this.client = null;
			ViewUtils.showMessage(this, MSG_EMPTY_ID, TITLE, infoMsg);
		}
		this.updateTextFields();
	}

	private void cleanAction() {
		this.client = null;
		this.jtfIdentification.setText("");
		this.updateTextFields();
	}

	private void payAction() {
		final int errorMsg = JOptionPane.ERROR_MESSAGE;
		if (this.client != null) {
			final String valueStr = this.jtfValue.getText().replace(".", "");
			if (!valueStr.equals("")) {
				this.receipt = this.receiptController.createReceipt(
						this.loggedUser, this.client,
						Integer.parseInt(valueStr));
				this.fillReceiptData();
			} else {
				ViewUtils.showMessage(this, MSG_NO_VALUE_SELECTED, TITLE,
						errorMsg);
			}
		} else {
			ViewUtils
					.showMessage(this, MSG_NO_CLIENT_SELECTED, TITLE, errorMsg);
		}
	}

	private void printAction() {
		if (this.jbtPrint.isVisible()) {
			final int confirm = ViewUtils.showConfirmDialog(this,
					MSG_START_PRINT, TITLE);
			if (confirm == JOptionPane.OK_OPTION) {
				this.receiptController.saveReceipt(this.receipt);
				ViewUtils.showMessage(this, MSG_PRINT_STARTED, TITLE,
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	private void updateTextFields() {
		if (this.client == null) {
			this.updateTextFields("", "", "");
			this.jtfValue.setEnabled(false);
		} else {
			this.updateTextFields(this.client.getName(),
					this.client.getLastname(), "");
		}
		this.setReceiptFieldsVisibility(false);
	}

	private void updateTextFields(final String name, final String lastName,
			final String value) {
		this.jtfName.setText(name);
		this.jtfLastName.setText(lastName);
		this.jtfValue.setText(value);
	}

	private void fillReceiptData() {
		if (this.receipt != null) {
			final StringBuilder nameBuilder = new StringBuilder();
			nameBuilder.append(this.client.getName().toUpperCase());
			nameBuilder.append(" ");
			nameBuilder.append(this.client.getLastname().toUpperCase());
			this.jlbReceiptNumber.setText("RECIBO DE CAJA NO "
					+ this.receipt.getNumber());
			this.jlbReceiptClientId.setText("Cliente "
					+ this.client.getIdentification());
			this.jlbReceiptClientName.setText(nameBuilder.toString());

			this.jtbReceiptItems = new JTable(this.buildTableData(),
					COLUMN_NAMES);
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

			this.jspReceiptTable = new JScrollPane(this.jtbReceiptItems);
			this.jspReceiptTable.setBounds(625, 240, 300, 200);
			this.panel.add(this.jspReceiptTable);
			this.setReceiptFieldsVisibility(true);
		}
	}

	private Object[][] buildTableData() {
		final int itemSize = this.receipt.getItemSet().size();
		final List<Item> itemList = new ArrayList<Item>(
				this.receipt.getItemSet());
		final Object[][] data = new Object[itemSize][2];
		for (int i = 0; i < itemSize; i++) {
			final Item item = itemList.get(i);
			final String name = this.getItemConceptName(item.getId()
					.getIditemconcept());
			data[i][0] = name;
			data[i][1] = item.getValue();
		}
		return data;
	}

	private String getItemConceptName(final int idItemConcept) {
		String name = "";
		final Set<Itemconcept> itemConceptSet = this.receipt.getConfiguration()
				.getItemconcepts();
		for (final Itemconcept concept : itemConceptSet) {
			if (concept.getId().equals(idItemConcept)) {
				name = concept.getName();
				break;
			}
		}
		return name;
	}

	private void setReceiptFieldsVisibility(final boolean visible) {
		this.jlbReceiptTitle.setVisible(visible);
		this.jlbReceiptNit.setVisible(visible);
		this.jlbReceiptNumber.setVisible(visible);
		this.jlbReceiptDate.setVisible(visible);
		this.jlbReceiptClientId.setVisible(visible);
		this.jlbReceiptClientName.setVisible(visible);
		this.jbtPrint.setVisible(visible);
		if (this.jspReceiptTable != null) {
			this.jspReceiptTable.setVisible(visible);
		}
	}
}
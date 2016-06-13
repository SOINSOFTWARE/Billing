package co.com.soinsoftware.billing.view;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumn;

import co.com.soinsoftware.billing.controller.MenuController;
import co.com.soinsoftware.billing.controller.ReceiptController;
import co.com.soinsoftware.billing.controller.UserController;
import co.com.soinsoftware.billing.entity.Receipt;
import co.com.soinsoftware.billing.entity.User;
import co.com.soinsoftware.billing.util.ItemTableModel;

public class JFReceiptDeactivation extends JFrame implements ActionListener {

	private static final long serialVersionUID = -8432520949164190295L;

	private static final String TITLE = "Facturador - Eliminar Recibos";

	private static final String MSG_CURRENTLY_DEACTIVATED = "El recibo ya fue eliminado";

	private static final String MSG_DEACTIVATE_QUESTION = "¿Está seguro que desea eliminar este recibo?, Al aceptar el valor pagado será sumado a la deuda del usuario nuevamente.";

	private static final String MSG_EMPTY_RECEIPT = "El campo número de recibo debe ser completado para realizar la busqueda";

	private static final String MSG_NO_RECORDS = "No se encontraron registros";

	private final User loggedUser;

	private final ReceiptController receiptController;

	private final UserController userController;

	private JPanel panel;

	private JFormattedTextField jtfReceiptNumber;

	private JButton jbtViewReceipt;

	private JButton jbtDeactivateReceipt;

	private JLabel jlbReceiptTitle;

	private JLabel jlbReceiptNit;

	private JLabel jlbReceiptDate;

	private JLabel jlbReceiptNumber;

	private JLabel jlbReceiptClientId;

	private JLabel jlbReceiptClientName;

	private JTable jtbReceiptItems;

	private ItemTableModel tableModel;

	private JScrollPane jspReceiptTable;

	private Receipt receipt;

	public JFReceiptDeactivation(final User loggedUser) {
		super();
		this.loggedUser = loggedUser;
		this.receiptController = new ReceiptController();
		this.userController = new UserController();
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
		this.receipt = null;
		this.cleanAction();
	}

	@Override
	public void actionPerformed(final ActionEvent evt) {
		final Object source = evt.getSource();
		if (source.equals(this.jbtViewReceipt)) {
			this.searchReceiptAction();
		} else if (source.equals(this.jbtDeactivateReceipt)) {
			this.deactivateAction();
		}
	}

	private JPanel createPanel() {
		final JPanel panel = new JPanel();
		panel.setBackground(ViewUtils.GREY);
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.setLayout(null);

		final JLabel jlbTitle = ViewUtils.createJLabel("ELIMINAR RECIBO", 30,
				10);
		final JLabel jlbViewReceiptNumber = ViewUtils.createJLabel(
				"Recibo de caja NO", 30, 40);
		this.jtfReceiptNumber = ViewUtils
				.createJFormatedTextField(null, 30, 70);
		this.jbtViewReceipt = ViewUtils.createJButton("Ver recibo",
				KeyEvent.VK_D, 127, 100);
		this.jbtViewReceipt.addActionListener(this);

		panel.add(jlbTitle);
		panel.add(jlbViewReceiptNumber);
		panel.add(this.jtfReceiptNumber);
		panel.add(this.jbtViewReceipt);
		this.buildReceiptInfo(panel);
		return panel;
	}

	private void cleanAction() {
		this.jtfReceiptNumber.setText("");
		this.setReceiptFieldsVisibility(false);
		if (this.jtbReceiptItems != null) {
			this.panel.remove(this.jspReceiptTable);
			this.panel.repaint();
		}
	}

	private void buildReceiptInfo(final JPanel panel) {
		final String name = this.loggedUser.getCompany().getName();
		final String nit = "NIT: " + this.loggedUser.getCompany().getNit();

		this.jlbReceiptTitle = ViewUtils.createJLabel(name, 625, 10);
		this.jlbReceiptNit = ViewUtils.createJLabel(nit, 625, 40);
		this.jlbReceiptNumber = ViewUtils.createJLabel("RECIBO DE CAJA NO",
				625, 100);
		this.jlbReceiptDate = ViewUtils.createJLabel("Fecha: ", 625, 130);
		this.jlbReceiptClientId = ViewUtils.createJLabel("Cliente", 625, 160);
		this.jlbReceiptClientName = ViewUtils.createJLabel("Nombre", 625, 190);
		this.jbtDeactivateReceipt = ViewUtils.createJButton("Eliminar",
				KeyEvent.VK_D, 625, 460);
		this.jbtDeactivateReceipt.addActionListener(this);

		panel.add(jlbReceiptTitle);
		panel.add(jlbReceiptNit);
		panel.add(jlbReceiptNumber);
		panel.add(jlbReceiptDate);
		panel.add(jlbReceiptClientId);
		panel.add(jlbReceiptClientName);
		panel.add(jbtDeactivateReceipt);
		this.setReceiptFieldsVisibility(false);
	}

	private void setReceiptFieldsVisibility(final boolean visible) {
		this.jlbReceiptTitle.setVisible(visible);
		this.jlbReceiptNit.setVisible(visible);
		this.jlbReceiptNumber.setVisible(visible);
		this.jlbReceiptDate.setVisible(visible);
		this.jlbReceiptClientId.setVisible(visible);
		this.jlbReceiptClientName.setVisible(visible);
		this.jbtDeactivateReceipt.setVisible(visible);
		if (this.jspReceiptTable != null) {
			this.jspReceiptTable.setVisible(visible);
		}
	}

	private void fillReceiptData() {
		if (this.receipt != null) {
			final User client = this.receipt.getUserByIduser();
			final SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");

			this.jlbReceiptNumber.setText("RECIBO DE CAJA NO "
					+ this.receipt.getNumber());
			this.jlbReceiptDate.setText("Fecha: "
					+ format.format(this.receipt.getReceiptdate()));
			this.jlbReceiptClientId.setText("Cliente "
					+ client.getIdentification());
			this.jlbReceiptClientName.setText(client.getFullName()
					.toUpperCase());

			this.tableModel = new ItemTableModel(
					this.receiptController.buildItemConceptData(this.receipt));
			this.jtbReceiptItems = new JTable(this.tableModel);
			this.jtbReceiptItems.setEnabled(false);
			this.jtbReceiptItems.setFillsViewportHeight(true);
			this.setTableColumnDimensions();

			this.jspReceiptTable = new JScrollPane(this.jtbReceiptItems);
			this.jspReceiptTable.setBounds(625, 240, 300, 200);
			this.panel.add(this.jspReceiptTable);
			this.setReceiptFieldsVisibility(true);
		}
	}

	private void setTableColumnDimensions() {
		for (int i = 0; i < 2; i++) {
			final TableColumn column = this.jtbReceiptItems.getColumnModel()
					.getColumn(i);
			column.setResizable(false);
			if (i == 0) {
				column.setPreferredWidth(230);
			} else {
				column.setPreferredWidth(70);
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
				if (this.receipt.isEnabled()) {
					this.fillReceiptData();
				} else {
					this.refresh();
					ViewUtils.showMessage(this, MSG_CURRENTLY_DEACTIVATED,
							TITLE, infoMsg);
				}
			} else {
				ViewUtils.showMessage(this, MSG_NO_RECORDS, TITLE, infoMsg);
			}
		} else {
			ViewUtils.showMessage(this, MSG_EMPTY_RECEIPT, TITLE, infoMsg);
		}
	}

	private void deactivateAction() {
		if (this.jbtDeactivateReceipt.isVisible() && this.receipt != null) {
			final int confirm = ViewUtils.showConfirmDialog(this,
					MSG_DEACTIVATE_QUESTION, TITLE);
			if (confirm == JOptionPane.OK_OPTION) {
				final Date currentDate = new Date();
				this.receipt.setEnabled(false);
				this.receipt.setUserByIdlastchangeuser(this.loggedUser);
				this.receipt.setUpdated(currentDate);
				final User client = this.receipt.getUserByIduser();
				client.setValue(client.getValue().add(this.receipt.getValue()));
				client.setUpdated(currentDate);
				this.receiptController.saveReceipt(this.receipt);
				this.userController.saveUser(client);
				ViewUtils.showMessage(this, ViewUtils.MSG_SAVED, TITLE,
						JOptionPane.INFORMATION_MESSAGE);
				this.refresh();
			}
		}
	}
}
package co.com.soinsoftware.billing.view;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import co.com.soinsoftware.billing.controller.MenuController;
import co.com.soinsoftware.billing.controller.UserController;
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

	private final User loggedUser;

	private final UserController controller;

	private JFormattedTextField jtfIdentification;

	private JTextField jtfName;

	private JTextField jtfLastName;

	private JFormattedTextField jtfValue;

	private JButton jbtSearch;

	private JButton jbtClean;

	private JButton jbtPay;

	private User client;

	public JFReceipt(final User loggedUser) {
		super();
		this.loggedUser = loggedUser;
		this.controller = new UserController();
		this.setBackground(ViewUtils.GREY);
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle(this.loggedUser.getCompany().getName() + " - " + TITLE);
		final JPanel panel = this.createPanel();
		this.setContentPane(panel);
	}

	public void addController(final MenuController controller) {
		final JMenuBar menuBar = new JMBAppMenu(controller);
		this.setJMenuBar(menuBar);
	}

	public void actionPerformed(final ActionEvent evt) {
		final Object source = evt.getSource();
		if (source.equals(this.jbtSearch)) {
			this.searchAction();
		} else if (source.equals(this.jbtClean)) {
			this.cleanAction();
		} else if (source.equals(this.jbtPay)) {
			this.payAction();
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
		return panel;
	}

	private void searchAction() {
		final int infoMsg = JOptionPane.INFORMATION_MESSAGE;
		final String identificationStr = this.jtfIdentification.getText()
				.replace(".", "");
		if (!identificationStr.equals("")) {
			final long identification = Long.parseLong(identificationStr);
			client = this.controller.selectUser(identification);
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
				
			} else {
				ViewUtils.showMessage(this, MSG_NO_VALUE_SELECTED, TITLE, errorMsg);
			}
		} else {
			ViewUtils.showMessage(this, MSG_NO_CLIENT_SELECTED, TITLE, errorMsg);
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
	}

	private void updateTextFields(final String name, final String lastName,
			final String value) {
		this.jtfName.setText(name);
		this.jtfLastName.setText(lastName);
		this.jtfValue.setText(value);
	}
}
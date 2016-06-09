package co.com.soinsoftware.billing.view;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;

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
 * @since 30/05/2016
 * @version 1.0
 */
public class JFUser extends JFrame implements ActionListener {

	private static final long serialVersionUID = -6950477778737766186L;

	private static final String TITLE = "Facturador - Usuario";

	private static final String MSG_EMPTY_FIELDS = "Por favor complete todos los campos";

	private static final String MSG_VALUE = "La deuda debe ser mayor a 0";

	private static final String MSG_USER_EXISTS = "Ya existe un usuario registrado con este número de documento";

	private final User loggedUser;

	private final UserController controller;

	private JFormattedTextField jtfIdentification;

	private JTextField jtfName;

	private JTextField jtfLastName;

	private JFormattedTextField jtfValue;

	private JButton jbtClean;

	private JButton jbtSave;

	public JFUser(final User loggedUser) {
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

	public void refresh() {
		this.cleanFields();
	}

	public void actionPerformed(final ActionEvent evt) {
		final Object source = evt.getSource();
		if (source.equals(this.jbtClean)) {
			this.cleanFields();
		} else if (source.equals(this.jbtSave)) {
			this.saveAction();
		}
	}

	private JPanel createPanel() {
		final JPanel panel = new JPanel();
		panel.setBackground(ViewUtils.GREY);
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.setLayout(null);

		final JLabel jlbTitle = ViewUtils.createJLabel("NUEVO USUARIO", 30, 10);
		final JLabel jlbIdentification = ViewUtils.createJLabel(
				"Identificación:", 30, 40);
		final JLabel jlbName = ViewUtils.createJLabel("Nombre(s):", 30, 100);
		final JLabel jlbLastName = ViewUtils.createJLabel("Apellido(s):", 30,
				160);
		final JLabel jlbValue = ViewUtils.createJLabel("Deuda:", 30, 220);

		this.jtfIdentification = ViewUtils.createJFormatedTextField(null, 30,
				60);
		this.jtfName = ViewUtils.createJTextField(null, 30, 120);
		this.jtfName.setDocument(new JTextFieldLimit(45));
		this.jtfLastName = ViewUtils.createJTextField(null, 30, 180);
		this.jtfLastName.setDocument(new JTextFieldLimit(45));
		this.jtfValue = ViewUtils.createJFormatedTextField(null, 30, 240);
		this.jtfValue.setText("0");

		this.jbtClean = ViewUtils.createJButton("Limpiar", KeyEvent.VK_L, 30,
				280);
		this.jbtSave = ViewUtils.createJButton("Guardar", KeyEvent.VK_S, 123,
				280);
		this.jbtClean.addActionListener(this);
		this.jbtSave.addActionListener(this);

		panel.add(jlbTitle);
		panel.add(jlbIdentification);
		panel.add(jlbName);
		panel.add(jlbLastName);
		panel.add(jlbValue);
		panel.add(this.jtfIdentification);
		panel.add(this.jtfName);
		panel.add(this.jtfLastName);
		panel.add(this.jtfValue);
		panel.add(this.jbtClean);
		panel.add(this.jbtSave);
		return panel;
	}

	private void cleanFields() {
		this.jtfIdentification.setText("");
		this.jtfName.setText("");
		this.jtfLastName.setText("");
		this.jtfValue.setText("0");
	}

	private void saveAction() {
		if (this.doValidations()) {
			final String identificationStr = this.jtfIdentification.getText()
					.replace(".", "").replace(",", "");
			final long identification = Long.parseLong(identificationStr);
			final String name = this.jtfName.getText();
			final String lastName = this.jtfLastName.getText();
			final String valueStr = this.jtfValue.getText().replace(".", "")
					.replace(",", "");
			final BigDecimal value = new BigDecimal(valueStr);
			this.controller.saveUser(this.loggedUser.getCompany(),
					identification, name, lastName, value);
			ViewUtils.showMessage(this, ViewUtils.MSG_SAVED, TITLE,
					JOptionPane.INFORMATION_MESSAGE);
			this.cleanFields();
		}
	}

	private boolean doValidations() {
		final int errorMsg = JOptionPane.ERROR_MESSAGE;
		boolean isValid = false;
		if (this.validateFields()) {
			if (this.validateValue()) {
				final String identificationStr = this.jtfIdentification
						.getText().replace(".", "").replace(",", "");
				final long identification = Long.parseLong(identificationStr);
				if (!this.controller.isExistingUser(identification)) {
					isValid = true;
				} else {
					ViewUtils.showMessage(this, MSG_USER_EXISTS, TITLE,
							errorMsg);
				}
			} else {
				ViewUtils.showMessage(this, MSG_VALUE, TITLE, errorMsg);
			}
		} else {
			ViewUtils.showMessage(this, MSG_EMPTY_FIELDS, TITLE, errorMsg);
		}
		return isValid;
	}

	private boolean validateFields() {
		final String valueStr = this.jtfValue.getText().replace(".", "")
				.replace(",", "");
		return !this.jtfIdentification.getText().equals("")
				&& !this.jtfName.getText().equals("")
				&& !this.jtfLastName.getText().equals("")
				&& !valueStr.equals("");
	}

	private boolean validateValue() {
		final String valueStr = this.jtfValue.getText().replace(".", "")
				.replace(",", "");
		final BigDecimal value = new BigDecimal(valueStr);
		return value.doubleValue() > 0;
	}
}
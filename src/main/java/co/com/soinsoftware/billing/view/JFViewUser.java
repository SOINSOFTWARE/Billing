package co.com.soinsoftware.billing.view;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.Date;

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
 * @since 02/06/2016
 * @version 1.0
 */
public class JFViewUser extends JFrame implements ActionListener {

	private static final long serialVersionUID = -4200612234138262654L;

	private static final String TITLE = "Facturador - Ver usuarios";

	private static final String MSG_EMPTY_ID = "El campo identificación debe ser completado para realizar la busqueda";

	private static final String MSG_NO_CLIENT = "No existen usuarios con el número de cedula especificado";

	private static final String MSG_SAVE_QUESTION = "¿Está seguro que desea guardar los cambios en el usuario?";

	private static final String MSG_EMPTY_FIELDS = "Debe completar todos los campos para poder actualizar";

	private static final String MSG_VALUE = "La deuda no puede ser disminuida en este modulo";

	private final User loggedUser;

	private final UserController userController;

	private JPanel panel;

	private JFormattedTextField jtfSearchIdentification;

	private JButton jbtSearch;

	private JLabel jlbIdentification;

	private JFormattedTextField jtfIdentification;

	private JLabel jlbName;

	private JTextField jtfName;

	private JLabel jlbLastName;

	private JTextField jtfLastName;

	private JLabel jlbValue;

	private JFormattedTextField jtfValue;

	private JButton jbtSave;

	private User client;

	public JFViewUser(final User loggedUser) {
		super();
		this.loggedUser = loggedUser;
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
		this.client = null;
		this.jtfSearchIdentification.setText("");
		this.updateTextFields();
	}

	@Override
	public void actionPerformed(final ActionEvent evt) {
		final Object source = evt.getSource();
		if (source.equals(this.jbtSearch)) {
			this.searchAction();
		} else if (source.equals(this.jbtSave)) {
			this.saveAction();
		}
	}

	private JPanel createPanel() {
		final JPanel panel = new JPanel();
		panel.setBackground(ViewUtils.GREY);
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.setLayout(null);

		final JLabel jlbTitle = ViewUtils.createJLabel("VER USUARIO", 30, 10);
		final JLabel jlbSearchIdentification = ViewUtils.createJLabel(
				"Identificación:", 30, 40);

		this.jtfSearchIdentification = ViewUtils.createJFormatedTextField(null,
				30, 60);
		this.jbtSearch = ViewUtils.createJButton("Buscar", KeyEvent.VK_B, 127,
				90);
		this.jbtSearch.addActionListener(this);

		this.jlbIdentification = ViewUtils.createJLabel("Identificación:", 425,
				40);
		this.jlbName = ViewUtils.createJLabel("Nombre(s):", 425, 100);
		this.jlbLastName = ViewUtils.createJLabel("Apellido(s):", 425, 160);
		this.jlbValue = ViewUtils.createJLabel("Deuda:", 425, 220);

		this.jtfIdentification = ViewUtils.createJFormatedTextField(null, 425,
				60);
		this.jtfName = ViewUtils.createJTextField(null, 425, 120);
		this.jtfLastName = ViewUtils.createJTextField(null, 425, 180);
		this.jtfValue = ViewUtils.createJFormatedTextField(null, 425, 240);
		this.jtfValue.setText("0");

		this.jbtSave = ViewUtils.createJButton("Guardar", KeyEvent.VK_G, 525,
				270);
		this.jbtSave.addActionListener(this);

		panel.add(jlbTitle);
		panel.add(jlbSearchIdentification);
		panel.add(this.jtfSearchIdentification);
		panel.add(this.jbtSearch);
		panel.add(this.jlbIdentification);
		panel.add(this.jlbName);
		panel.add(this.jlbLastName);
		panel.add(this.jlbValue);
		panel.add(this.jtfIdentification);
		panel.add(this.jtfName);
		panel.add(this.jtfLastName);
		panel.add(this.jtfValue);
		panel.add(this.jbtSave);
		this.setUserFieldsVisibility(false);
		return panel;
	}

	private void setUserFieldsVisibility(final boolean visible) {
		this.jlbIdentification.setVisible(visible);
		this.jlbName.setVisible(visible);
		this.jlbLastName.setVisible(visible);
		this.jlbValue.setVisible(visible);
		this.jtfIdentification.setVisible(visible);
		this.jtfName.setVisible(visible);
		this.jtfLastName.setVisible(visible);
		this.jtfValue.setVisible(visible);
		this.jbtSave.setVisible(visible);
	}

	private void searchAction() {
		final int infoMsg = JOptionPane.INFORMATION_MESSAGE;
		final String identificationStr = this.jtfSearchIdentification.getText()
				.replace(".", "").replace(",", "");
		if (!identificationStr.equals("")) {
			final long identification = Long.parseLong(identificationStr);
			client = this.userController.selectUser(identification);
			if (client == null) {
				this.client = null;
				ViewUtils.showMessage(this, MSG_NO_CLIENT, TITLE, infoMsg);
			}
		} else {
			this.client = null;
			ViewUtils.showMessage(this, MSG_EMPTY_ID, TITLE, infoMsg);
		}
		this.updateTextFields();
	}

	private void saveAction() {
		if (this.client != null && this.jbtSave.isVisible()) {
			if (this.validateUpdatedClientData()) {
				if (this.validateValue()) {
					final int confirm = ViewUtils.showConfirmDialog(this,
							MSG_SAVE_QUESTION, TITLE);
					if (confirm == JOptionPane.OK_OPTION) {
						this.updateClientData();
						this.userController.saveUser(this.client);
						ViewUtils.showMessage(this, ViewUtils.MSG_SAVED, TITLE,
								JOptionPane.INFORMATION_MESSAGE);
					}
				} else {
					ViewUtils.showMessage(this, MSG_VALUE, TITLE,
							JOptionPane.ERROR_MESSAGE);
				}
			} else {
				ViewUtils.showMessage(this, MSG_EMPTY_FIELDS, TITLE,
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void updateTextFields() {
		boolean visible = false;
		if (this.client == null) {
			this.updateTextFields("", "", "", "0");
		} else {
			final long value = Math.round(this.client.getValue().doubleValue());
			this.updateTextFields(
					String.valueOf(this.client.getIdentification()),
					this.client.getName(), this.client.getLastname(),
					String.valueOf(value));
			visible = true;
		}
		this.setUserFieldsVisibility(visible);
	}

	private void updateTextFields(final String identification,
			final String name, final String lastName, final String value) {
		this.jtfIdentification.setText(identification);
		this.jtfName.setText(name);
		this.jtfLastName.setText(lastName);
		this.jtfValue.setText(value);
	}

	private boolean validateUpdatedClientData() {
		final String identificationStr = this.jtfIdentification.getText()
				.replace(".", "").replace(",", "");
		final String name = this.jtfName.getText();
		final String lastName = this.jtfLastName.getText();
		return !identificationStr.equals("") && !name.equals("")
				&& !lastName.equals("");
	}

	private void updateClientData() {
		final String identificationStr = this.jtfIdentification.getText()
				.replace(".", "").replace(",", "");
		final String name = this.jtfName.getText();
		final String lastName = this.jtfLastName.getText();
		final Date currentDate = new Date();
		final String valueStr = this.jtfValue.getText().replace(".", "")
				.replace(",", "");
		final BigDecimal value = new BigDecimal(valueStr);
		this.client.setIdentification(Long.parseLong(identificationStr));
		this.client.setName(name);
		this.client.setLastname(lastName);
		this.client.setUpdated(currentDate);
		this.client.setValue(value);
	}

	private boolean validateValue() {
		final String valueStr = this.jtfValue.getText().replace(".", "")
				.replace(",", "");
		final BigDecimal value = new BigDecimal(valueStr);
		return value.doubleValue() >= this.client.getValue().doubleValue();
	}
}
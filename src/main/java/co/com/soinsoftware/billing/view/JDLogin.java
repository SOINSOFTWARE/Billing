package co.com.soinsoftware.billing.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import co.com.soinsoftware.billing.controller.LoginController;
import co.com.soinsoftware.billing.entity.User;

public class JDLogin extends JDialog implements ActionListener {

	/**
	 * Default serial version.
	 */
	private static final long serialVersionUID = 1L;

	private static final String EMPTY_LOGIN = "Por favor complete la casilla login";

	private static final String EMPTY_PASSWORD = "Por favor complete la casilla contraseña";

	private static final String TITLE = "Facturador - Login";

	private static final String WRONG_LOGIN = "¡Usuario o clave invalida!, intente nuevamente";
	
	private final LoginController controller;

	private JTextField jtfUser;

	private JPasswordField jpfPassword;

	private JButton jbtLogin;

	private JButton jbtCancel;

	/**
	 * Create the login frame.
	 */
	public JDLogin() {
		final Dimension screenSize = Toolkit.getDefaultToolkit()
				.getScreenSize();
		this.setJMenuBar(this.createMenuBar());
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setBackground(ViewUtils.GREY);
		this.setResizable(false);
		this.setTitle(TITLE);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setBounds((int) (screenSize.getWidth() / 2 - 240),
				(int) (screenSize.getHeight() / 2 - 150), 500, 330);
		final JPanel panel = this.createJpanel();
		this.setContentPane(panel);
		this.controller = new LoginController();
	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource().equals(jbtLogin)) {
			this.validateLogin();
		} else if (evt.getSource().equals(jbtCancel)) {
			System.exit(EXIT_ON_CLOSE);
		} else if (evt.getActionCommand().equals("Cambiar contraseña")) {

		} else if (evt.getActionCommand().equals("Recuperar contraseña")) {

		}
	}

	private JPanel createJpanel() {
		final JPanel panel = new JPanel();
		panel.setBackground(ViewUtils.GREY);
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.setLayout(null);

		final JLabel jlbUser = this.createJLabel("Login:", 175, 61);
		final JLabel jlbPassword = this.createJLabel("Contraseña:", 175, 112);
		jtfUser = this.createJTextField("login del usuario", 175, 81);
		jtfUser.setDocument(new JTextFieldLimit(10));
		jpfPassword = this.createJPasswordField("contrase\u00F1a", 175, 132);
		jpfPassword.setDocument(new JTextFieldLimit(10));
		jbtCancel = this.createJButton("Cancelar", KeyEvent.VK_C, 175, 175);
		jbtLogin = this.createJButton("Entrar", KeyEvent.VK_E, 272, 175);

		panel.add(jlbUser);
		panel.add(jtfUser);
		panel.add(jlbPassword);
		panel.add(jpfPassword);
		panel.add(jbtCancel);
		panel.add(jbtLogin);

		return panel;
	}

	private JLabel createJLabel(final String label, final int x, final int y) {
		final JLabel jlabel = new JLabel(label);
		jlabel.setForeground(Color.BLACK);
		jlabel.setFont(ViewUtils.VERDANA_BOLD);
		jlabel.setBounds(x, y, 83, 20);
		return jlabel;
	}

	private JTextField createJTextField(final String toolTip, final int x,
			final int y) {
		final JTextField textField = new JTextField();
		textField.setToolTipText(toolTip);
		textField.setFont(ViewUtils.VERDANA_PLAIN);
		textField.setColumns(10);
		textField.setBounds(x, y, 186, 20);
		return textField;
	}

	private JPasswordField createJPasswordField(final String toolTip,
			final int x, final int y) {
		final JPasswordField password = new JPasswordField();
		password.setToolTipText(toolTip);
		password.setFont(ViewUtils.VERDANA_PLAIN);
		password.setBounds(x, y, 186, 20);
		return password;
	}

	private JButton createJButton(final String label, final int key,
			final int x, final int y) {
		final JButton button = new JButton(label);
		button.setMnemonic(key);
		button.setForeground(Color.WHITE);
		button.setBackground(ViewUtils.BLUE);
		button.setFont(ViewUtils.VERDANA_BOLD);
		button.setBounds(x, y, 89, 23);
		button.addActionListener(this);
		return button;
	}

	private JMenuBar createMenuBar() {
		final JMenuBar menuBar = new JMenuBar();
		final JMenu menu = new JMenu("Opciones");
		final JMenuItem miChangePass = this.createJMenuItem(
				"Cambiar contraseña", KeyEvent.VK_H,
				KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
		final JMenuItem miGetPass = this.createJMenuItem(
				"Recuperar contraseña", KeyEvent.VK_R,
				KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
		menu.setMnemonic(KeyEvent.VK_O);
		menu.add(miChangePass);
		menu.add(miGetPass);
		menuBar.add(menu);
		return menuBar;
	}

	private JMenuItem createJMenuItem(final String label, final int key,
			final KeyStroke keyStroke) {
		final JMenuItem menuItem = new JMenuItem(label, key);
		menuItem.setAccelerator(keyStroke);
		menuItem.addActionListener(this);
		return menuItem;
	}

	private void validateLogin() {
		final String login = this.jtfUser.getText().trim();
		final char[] password = this.jpfPassword.getPassword();
		final int infoMessage = JOptionPane.INFORMATION_MESSAGE;
		if (login.equals("")) {
			ViewUtils.showMessage(this, EMPTY_LOGIN, TITLE, infoMessage);
		} else if (password.length == 0) {
			ViewUtils.showMessage(this, EMPTY_PASSWORD, TITLE, infoMessage);
		} else {
			final User user = controller.selectUser(login, password);
			if (user != null) {
				this.setVisible(false);
				// new JFOwnerAndPetsInfo().setVisible(true);
			} else {
				ViewUtils.showMessage(this, WRONG_LOGIN, TITLE, infoMessage);
			}
		}
	}
}

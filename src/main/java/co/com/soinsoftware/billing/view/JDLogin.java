package co.com.soinsoftware.billing.view;

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
import co.com.soinsoftware.billing.controller.MenuController;
import co.com.soinsoftware.billing.entity.User;

/**
 * @author Carlos Rodriguez
 * @since 30/05/2016
 * @version 1.0
 */
public class JDLogin extends JDialog implements ActionListener {

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

	public void actionPerformed(final ActionEvent evt) {
		final Object source = evt.getSource();
		if (source.equals(this.jbtLogin)) {
			this.validateLogin();
		} else if (source.equals(this.jbtCancel)) {
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
		final JLabel jlbUser = ViewUtils.createJLabel("Login:", 175, 61);
		final JLabel jlbPass = ViewUtils.createJLabel("Contraseña:", 175, 112);

		this.jtfUser = ViewUtils.createJTextField("login del usuario", 175, 81);
		this.jtfUser.setDocument(new JTextFieldLimit(10));
		this.jpfPassword = ViewUtils.createJPasswordField("contraseña", 175,
				132);
		this.jpfPassword.setDocument(new JTextFieldLimit(10));
		this.jbtCancel = ViewUtils.createJButton("Cancelar", KeyEvent.VK_C,
				175, 175);
		this.jbtLogin = ViewUtils.createJButton("Entrar", KeyEvent.VK_E, 272,
				175);
		this.jbtCancel.addActionListener(this);
		jbtLogin.addActionListener(this);

		panel.add(jlbUser);
		panel.add(this.jtfUser);
		panel.add(jlbPass);
		panel.add(this.jpfPassword);
		panel.add(this.jbtCancel);
		panel.add(this.jbtLogin);
		return panel;
	}

	private JMenuBar createMenuBar() {
		final JMenuBar menuBar = new JMenuBar();
		final JMenu menu = new JMenu("Opciones");
		final JMenuItem miChangePass = ViewUtils.createJMenuItem(
				"Cambiar contraseña", KeyEvent.VK_H,
				KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
		miChangePass.addActionListener(this);
		final JMenuItem miGetPass = ViewUtils.createJMenuItem(
				"Recuperar contraseña", KeyEvent.VK_R,
				KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
		miGetPass.addActionListener(this);
		menu.setMnemonic(KeyEvent.VK_O);
		menu.add(miChangePass);
		menu.add(miGetPass);
		menuBar.add(menu);
		return menuBar;
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
				this.createAppFrames(user);
			} else {
				ViewUtils.showMessage(this, WRONG_LOGIN, TITLE, infoMessage);
			}
		}
	}

	private void createAppFrames(final User user) {
		final JFReportReceipt mainFrame = new JFReportReceipt(user);
		final JFUser userFrame = new JFUser(user);
		final JFReceipt receiptFrame = new JFReceipt(user);
		final MenuController menuController = new MenuController(mainFrame,
				userFrame, receiptFrame);
		mainFrame.addController(menuController);
		userFrame.addController(menuController);
		receiptFrame.addController(menuController);
		mainFrame.setVisible(true);
	}
}
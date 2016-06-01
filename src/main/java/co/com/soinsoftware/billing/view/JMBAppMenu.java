package co.com.soinsoftware.billing.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import co.com.soinsoftware.billing.controller.MenuController;

/**
 * @author Carlos Rodriguez
 * @since 30/05/2016
 * @version 1.0
 */
public class JMBAppMenu extends JMenuBar implements ActionListener {

	private static final long serialVersionUID = 2306440560901177958L;

	private static final String MENU_CREATE_RECEIPT = "Crear recibo";

	private static final String MENU_CREATE_USER = "Crear usuario";

	private MenuController controller;

	public JMBAppMenu(final MenuController controller) {
		super();
		this.addMenuNew();
		this.controller = controller;
	}
	
	public void refresh() {
		this.controller.getUserFrame().refresh();
		this.controller.getReceiptFrame().refresh();
	}

	public void actionPerformed(final ActionEvent evt) {
		this.refresh();
		final String actionCommand = evt.getActionCommand();
		if (actionCommand.equals(MENU_CREATE_USER)) {
			this.controller.getMainFrame().setVisible(false);
			this.controller.getReceiptFrame().setVisible(false);
			this.controller.getUserFrame().setVisible(true);
		} else if (actionCommand.equals(MENU_CREATE_RECEIPT)) {
			this.controller.getMainFrame().setVisible(false);
			this.controller.getUserFrame().setVisible(false);
			this.controller.getReceiptFrame().setVisible(true);
		}
	}

	private void addMenuNew() {
		final JMenu menu = new JMenu("Nuevo");
		menu.setMnemonic(KeyEvent.VK_N);
		final JMenuItem miNewUser = ViewUtils.createJMenuItem(MENU_CREATE_USER,
				KeyEvent.VK_U,
				KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
		final JMenuItem miNewReceipt = ViewUtils.createJMenuItem(
				MENU_CREATE_RECEIPT, KeyEvent.VK_R,
				KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
		miNewUser.addActionListener(this);
		miNewReceipt.addActionListener(this);
		menu.add(miNewUser);
		menu.add(miNewReceipt);
		this.add(menu);
	}
}
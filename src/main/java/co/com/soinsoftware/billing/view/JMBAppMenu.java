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

	private static final String MENU_NEW = "Nuevo";

	private static final String MENU_VIEW = "Ver";

	private static final String MENU_VIEW_RECEIPT = "Ver recibos";

	private static final String MENU_VIEW_USER = "Ver usuarios";

	private MenuController controller;

	public JMBAppMenu(final MenuController controller) {
		super();
		this.addMenuNew();
		this.addMenuReport();
		this.controller = controller;
	}

	public void refresh() {
		this.controller.getUserFrame().refresh();
		this.controller.getReceiptFrame().refresh();
		this.controller.getReportFrame().refresh();
		this.controller.getViewUserFrame().refresh();
	}

	public void actionPerformed(final ActionEvent evt) {
		this.refresh();
		final String actionCommand = evt.getActionCommand();
		if (actionCommand.equals(MENU_CREATE_USER)) {
			this.controller.getReportFrame().setVisible(false);
			this.controller.getReceiptFrame().setVisible(false);
			this.controller.getViewUserFrame().setVisible(false);
			this.controller.getUserFrame().setVisible(true);
		} else if (actionCommand.equals(MENU_CREATE_RECEIPT)) {
			this.controller.getReportFrame().setVisible(false);
			this.controller.getUserFrame().setVisible(false);
			this.controller.getViewUserFrame().setVisible(false);
			this.controller.getReceiptFrame().setVisible(true);
		} else if (actionCommand.equals(MENU_VIEW_USER)) {
			this.controller.getUserFrame().setVisible(false);
			this.controller.getReceiptFrame().setVisible(false);
			this.controller.getReportFrame().setVisible(false);
			this.controller.getViewUserFrame().setVisible(true);
		} else if (actionCommand.equals(MENU_VIEW_RECEIPT)) {
			this.controller.getUserFrame().setVisible(false);
			this.controller.getReceiptFrame().setVisible(false);
			this.controller.getViewUserFrame().setVisible(false);
			this.controller.getReportFrame().setVisible(true);
		}
	}

	private void addMenuNew() {
		final JMenu menu = new JMenu(MENU_NEW);
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

	private void addMenuReport() {
		final JMenu menu = new JMenu(MENU_VIEW);
		menu.setMnemonic(KeyEvent.VK_V);
		final JMenuItem miViewUser = ViewUtils.createJMenuItem(MENU_VIEW_USER,
				KeyEvent.VK_R,
				KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.ALT_MASK));
		final JMenuItem miViewReceipt = ViewUtils.createJMenuItem(
				MENU_VIEW_RECEIPT, KeyEvent.VK_R,
				KeyStroke.getKeyStroke(KeyEvent.VK_4, ActionEvent.ALT_MASK));
		miViewUser.addActionListener(this);
		miViewReceipt.addActionListener(this);
		menu.add(miViewUser);
		menu.add(miViewReceipt);
		this.add(menu);
	}
}
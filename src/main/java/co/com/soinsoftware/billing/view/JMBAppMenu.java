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

	private static final String MENU_DEACTIVATE = "Eliminar";

	private static final String MENU_DEACTIVATE_CONCEPT = "Eliminar concepto";

	private static final String MENU_DEACTIVATE_RECEIPT = "Eliminar recibo";

	private static final String MENU_NEW = "Nuevo";

	private static final String MENU_NEW_CONCEPT = "Crear concepto";

	private static final String MENU_NEW_RECEIPT = "Crear recibo";

	private static final String MENU_NEW_USER = "Crear usuario";

	private static final String MENU_VIEW = "Ver";

	private static final String MENU_VIEW_RECEIPT = "Ver recibos";

	private static final String MENU_VIEW_USER = "Ver usuarios";

	private MenuController controller;

	public JMBAppMenu(final MenuController controller) {
		super();
		this.addMenuNew();
		this.addMenuReport();
		this.addMenuDeactivate();
		this.controller = controller;
	}

	public void refresh() {
		this.controller.getUserFrame().refresh();
		this.controller.getReceiptFrame().refresh();
		this.controller.getReportFrame().refresh();
		this.controller.getViewUserFrame().refresh();
		this.controller.getDeactivateReceiptFrame().refresh();
		this.controller.getItemConceptFrame().refresh();
		this.controller.getDeactivateItemConceptFrame().refresh();
	}

	public void actionPerformed(final ActionEvent evt) {
		this.refresh();
		final String actionCommand = evt.getActionCommand();
		switch (actionCommand) {
		case MENU_NEW_USER:
			this.showNewUserFrame();
			break;
		case MENU_NEW_RECEIPT:
			this.showNewReceiptFrame();
			break;
		case MENU_VIEW_USER:
			this.showViewUserFrame();
			break;
		case MENU_VIEW_RECEIPT:
			this.showViewReceiptFrame();
			break;
		case MENU_DEACTIVATE_RECEIPT:
			this.showDeactivateReceiptFrame();
			break;
		case MENU_NEW_CONCEPT:
			this.showItemConceptFrame();
			break;
		case MENU_DEACTIVATE_CONCEPT:
			this.showDeactivateItemConceptFrame();
			break;
		}
	}

	private void addMenuNew() {
		final JMenu menu = new JMenu(MENU_NEW);
		menu.setMnemonic(KeyEvent.VK_N);
		final JMenuItem miNewUser = ViewUtils.createJMenuItem(MENU_NEW_USER,
				KeyEvent.VK_U,
				KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
		final JMenuItem miNewReceipt = ViewUtils.createJMenuItem(
				MENU_NEW_RECEIPT, KeyEvent.VK_R,
				KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
		final JMenuItem miNewConcept = ViewUtils.createJMenuItem(
				MENU_NEW_CONCEPT, KeyEvent.VK_I,
				KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.ALT_MASK));
		miNewUser.addActionListener(this);
		miNewReceipt.addActionListener(this);
		miNewConcept.addActionListener(this);
		menu.add(miNewUser);
		menu.add(miNewReceipt);
		menu.add(miNewConcept);
		this.add(menu);
	}

	private void addMenuReport() {
		final JMenu menu = new JMenu(MENU_VIEW);
		menu.setMnemonic(KeyEvent.VK_V);
		final JMenuItem miViewUser = ViewUtils.createJMenuItem(MENU_VIEW_USER,
				KeyEvent.VK_R,
				KeyStroke.getKeyStroke(KeyEvent.VK_4, ActionEvent.ALT_MASK));
		final JMenuItem miViewReceipt = ViewUtils.createJMenuItem(
				MENU_VIEW_RECEIPT, KeyEvent.VK_R,
				KeyStroke.getKeyStroke(KeyEvent.VK_5, ActionEvent.ALT_MASK));
		miViewUser.addActionListener(this);
		miViewReceipt.addActionListener(this);
		menu.add(miViewUser);
		menu.add(miViewReceipt);
		this.add(menu);
	}

	private void addMenuDeactivate() {
		final JMenu menu = new JMenu(MENU_DEACTIVATE);
		menu.setMnemonic(KeyEvent.VK_E);
		final JMenuItem miDeactivateReceipt = ViewUtils.createJMenuItem(
				MENU_DEACTIVATE_RECEIPT, KeyEvent.VK_E,
				KeyStroke.getKeyStroke(KeyEvent.VK_6, ActionEvent.ALT_MASK));
		final JMenuItem miDeactivateConcept = ViewUtils.createJMenuItem(
				MENU_DEACTIVATE_CONCEPT, KeyEvent.VK_C,
				KeyStroke.getKeyStroke(KeyEvent.VK_7, ActionEvent.ALT_MASK));
		miDeactivateReceipt.addActionListener(this);
		miDeactivateConcept.addActionListener(this);
		menu.add(miDeactivateReceipt);
		menu.add(miDeactivateConcept);
		this.add(menu);
	}

	private void showNewUserFrame() {
		this.controller.getReportFrame().setVisible(false);
		this.controller.getReceiptFrame().setVisible(false);
		this.controller.getViewUserFrame().setVisible(false);
		this.controller.getDeactivateReceiptFrame().setVisible(false);
		this.controller.getItemConceptFrame().setVisible(false);
		this.controller.getDeactivateItemConceptFrame().setVisible(false);
		this.controller.getUserFrame().setVisible(true);
	}

	private void showNewReceiptFrame() {
		this.controller.getReportFrame().setVisible(false);
		this.controller.getUserFrame().setVisible(false);
		this.controller.getViewUserFrame().setVisible(false);
		this.controller.getDeactivateReceiptFrame().setVisible(false);
		this.controller.getItemConceptFrame().setVisible(false);
		this.controller.getDeactivateItemConceptFrame().setVisible(false);
		this.controller.getReceiptFrame().setVisible(true);
	}

	private void showViewUserFrame() {
		this.controller.getUserFrame().setVisible(false);
		this.controller.getReceiptFrame().setVisible(false);
		this.controller.getReportFrame().setVisible(false);
		this.controller.getDeactivateReceiptFrame().setVisible(false);
		this.controller.getItemConceptFrame().setVisible(false);
		this.controller.getDeactivateItemConceptFrame().setVisible(false);
		this.controller.getViewUserFrame().setVisible(true);
	}

	private void showViewReceiptFrame() {
		this.controller.getUserFrame().setVisible(false);
		this.controller.getReceiptFrame().setVisible(false);
		this.controller.getViewUserFrame().setVisible(false);
		this.controller.getDeactivateReceiptFrame().setVisible(false);
		this.controller.getItemConceptFrame().setVisible(false);
		this.controller.getDeactivateItemConceptFrame().setVisible(false);
		this.controller.getReportFrame().setVisible(true);
	}

	private void showDeactivateReceiptFrame() {
		this.controller.getUserFrame().setVisible(false);
		this.controller.getReceiptFrame().setVisible(false);
		this.controller.getViewUserFrame().setVisible(false);
		this.controller.getReportFrame().setVisible(false);
		this.controller.getItemConceptFrame().setVisible(false);
		this.controller.getDeactivateItemConceptFrame().setVisible(false);
		this.controller.getDeactivateReceiptFrame().setVisible(true);
	}

	private void showItemConceptFrame() {
		this.controller.getUserFrame().setVisible(false);
		this.controller.getReceiptFrame().setVisible(false);
		this.controller.getViewUserFrame().setVisible(false);
		this.controller.getReportFrame().setVisible(false);
		this.controller.getDeactivateReceiptFrame().setVisible(false);
		this.controller.getDeactivateItemConceptFrame().setVisible(false);
		this.controller.getItemConceptFrame().setVisible(true);
	}

	private void showDeactivateItemConceptFrame() {
		this.controller.getUserFrame().setVisible(false);
		this.controller.getReceiptFrame().setVisible(false);
		this.controller.getViewUserFrame().setVisible(false);
		this.controller.getReportFrame().setVisible(false);
		this.controller.getDeactivateReceiptFrame().setVisible(false);
		this.controller.getItemConceptFrame().setVisible(false);
		this.controller.getDeactivateItemConceptFrame().setVisible(true);
	}
}
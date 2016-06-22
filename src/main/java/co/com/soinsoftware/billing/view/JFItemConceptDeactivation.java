package co.com.soinsoftware.billing.view;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import co.com.soinsoftware.billing.controller.ItemConceptController;
import co.com.soinsoftware.billing.controller.MenuController;
import co.com.soinsoftware.billing.entity.Itemconcept;
import co.com.soinsoftware.billing.entity.User;

/**
 * @author Carlos Rodriguez
 * @since 15/06/2016
 * @version 1.0
 */
public class JFItemConceptDeactivation extends JFrame implements ActionListener {

	private static final long serialVersionUID = 7962825337634660703L;

	private static final String TITLE = "Facturador - Eliminar concepto de facturación";

	private static final String MSG_NO_SELECTION = "Debe seleccionar un elemento para realizar la eliminación";

	private static final String MSG_SAVE_QUESTION = "¿Está seguro que desea eliminar el concepto?, Una vez realizada la eliminación el concepto no aparecerá en nuevas factura.";

	private final User loggedUser;

	private final ItemConceptController itemConceptController;

	private final JPanel panel;

	private JList<String> jlsItemConcept;

	private JScrollPane jspItemConcept;

	private JButton jbtClean;

	private JButton jbtDeactivate;

	public JFItemConceptDeactivation(final User loggedUser) {
		super();
		this.loggedUser = loggedUser;
		this.itemConceptController = new ItemConceptController();
		this.setBackground(ViewUtils.GREY);
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle(this.loggedUser.getCompany().getName() + " - " + TITLE);
		this.panel = this.createPanel();
		this.setContentPane(panel);
	}

	public void addController(final MenuController controller) {
		final JMenuBar menuBar = new JMBAppMenu(controller);
		this.setJMenuBar(menuBar);
	}

	public void refresh() {
		this.jlsItemConcept.setListData(this.getActiveItemConcepts());
		this.jlsItemConcept.clearSelection();
	}

	@Override
	public void actionPerformed(final ActionEvent evt) {
		final Object source = evt.getSource();
		if (source.equals(this.jbtClean)) {
			this.refresh();
		} else if (source.equals(this.jbtDeactivate)) {
			this.saveAction();
		}
	}

	private void saveAction() {
		if (!this.jlsItemConcept.isSelectionEmpty()) {
			final int confirm = ViewUtils.showConfirmDialog(this,
					MSG_SAVE_QUESTION, TITLE);
			if (confirm == JOptionPane.OK_OPTION) {
				final String name = this.jlsItemConcept.getSelectedValue();
				final Itemconcept itemConcept = this.itemConceptController
						.getItemConceptByName(this.loggedUser, name);
				itemConcept.setEnabled(false);
				itemConcept.setUpdated(new Date());
				this.itemConceptController.saveItemConcept(this.loggedUser,
						itemConcept);
				ViewUtils.showMessage(this, ViewUtils.MSG_SAVED, TITLE,
						JOptionPane.INFORMATION_MESSAGE);
				this.refresh();
				this.jlsItemConcept.setListData(this.getActiveItemConcepts());
			}
		} else {
			ViewUtils.showMessage(this, MSG_NO_SELECTION, TITLE,
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private JPanel createPanel() {
		final JPanel panel = new JPanel();
		panel.setBackground(ViewUtils.GREY);
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.setLayout(null);

		final JLabel jlbTitle = ViewUtils.createJLabel("ELIMINAR CONCEPTO", 30,
				10);
		final JLabel jlbItemConcept = ViewUtils.createJLabel(
				"Conceptos de facturación", 30, 40);
		this.jlsItemConcept = ViewUtils.createJList(null,
				this.getActiveItemConcepts(), 30, 120);
		this.jspItemConcept = new JScrollPane(this.jlsItemConcept);
		this.jspItemConcept.setBounds(30, 70, 200, 200);

		this.jbtClean = ViewUtils.createJButton("Limpiar", KeyEvent.VK_L, 30,
				300);
		this.jbtDeactivate = ViewUtils.createJButton("Eliminar", KeyEvent.VK_S,
				123, 300);
		this.jbtClean.addActionListener(this);
		this.jbtDeactivate.addActionListener(this);

		panel.add(jlbTitle);
		panel.add(jlbItemConcept);
		panel.add(this.jspItemConcept);
		panel.add(this.jbtClean);
		panel.add(this.jbtDeactivate);
		ViewUtils.buildSoinSoftwareLabel(this.getSize(), panel);
		return panel;
	}

	private String[] getActiveItemConcepts() {
		String[] itemConceptArray = null;
		final List<Itemconcept> itemConceptList = this.itemConceptController
				.getItemConceptList(this.loggedUser, true);
		if (itemConceptList != null) {
			itemConceptArray = new String[itemConceptList.size()];
			for (int i = 0; i < itemConceptList.size(); i++) {
				if (itemConceptList.get(i).getId() != 7) {
					itemConceptArray[i] = itemConceptList.get(i).getName();
				}
			}
		}
		return itemConceptArray;
	}
}
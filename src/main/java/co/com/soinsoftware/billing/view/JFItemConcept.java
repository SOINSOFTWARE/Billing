package co.com.soinsoftware.billing.view;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
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
public class JFItemConcept extends JFrame implements ActionListener {

	private static final long serialVersionUID = 7962825337634660703L;

	private static final String TITLE = "Facturador - Concepto de facturación";

	private static final String MSG_EMPTY_FIELDS = "Por favor complete el campo nombre";

	private final User loggedUser;

	private final ItemConceptController itemConceptController;
	
	private final JPanel panel;

	private JTextField jtfName;

	private JList<String> jlsItemConcept;

	private JScrollPane jspItemConcept;

	private JButton jbtClean;

	private JButton jbtSave;

	public JFItemConcept(final User loggedUser) {
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
		this.cleanFields();
		this.jlsItemConcept.setListData(this.getActiveItemConcepts());
	}

	@Override
	public void actionPerformed(final ActionEvent evt) {
		final Object source = evt.getSource();
		if (source.equals(this.jbtClean)) {
			this.cleanFields();
		} else if (source.equals(this.jbtSave)) {
			this.saveAction();
		}
	}

	private void cleanFields() {
		this.jtfName.setText("");
	}

	private void saveAction() {
		final String name = this.jtfName.getText();
		if (!name.trim().equals("")) {
			this.itemConceptController.saveItemConcept(this.loggedUser, name);
			ViewUtils.showMessage(this, ViewUtils.MSG_SAVED, TITLE,
					JOptionPane.INFORMATION_MESSAGE);
			this.cleanFields();
			this.jlsItemConcept.setListData(this.getActiveItemConcepts());
		} else {
			ViewUtils.showMessage(this, MSG_EMPTY_FIELDS, TITLE,
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private JPanel createPanel() {
		final JPanel panel = new JPanel();
		panel.setBackground(ViewUtils.GREY);
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.setLayout(null);

		final JLabel jlbTitle = ViewUtils
				.createJLabel("NUEVO CONCEPTO", 30, 10);
		final JLabel jlbName = ViewUtils.createJLabel("Nombre(s):", 30, 40);

		this.jtfName = ViewUtils.createJTextField(null, 30, 70);
		this.jtfName.setDocument(new JTextFieldLimit(60));

		this.jbtClean = ViewUtils.createJButton("Limpiar", KeyEvent.VK_L, 30,
				100);
		this.jbtSave = ViewUtils.createJButton("Guardar", KeyEvent.VK_S, 123,
				100);
		this.jbtClean.addActionListener(this);
		this.jbtSave.addActionListener(this);

		final JLabel jlbItemConcept = ViewUtils.createJLabel("Conceptos de facturación", 425, 10);
		this.jlsItemConcept = ViewUtils.createJList(null,
				this.getActiveItemConcepts(), 30, 120);
		this.jlsItemConcept.setEnabled(false);
		this.jspItemConcept = new JScrollPane(this.jlsItemConcept);
		this.jspItemConcept.setBounds(425, 40, 200, 200);

		panel.add(jlbTitle);
		panel.add(jlbName);
		panel.add(this.jtfName);
		panel.add(this.jbtClean);
		panel.add(this.jbtSave);
		panel.add(jlbItemConcept);
		panel.add(this.jspItemConcept);
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
				itemConceptArray[i] = itemConceptList.get(i).getName();
			}
		}
		return itemConceptArray;
	}
}
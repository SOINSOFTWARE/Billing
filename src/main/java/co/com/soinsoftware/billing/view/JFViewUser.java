/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soinsoftware.billing.view;

import java.awt.GraphicsEnvironment;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import co.com.soinsoftware.billing.controller.CreditController;
import co.com.soinsoftware.billing.controller.MenuController;
import co.com.soinsoftware.billing.controller.UserController;
import co.com.soinsoftware.billing.entity.Credit;
import co.com.soinsoftware.billing.entity.Credittype;
import co.com.soinsoftware.billing.entity.User;

/**
 * @author Carlos Rodriguez
 * @since 02/06/2016
 * @version 1.0
 */
public class JFViewUser extends JFrame {

    private static final long serialVersionUID = -4200612234138262654L;

    private static final String TITLE = "Cooperativo - Ver usuarios";

    private static final String MSG_EMPTY_ID = "El campo identificación debe ser completado para realizar la busqueda";

    private static final String MSG_NO_CLIENT = "No existen usuarios con el número de cedula especificado";

    private static final String MSG_SAVE_QUESTION = "¿Está seguro que desea guardar los cambios en el usuario?";

    private static final String MSG_EMPTY_FIELDS = "Debe completar todos los campos para poder actualizar";

    private static final String MSG_VALUE = "La deuda no puede ser disminuida en este modulo";
    
    private static final String[] CREDIT_COLUMN_NAMES = {"Fecha credito", "Valor"};

    private final User loggedUser;

    private final UserController userController;

    private User client;
    
    private final CreditController creditController;
	
	private final Set<Credittype> creditTypeSet;

    /**
     * Creates new form JFViewUser
     *
     * @param loggedUser
     */
    public JFViewUser(final User loggedUser) {
        super();
        this.loggedUser = loggedUser;
        this.initComponents();
        this.userController = new UserController();  
        this.creditController = new CreditController();
        this.setTextFieldLimits();
        this.setUserFieldsVisibility(false);
        this.setTitle(this.loggedUser.getCompany().getName() + " - " + TITLE);
        this.setMaximized();
        this.creditTypeSet = this.creditController.selectCreditType();
		this.fillCreditTypeComboBox();
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
    
    private void setMaximized() {
        final GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        this.setMaximizedBounds(env.getMaximumWindowBounds());
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }
    
    private void fillCreditTypeComboBox() {
		final DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
		model.addElement("Seleccione uno...");
		for (final Credittype creditType : this.creditTypeSet) {
			model.addElement(creditType.getName());
		}
		this.jcbCreditType.setModel(model);
		this.jtfValue.setEditable(false);
	}

    private void setTextFieldLimits() {
        this.jtfName.setDocument(new JTextFieldLimit(45));
        this.jtfLastName.setDocument(new JTextFieldLimit(45));
    }

    private void setUserFieldsVisibility(final boolean visible) {
        this.pnUser.setVisible(visible);
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
            this.loadCreditHistory();
            visible = true;
        }
        this.setUserFieldsVisibility(visible);
    }

    private void updateTextFields(final String identification,
            final String name, final String lastName, final String value) {
        this.jtfIdentification.setText(identification);
        this.jtfName.setText(name);
        this.jtfLastName.setText(lastName);
        this.jtfDebtValue.setText(value);
        this.jcbCreditType.setSelectedIndex(0);
        this.jtfValue.setText("0");
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
        this.client.setValue(this.client.getValue().add(value));
    }

    private boolean validateValue() {
        final String valueStr = this.jtfValue.getText().replace(".", "")
                .replace(",", "");
        final BigDecimal value = new BigDecimal(valueStr);
        return value.doubleValue() > 0;
    }
    
    private void saveCreditInformation(final User user) {
		final String valueStr = this.jtfValue.getText().replace(".", "")
				.replace(",", "");
		final BigDecimal value = new BigDecimal(valueStr);
		if (value.doubleValue() > 0) {
			for (final Credittype creditType : this.creditTypeSet) {
				if (creditType.getName().equals(this.jcbCreditType.getSelectedItem())) {
					final Date currentDate = new Date();
					final Credit credit = new Credit(creditType, user, value, currentDate, currentDate, true);
					this.creditController.saveCredit(credit);
					this.client.getCredits().add(credit);
					this.updateTextFields();
					break;
				}
			}
		}
	}
    
	private void loadCreditHistory() {
		if (this.client != null && this.client.getCredits() != null) {
			final Object[][] data = this.creditController
					.buildCreditData(this.client.getCredits());
			final DefaultTableModel tableModel = new DefaultTableModel(data,
					CREDIT_COLUMN_NAMES);
			this.jtbCredit.setModel(tableModel);
			this.jtbCredit.setFillsViewportHeight(true);
	        this.jtbCredit.setEnabled(false);
	        for (int i = 0; i < 2; i++) {
	            final TableColumn column = this.jtbCredit.getColumnModel()
	                    .getColumn(i);
	            column.setResizable(false);
	            if (i == 1) {
	                column.setPreferredWidth(100);
	            } else {
	                column.setPreferredWidth(100);
	            }
	        }
	        this.jtbCredit.repaint();
	        this.jspCredit.setVisible(true);
	        this.pnUser.revalidate();
	        this.pnUser.repaint();
		} else {
			this.jspCredit.setVisible(false);
		}
	}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnTitle = new javax.swing.JPanel();
        lbTitle = new javax.swing.JLabel();
        pnSearch = new javax.swing.JPanel();
        jlbSearchIdentification = new javax.swing.JLabel();
        jtfSearchIdentification = new javax.swing.JFormattedTextField();
        jbtSearch = new javax.swing.JButton();
        pnUser = new javax.swing.JPanel();
        jlbIdentification = new javax.swing.JLabel();
        jtfIdentification = new javax.swing.JFormattedTextField();
        jlbName = new javax.swing.JLabel();
        jtfName = new javax.swing.JTextField();
        jlbLastName = new javax.swing.JLabel();
        jtfLastName = new javax.swing.JTextField();
        jlbValue = new javax.swing.JLabel();
        jtfDebtValue = new javax.swing.JFormattedTextField();
        jbtSave = new javax.swing.JButton();
        jcbCreditType = new javax.swing.JComboBox<String>();
        jlbValue1 = new javax.swing.JLabel();
        jtfValue = new javax.swing.JFormattedTextField();
        jlbCreditType = new javax.swing.JLabel();
        jspCredit = new javax.swing.JScrollPane();
        jtbCredit = new javax.swing.JTable();
        lbImage = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(520, 430));

        pnTitle.setBackground(new java.awt.Color(255, 255, 255));
        pnTitle.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnTitle.setToolTipText("");
        pnTitle.setName("pnTitle"); // NOI18N

        lbTitle.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        lbTitle.setText("Ver usuario");

        javax.swing.GroupLayout pnTitleLayout = new javax.swing.GroupLayout(pnTitle);
        pnTitle.setLayout(pnTitleLayout);
        pnTitleLayout.setHorizontalGroup(
            pnTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbTitle)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnTitleLayout.setVerticalGroup(
            pnTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbTitle)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnSearch.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Buscar", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 1, 12))); // NOI18N
        pnSearch.setName("pnSearch"); // NOI18N
        pnSearch.setPreferredSize(new java.awt.Dimension(300, 200));

        jlbSearchIdentification.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jlbSearchIdentification.setText("Identificación:");

        jtfSearchIdentification.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0"))));
        jtfSearchIdentification.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N

        jbtSearch.setBackground(new java.awt.Color(16, 135, 221));
        jbtSearch.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jbtSearch.setForeground(new java.awt.Color(255, 255, 255));
        jbtSearch.setText("Buscar");
        jbtSearch.setPreferredSize(new java.awt.Dimension(89, 23));
        jbtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnSearchLayout = new javax.swing.GroupLayout(pnSearch);
        pnSearch.setLayout(pnSearchLayout);
        pnSearchLayout.setHorizontalGroup(
            pnSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnSearchLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnSearchLayout.createSequentialGroup()
                        .addGroup(pnSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlbSearchIdentification)
                            .addComponent(jtfSearchIdentification, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnSearchLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jbtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnSearchLayout.setVerticalGroup(
            pnSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnSearchLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlbSearchIdentification)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfSearchIdentification, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jbtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnUser.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Usuario", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 1, 12))); // NOI18N
        pnUser.setName("pnUser"); // NOI18N
        pnUser.setPreferredSize(new java.awt.Dimension(300, 200));

        jlbIdentification.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jlbIdentification.setText("Identificación:");

        jtfIdentification.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0"))));
        jtfIdentification.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N

        jlbName.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jlbName.setText("Nombre(s):");

        jtfName.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        jtfName.setPreferredSize(new java.awt.Dimension(200, 20));

        jlbLastName.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jlbLastName.setText("Apellido(s):");

        jtfLastName.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        jtfLastName.setPreferredSize(new java.awt.Dimension(200, 20));

        jlbValue.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jlbValue.setText("Deuda:");

        jtfDebtValue.setEditable(false);
        jtfDebtValue.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0"))));
        jtfDebtValue.setText("0");
        jtfDebtValue.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N

        jbtSave.setBackground(new java.awt.Color(16, 135, 221));
        jbtSave.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jbtSave.setForeground(new java.awt.Color(255, 255, 255));
        jbtSave.setText("Guardar");
        jbtSave.setPreferredSize(new java.awt.Dimension(89, 23));
        jbtSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtSaveActionPerformed(evt);
            }
        });

        jcbCreditType.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        jcbCreditType.setModel(new javax.swing.DefaultComboBoxModel<String>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbCreditType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbCreditTypeActionPerformed(evt);
            }
        });

        jlbValue1.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jlbValue1.setText("Cantidad($):");

        jtfValue.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0"))));
        jtfValue.setText("0");
        jtfValue.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N

        jlbCreditType.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jlbCreditType.setText("Tipo de credito:");

        jtbCredit.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jspCredit.setViewportView(jtbCredit);

        javax.swing.GroupLayout pnUserLayout = new javax.swing.GroupLayout(pnUser);
        pnUser.setLayout(pnUserLayout);
        pnUserLayout.setHorizontalGroup(
            pnUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnUserLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlbValue1)
                    .addGroup(pnUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jbtSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(pnUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jtfValue, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtfDebtValue)
                            .addGroup(pnUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(pnUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jlbName)
                                    .addComponent(jlbIdentification)
                                    .addComponent(jlbLastName)
                                    .addComponent(jtfLastName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jtfName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jtfIdentification))
                                .addComponent(jlbValue))
                            .addComponent(jlbCreditType, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jcbCreditType, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(18, 18, 18)
                .addComponent(jspCredit, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnUserLayout.setVerticalGroup(
            pnUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnUserLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnUserLayout.createSequentialGroup()
                        .addComponent(jlbIdentification)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfIdentification, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(jlbName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(jlbLastName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jlbValue)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfDebtValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jlbCreditType)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcbCreditType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jlbValue1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jspCredit, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jbtSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lbImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/soin.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 231, Short.MAX_VALUE)
                .addComponent(lbImage, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(115, 115, 115))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnUser, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnUser, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbImage, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtSearchActionPerformed
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
    }//GEN-LAST:event_jbtSearchActionPerformed

    private void jbtSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtSaveActionPerformed
        if (this.client != null && this.jbtSave.isVisible()) {
            if (this.validateUpdatedClientData()) {
                if (this.validateValue()) {
                    final int confirm = ViewUtils.showConfirmDialog(this,
                            MSG_SAVE_QUESTION, TITLE);
                    if (confirm == JOptionPane.OK_OPTION) {
                        this.updateClientData();
                        this.userController.saveUser(this.client);
                        this.saveCreditInformation(this.client);
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
    }//GEN-LAST:event_jbtSaveActionPerformed

    private void jcbCreditTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbCreditTypeActionPerformed
        this.jtfValue.setEditable(this.jcbCreditType.getSelectedIndex() > 0);
        this.jtfValue.setText("0");
    }//GEN-LAST:event_jcbCreditTypeActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jbtSave;
    private javax.swing.JButton jbtSearch;
    private javax.swing.JComboBox<String> jcbCreditType;
    private javax.swing.JLabel jlbCreditType;
    private javax.swing.JLabel jlbIdentification;
    private javax.swing.JLabel jlbLastName;
    private javax.swing.JLabel jlbName;
    private javax.swing.JLabel jlbSearchIdentification;
    private javax.swing.JLabel jlbValue;
    private javax.swing.JLabel jlbValue1;
    private javax.swing.JScrollPane jspCredit;
    private javax.swing.JTable jtbCredit;
    private javax.swing.JFormattedTextField jtfDebtValue;
    private javax.swing.JFormattedTextField jtfIdentification;
    private javax.swing.JTextField jtfLastName;
    private javax.swing.JTextField jtfName;
    private javax.swing.JFormattedTextField jtfSearchIdentification;
    private javax.swing.JFormattedTextField jtfValue;
    private javax.swing.JLabel lbImage;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JPanel pnSearch;
    private javax.swing.JPanel pnTitle;
    private javax.swing.JPanel pnUser;
    // End of variables declaration//GEN-END:variables
}

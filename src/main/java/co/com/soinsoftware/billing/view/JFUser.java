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

import co.com.soinsoftware.billing.controller.CreditController;
import co.com.soinsoftware.billing.controller.MenuController;
import co.com.soinsoftware.billing.controller.UserController;
import co.com.soinsoftware.billing.entity.Credit;
import co.com.soinsoftware.billing.entity.Credittype;
import co.com.soinsoftware.billing.entity.User;

/**
 * @author Carlos Rodriguez
 * @since 30/05/2016
 * @version 1.0
 */
public class JFUser extends JFrame {

	private static final long serialVersionUID = -6950477778737766186L;

	private static final String TITLE = "Cooperativo - Usuario";

	private static final String MSG_EMPTY_FIELDS = "Por favor complete todos los campos";

	private static final String MSG_VALUE = "La deuda debe ser mayor a 0";

	private static final String MSG_USER_EXISTS = "Ya existe un usuario registrado con este número de documento";

	private final User loggedUser;

	private final UserController userController;

	private final CreditController creditController;
	
	private final Set<Credittype> creditTypeSet;

	/**
	 * Creates new form JFUser
	 *
	 * @param loggedUser
	 */
	public JFUser(final User loggedUser) {
		super();
		this.loggedUser = loggedUser;
		this.initComponents();
		this.userController = new UserController();
		this.creditController = new CreditController();
		this.setTextFieldLimits();
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
		this.cleanFields();
	}

	private void setMaximized() {
		final GraphicsEnvironment env = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		this.setMaximizedBounds(env.getMaximumWindowBounds());
		this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
	}

	private void setTextFieldLimits() {
		this.jtfName.setDocument(new JTextFieldLimit(45));
		this.jtfLastName.setDocument(new JTextFieldLimit(45));
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

	private void cleanFields() {
		this.jtfIdentification.setText("");
		this.jtfName.setText("");
		this.jtfLastName.setText("");
		this.jcbCreditType.setSelectedIndex(0);
		this.jtfValue.setText("0");
	}

	private boolean doValidations() {
		final int errorMsg = JOptionPane.ERROR_MESSAGE;
		boolean isValid = false;
		if (this.validateFields()) {
			if (this.validateValue()) {
				final String identificationStr = this.jtfIdentification
						.getText().replace(".", "").replace(",", "");
				final long identification = Long.parseLong(identificationStr);
				if (!this.userController.isExistingUser(identification)) {
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
		return value.doubleValue() >= 0;
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
					break;
				}
			}
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnTitle = new javax.swing.JPanel();
        lbTitle = new javax.swing.JLabel();
        pnUser = new javax.swing.JPanel();
        jlbIdentification = new javax.swing.JLabel();
        jtfIdentification = new javax.swing.JFormattedTextField();
        jlbName = new javax.swing.JLabel();
        jtfName = new javax.swing.JTextField();
        jlbLastName = new javax.swing.JLabel();
        jtfLastName = new javax.swing.JTextField();
        jlbValue = new javax.swing.JLabel();
        jtfValue = new javax.swing.JFormattedTextField();
        jbtSave = new javax.swing.JButton();
        jbtClean = new javax.swing.JButton();
        jlbCreditType = new javax.swing.JLabel();
        jcbCreditType = new javax.swing.JComboBox<String>();
        lbImage = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMinimumSize(new java.awt.Dimension(400, 430));
        setName("jfUser"); // NOI18N
        setPreferredSize(new java.awt.Dimension(400, 430));

        pnTitle.setBackground(new java.awt.Color(255, 255, 255));
        pnTitle.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnTitle.setName("pnTitle"); // NOI18N

        lbTitle.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        lbTitle.setText("Nuevo usuario");

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
        jlbValue.setText("Cantidad($):");

        jtfValue.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0"))));
        jtfValue.setText("0");
        jtfValue.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N

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

        jbtClean.setBackground(new java.awt.Color(16, 135, 221));
        jbtClean.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jbtClean.setForeground(new java.awt.Color(255, 255, 255));
        jbtClean.setText("Limpiar");
        jbtClean.setPreferredSize(new java.awt.Dimension(89, 23));
        jbtClean.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtCleanActionPerformed(evt);
            }
        });

        jlbCreditType.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jlbCreditType.setText("Tipo de crédito:");

        jcbCreditType.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        jcbCreditType.setModel(new javax.swing.DefaultComboBoxModel<String>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbCreditType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbCreditTypeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnUserLayout = new javax.swing.GroupLayout(pnUser);
        pnUser.setLayout(pnUserLayout);
        pnUserLayout.setHorizontalGroup(
            pnUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnUserLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jcbCreditType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnUserLayout.createSequentialGroup()
                        .addGroup(pnUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jtfValue)
                                .addGroup(pnUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jlbName)
                                        .addComponent(jlbIdentification)
                                        .addComponent(jlbLastName)
                                        .addComponent(jtfLastName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jtfName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jtfIdentification))
                                    .addComponent(jlbValue)))
                            .addComponent(jlbCreditType))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnUserLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jbtClean, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbtSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnUserLayout.setVerticalGroup(
            pnUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnUserLayout.createSequentialGroup()
                .addContainerGap()
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
                .addComponent(jlbCreditType)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcbCreditType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jlbValue)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtClean, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lbImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/soin.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnUser, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lbImage, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnUser, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(lbImage, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jcbCreditTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbCreditTypeActionPerformed
    	this.jtfValue.setEditable(this.jcbCreditType.getSelectedIndex() > 0);
    	this.jtfValue.setText("0");
    }//GEN-LAST:event_jcbCreditTypeActionPerformed

	private void jbtCleanActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jbtCleanActionPerformed
		this.cleanFields();
	}// GEN-LAST:event_jbtCleanActionPerformed

	private void jbtSaveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jbtSaveActionPerformed
		if (this.doValidations()) {
			final String identificationStr = this.jtfIdentification.getText()
					.replace(".", "").replace(",", "");
			final long identification = Long.parseLong(identificationStr);
			final String name = this.jtfName.getText();
			final String lastName = this.jtfLastName.getText();
			final String valueStr = this.jtfValue.getText().replace(".", "")
					.replace(",", "");
			final BigDecimal value = new BigDecimal(valueStr);
			final User user = this.userController.saveUser(
					this.loggedUser.getCompany(), identification, name,
					lastName, value);
			this.saveCreditInformation(user);
			ViewUtils.showMessage(this, ViewUtils.MSG_SAVED, TITLE,
					JOptionPane.INFORMATION_MESSAGE);
			this.cleanFields();
		}
	}// GEN-LAST:event_jbtSaveActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jbtClean;
    private javax.swing.JButton jbtSave;
    private javax.swing.JComboBox<String> jcbCreditType;
    private javax.swing.JLabel jlbCreditType;
    private javax.swing.JLabel jlbIdentification;
    private javax.swing.JLabel jlbLastName;
    private javax.swing.JLabel jlbName;
    private javax.swing.JLabel jlbValue;
    private javax.swing.JFormattedTextField jtfIdentification;
    private javax.swing.JTextField jtfLastName;
    private javax.swing.JTextField jtfName;
    private javax.swing.JFormattedTextField jtfValue;
    private javax.swing.JLabel lbImage;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JPanel pnTitle;
    private javax.swing.JPanel pnUser;
    // End of variables declaration//GEN-END:variables
}

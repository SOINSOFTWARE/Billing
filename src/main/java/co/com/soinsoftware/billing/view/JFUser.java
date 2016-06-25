/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soinsoftware.billing.view;

import co.com.soinsoftware.billing.controller.MenuController;
import co.com.soinsoftware.billing.controller.UserController;
import co.com.soinsoftware.billing.entity.User;
import java.awt.GraphicsEnvironment;
import java.math.BigDecimal;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

/**
 * @author Carlos Rodriguez
 * @since 30/05/2016
 * @version 1.0
 */
public class JFUser extends JFrame {

    private static final long serialVersionUID = -6950477778737766186L;

    private static final String TITLE = "Facturador - Usuario";

    private static final String MSG_EMPTY_FIELDS = "Por favor complete todos los campos";

    private static final String MSG_VALUE = "La deuda debe ser mayor a 0";

    private static final String MSG_USER_EXISTS = "Ya existe un usuario registrado con este número de documento";

    private final User loggedUser;

    private final UserController controller;

    /**
     * Creates new form JFUser
     *
     * @param loggedUser
     */
    public JFUser(final User loggedUser) {
        super();
        this.loggedUser = loggedUser;
        this.initComponents();
        this.controller = new UserController();
        this.setTextFieldLimits();
        this.setTitle(this.loggedUser.getCompany().getName() + " - " + TITLE);
        this.setMaximized();
    }

    public void addController(final MenuController controller) {
        final JMenuBar menuBar = new JMBAppMenu(controller);
        this.setJMenuBar(menuBar);
    }

    public void refresh() {
        this.cleanFields();
    }
    
    private void setMaximized() {
        final GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        this.setMaximizedBounds(env.getMaximumWindowBounds());
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

    private void setTextFieldLimits() {
        this.jtfName.setDocument(new JTextFieldLimit(45));
        this.jtfLastName.setDocument(new JTextFieldLimit(45));
    }

    private void cleanFields() {
        this.jtfIdentification.setText("");
        this.jtfName.setText("");
        this.jtfLastName.setText("");
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
                if (!this.controller.isExistingUser(identification)) {
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
        jlbValue.setText("Deuda:");

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

        javax.swing.GroupLayout pnUserLayout = new javax.swing.GroupLayout(pnUser);
        pnUser.setLayout(pnUserLayout);
        pnUserLayout.setHorizontalGroup(
            pnUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnUserLayout.createSequentialGroup()
                .addContainerGap()
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnUserLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbtClean, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jbtSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addComponent(jlbValue)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtClean, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
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
                .addComponent(pnUser, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbImage, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtCleanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtCleanActionPerformed
        this.cleanFields();
    }//GEN-LAST:event_jbtCleanActionPerformed

    private void jbtSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtSaveActionPerformed
        if (this.doValidations()) {
            final String identificationStr = this.jtfIdentification.getText()
                    .replace(".", "").replace(",", "");
            final long identification = Long.parseLong(identificationStr);
            final String name = this.jtfName.getText();
            final String lastName = this.jtfLastName.getText();
            final String valueStr = this.jtfValue.getText().replace(".", "")
                    .replace(",", "");
            final BigDecimal value = new BigDecimal(valueStr);
            this.controller.saveUser(this.loggedUser.getCompany(),
                    identification, name, lastName, value);
            ViewUtils.showMessage(this, ViewUtils.MSG_SAVED, TITLE,
                    JOptionPane.INFORMATION_MESSAGE);
            this.cleanFields();
        }
    }//GEN-LAST:event_jbtSaveActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jbtClean;
    private javax.swing.JButton jbtSave;
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

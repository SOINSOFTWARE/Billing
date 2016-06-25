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
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

/**
 * @author Carlos Rodriguez
 * @since 02/06/2016
 * @version 1.0
 */
public class JFViewUser extends JFrame {

    private static final long serialVersionUID = -4200612234138262654L;

    private static final String TITLE = "Facturador - Ver usuarios";

    private static final String MSG_EMPTY_ID = "El campo identificación debe ser completado para realizar la busqueda";

    private static final String MSG_NO_CLIENT = "No existen usuarios con el número de cedula especificado";

    private static final String MSG_SAVE_QUESTION = "¿Está seguro que desea guardar los cambios en el usuario?";

    private static final String MSG_EMPTY_FIELDS = "Debe completar todos los campos para poder actualizar";

    private static final String MSG_VALUE = "La deuda no puede ser disminuida en este modulo";

    private final User loggedUser;

    private final UserController userController;

    private User client;

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
        this.setTextFieldLimits();
        this.setUserFieldsVisibility(false);
        this.setTitle(this.loggedUser.getCompany().getName() + " - " + TITLE);
        this.setMaximized();
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
            visible = true;
        }
        this.setUserFieldsVisibility(visible);
    }

    private void updateTextFields(final String identification,
            final String name, final String lastName, final String value) {
        this.jtfIdentification.setText(identification);
        this.jtfName.setText(name);
        this.jtfLastName.setText(lastName);
        this.jtfValue.setText(value);
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
        this.client.setValue(value);
    }

    private boolean validateValue() {
        final String valueStr = this.jtfValue.getText().replace(".", "")
                .replace(",", "");
        final BigDecimal value = new BigDecimal(valueStr);
        return value.doubleValue() >= this.client.getValue().doubleValue();
    }

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
        jtfValue = new javax.swing.JFormattedTextField();
        jbtSave = new javax.swing.JButton();
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
                .addComponent(jbtSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addComponent(pnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnUser, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jbtSave;
    private javax.swing.JButton jbtSearch;
    private javax.swing.JLabel jlbIdentification;
    private javax.swing.JLabel jlbLastName;
    private javax.swing.JLabel jlbName;
    private javax.swing.JLabel jlbSearchIdentification;
    private javax.swing.JLabel jlbValue;
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

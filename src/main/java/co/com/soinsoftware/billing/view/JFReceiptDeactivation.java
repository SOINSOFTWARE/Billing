/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soinsoftware.billing.view;

import co.com.soinsoftware.billing.controller.MenuController;
import co.com.soinsoftware.billing.controller.ReceiptController;
import co.com.soinsoftware.billing.controller.UserController;
import co.com.soinsoftware.billing.entity.Receipt;
import co.com.soinsoftware.billing.entity.User;
import co.com.soinsoftware.billing.util.ItemTableModel;
import java.awt.GraphicsEnvironment;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.table.TableColumn;

/**
 * @author Carlos Rodriguez
 * @since 31/05/2016
 * @version 1.0
 */
public class JFReceiptDeactivation extends JFrame {

    private static final long serialVersionUID = -8432520949164190295L;

    private static final String TITLE = "Facturador - Eliminar Recibos";

    private static final String MSG_CURRENTLY_DEACTIVATED = "El recibo ya fue eliminado";

    private static final String MSG_DEACTIVATE_QUESTION = "¿Está seguro que desea eliminar este recibo?, Al aceptar el valor pagado será sumado a la deuda del usuario nuevamente.";

    private static final String MSG_EMPTY_RECEIPT = "El campo número de recibo debe ser completado para realizar la busqueda";

    private static final String MSG_NO_RECORDS = "No se encontraron registros";

    private final User loggedUser;

    private final ReceiptController receiptController;

    private final UserController userController;

    private ItemTableModel tableModel;

    private Receipt receipt;

    /**
     * Creates new form JFReceiptDeactivation
     *
     * @param loggedUser
     */
    public JFReceiptDeactivation(final User loggedUser) {
        super();
        this.loggedUser = loggedUser;
        this.initComponents();
        this.receiptController = new ReceiptController();
        this.userController = new UserController();
        this.setReceiptFieldsVisibility(false);
        this.setTitle(this.loggedUser.getCompany().getName() + " - " + TITLE);
        this.setMaximized();
    }

    public void addController(final MenuController controller) {
        final JMenuBar menuBar = new JMBAppMenu(controller);
        this.setJMenuBar(menuBar);
    }

    public void refresh() {
        this.receipt = null;
        this.cleanAction();
    }
    
    private void setMaximized() {
        final GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        this.setMaximizedBounds(env.getMaximumWindowBounds());
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

    private void cleanAction() {
        this.jtfReceiptNumber.setText("");
        this.setReceiptFieldsVisibility(false);
    }

    private void setReceiptFieldsVisibility(final boolean visible) {
        this.pnReceipt.setVisible(visible);
    }

    private void fillReceiptData() {
        if (this.receipt != null) {
            final User client = this.receipt.getUserByIduser();
            final SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");

            this.jlbReceiptNumber.setText("RECIBO DE CAJA NO "
                    + this.receipt.getNumber());
            this.jlbReceiptDate.setText("Fecha: "
                    + format.format(this.receipt.getReceiptdate()));
            this.jlbReceiptClientId.setText("Cliente "
                    + client.getIdentification());
            this.jlbReceiptClientName.setText(client.getFullName()
                    .toUpperCase());

            this.tableModel = new ItemTableModel(
                    this.receiptController.buildItemConceptData(this.receipt));
            this.jtbReceiptItems.setModel(tableModel);
            this.jtbReceiptItems.setEnabled(false);
            this.jtbReceiptItems.setFillsViewportHeight(true);
            this.setTableColumnDimensions();
            this.setReceiptFieldsVisibility(true);
        }
    }

    private void setTableColumnDimensions() {
        for (int i = 0; i < 2; i++) {
            final TableColumn column = this.jtbReceiptItems.getColumnModel()
                    .getColumn(i);
            column.setResizable(false);
            if (i == 0) {
                column.setPreferredWidth(230);
            } else {
                column.setPreferredWidth(70);
            }
        }
    }

    private void initComponents() {

        pnTitle = new javax.swing.JPanel();
        lbTitle = new javax.swing.JLabel();
        pnReceiptSearch = new javax.swing.JPanel();
        jlbViewReceiptNumber = new javax.swing.JLabel();
        jtfReceiptNumber = new javax.swing.JFormattedTextField();
        jbtViewReceipt = new javax.swing.JButton();
        pnReceipt = new javax.swing.JPanel();
        jlbReceiptNumber = new javax.swing.JLabel();
        jlbReceiptDate = new javax.swing.JLabel();
        jlbReceiptClientId = new javax.swing.JLabel();
        jlbReceiptClientName = new javax.swing.JLabel();
        jbtDeactivateReceipt = new javax.swing.JButton();
        jspReceiptItemsTable = new javax.swing.JScrollPane();
        jtbReceiptItems = new javax.swing.JTable();
        jlbReceiptTitle = new javax.swing.JLabel();
        jlbReceiptNit = new javax.swing.JLabel();
        lbImage = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(620, 580));

        pnTitle.setBackground(new java.awt.Color(255, 255, 255));
        pnTitle.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnTitle.setName("pnTitle"); // NOI18N

        lbTitle.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        lbTitle.setText("Eliminar recibo");

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

        pnReceiptSearch.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Buscar", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 1, 12))); // NOI18N
        pnReceiptSearch.setName("pnReceiptSearch"); // NOI18N
        pnReceiptSearch.setPreferredSize(new java.awt.Dimension(300, 200));

        jlbViewReceiptNumber.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jlbViewReceiptNumber.setText("Recibo de caja NO:");

        jtfReceiptNumber.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("####0"))));
        jtfReceiptNumber.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N

        jbtViewReceipt.setBackground(new java.awt.Color(16, 135, 221));
        jbtViewReceipt.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jbtViewReceipt.setForeground(new java.awt.Color(255, 255, 255));
        jbtViewReceipt.setText("Ver recibo");
        jbtViewReceipt.setPreferredSize(new java.awt.Dimension(89, 23));
        jbtViewReceipt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtViewReceiptActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnReceiptSearchLayout = new javax.swing.GroupLayout(pnReceiptSearch);
        pnReceiptSearch.setLayout(pnReceiptSearchLayout);
        pnReceiptSearchLayout.setHorizontalGroup(
            pnReceiptSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnReceiptSearchLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnReceiptSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlbViewReceiptNumber)
                    .addGroup(pnReceiptSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jbtViewReceipt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jtfReceiptNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnReceiptSearchLayout.setVerticalGroup(
            pnReceiptSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnReceiptSearchLayout.createSequentialGroup()
                .addComponent(jlbViewReceiptNumber)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfReceiptNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jbtViewReceipt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnReceipt.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Recibo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 1, 12))); // NOI18N
        pnReceipt.setName("pnReceipt"); // NOI18N
        pnReceipt.setPreferredSize(new java.awt.Dimension(300, 200));

        jlbReceiptNumber.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jlbReceiptNumber.setText("RECIBO DE CAJA NO");

        jlbReceiptDate.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jlbReceiptDate.setText("Fecha:");

        jlbReceiptClientId.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jlbReceiptClientId.setText("Cliente");

        jlbReceiptClientName.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jlbReceiptClientName.setText("Nombre");

        jbtDeactivateReceipt.setBackground(new java.awt.Color(16, 135, 221));
        jbtDeactivateReceipt.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jbtDeactivateReceipt.setForeground(new java.awt.Color(255, 255, 255));
        jbtDeactivateReceipt.setText("Eliminar");
        jbtDeactivateReceipt.setPreferredSize(new java.awt.Dimension(89, 23));
        jbtDeactivateReceipt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtDeactivateReceiptActionPerformed(evt);
            }
        });

        jtbReceiptItems.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        jtbReceiptItems.setModel(new javax.swing.table.DefaultTableModel(
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
        jspReceiptItemsTable.setViewportView(jtbReceiptItems);

        jlbReceiptTitle.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jlbReceiptTitle.setText(this.loggedUser.getCompany().getName());

        jlbReceiptNit.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jlbReceiptNit.setText("NIT: " + this.loggedUser.getCompany().getNit());

        javax.swing.GroupLayout pnReceiptLayout = new javax.swing.GroupLayout(pnReceipt);
        pnReceipt.setLayout(pnReceiptLayout);
        pnReceiptLayout.setHorizontalGroup(
            pnReceiptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnReceiptLayout.createSequentialGroup()
                .addGroup(pnReceiptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnReceiptLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jspReceiptItemsTable, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnReceiptLayout.createSequentialGroup()
                        .addGap(99, 99, 99)
                        .addGroup(pnReceiptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlbReceiptNit)
                            .addComponent(jlbReceiptTitle)
                            .addComponent(jlbReceiptNumber)
                            .addComponent(jlbReceiptDate)
                            .addComponent(jlbReceiptClientId)
                            .addComponent(jlbReceiptClientName))
                        .addGap(0, 109, Short.MAX_VALUE))
                    .addGroup(pnReceiptLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jbtDeactivateReceipt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnReceiptLayout.setVerticalGroup(
            pnReceiptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnReceiptLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jlbReceiptTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlbReceiptNit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jlbReceiptNumber)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jlbReceiptDate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlbReceiptClientId)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlbReceiptClientName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jspReceiptItemsTable, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jbtDeactivateReceipt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        lbImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/soin.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnReceiptSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnReceipt, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(pnReceiptSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnReceipt, javax.swing.GroupLayout.PREFERRED_SIZE, 422, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbImage, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtViewReceiptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtViewReceiptActionPerformed
        final int infoMsg = JOptionPane.INFORMATION_MESSAGE;
        this.receipt = null;
        final String receiptNumber = this.jtfReceiptNumber.getText().replace(
                ".", "");
        if (!receiptNumber.equals("")) {
            this.receipt = this.receiptController.select(Long
                    .parseLong(receiptNumber));
            if (this.receipt != null) {
                if (this.receipt.isEnabled()) {
                    this.fillReceiptData();
                } else {
                    ViewUtils.showMessage(this, MSG_CURRENTLY_DEACTIVATED,
                            TITLE, infoMsg);
                    this.refresh();
                }
            } else {
                ViewUtils.showMessage(this, MSG_NO_RECORDS, TITLE, infoMsg);
                this.refresh();
            }
        } else {
            ViewUtils.showMessage(this, MSG_EMPTY_RECEIPT, TITLE, infoMsg);
            this.refresh();
        }
    }//GEN-LAST:event_jbtViewReceiptActionPerformed

    private void jbtDeactivateReceiptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtDeactivateReceiptActionPerformed
        if (this.jbtDeactivateReceipt.isVisible() && this.receipt != null) {
            final int confirm = ViewUtils.showConfirmDialog(this,
                    MSG_DEACTIVATE_QUESTION, TITLE);
            if (confirm == JOptionPane.OK_OPTION) {
                final Date currentDate = new Date();
                this.receipt.setEnabled(false);
                this.receipt.setUserByIdlastchangeuser(this.loggedUser);
                this.receipt.setUpdated(currentDate);
                final User client = this.receipt.getUserByIduser();
                client.setValue(client.getValue().add(this.receipt.getValue()));
                client.setUpdated(currentDate);
                this.receiptController.saveReceipt(this.receipt);
                this.userController.saveUser(client);
                ViewUtils.showMessage(this, ViewUtils.MSG_SAVED, TITLE,
                        JOptionPane.INFORMATION_MESSAGE);
                this.refresh();
            }
        }
    }//GEN-LAST:event_jbtDeactivateReceiptActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jbtDeactivateReceipt;
    private javax.swing.JButton jbtViewReceipt;
    private javax.swing.JLabel jlbReceiptClientId;
    private javax.swing.JLabel jlbReceiptClientName;
    private javax.swing.JLabel jlbReceiptDate;
    private javax.swing.JLabel jlbReceiptNit;
    private javax.swing.JLabel jlbReceiptNumber;
    private javax.swing.JLabel jlbReceiptTitle;
    private javax.swing.JLabel jlbViewReceiptNumber;
    private javax.swing.JScrollPane jspReceiptItemsTable;
    private javax.swing.JTable jtbReceiptItems;
    private javax.swing.JFormattedTextField jtfReceiptNumber;
    private javax.swing.JLabel lbImage;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JPanel pnReceipt;
    private javax.swing.JPanel pnReceiptSearch;
    private javax.swing.JPanel pnTitle;
    // End of variables declaration//GEN-END:variables
}

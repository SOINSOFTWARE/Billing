/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soinsoftware.billing.view;

import co.com.soinsoftware.billing.controller.ItemConceptController;
import co.com.soinsoftware.billing.controller.MenuController;
import co.com.soinsoftware.billing.entity.Itemconcept;
import co.com.soinsoftware.billing.entity.User;

import java.awt.GraphicsEnvironment;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

/**
 * @author Carlos Rodriguez
 * @since 15/06/2016
 * @version 1.0
 */
public class JFItemConceptDeactivation extends JFrame {

    private static final long serialVersionUID = 7962825337634660703L;

    private static final String TITLE = "Facturador - Eliminar concepto de facturación";

    private static final String MSG_NO_SELECTION = "Debe seleccionar un elemento para realizar la eliminación";

    private static final String MSG_SAVE_QUESTION = "¿Está seguro que desea eliminar el concepto?, Una vez realizada la eliminación el concepto no aparecerá en nuevas factura.";

    private final User loggedUser;

    private final ItemConceptController itemConceptController;

    /**
     * Creates new form JFItemConceptDeactivation
     *
     * @param loggedUser
     */
    @SuppressWarnings("unchecked")
	public JFItemConceptDeactivation(final User loggedUser) {
        super();
        this.loggedUser = loggedUser;
        this.initComponents();
        this.itemConceptController = new ItemConceptController();
        this.jlsItemConcept.setListData(this.getActiveItemConcepts());
        this.setTitle(this.loggedUser.getCompany().getName() + " - " + TITLE);
        this.setMaximized();
    }

    public void addController(final MenuController controller) {
        final JMenuBar menuBar = new JMBAppMenu(controller);
        this.setJMenuBar(menuBar);
    }

    @SuppressWarnings("unchecked")
	public void refresh() {
        this.jlsItemConcept.setListData(this.getActiveItemConcepts());
        this.jlsItemConcept.clearSelection();
    }
    
    private void setMaximized() {
        final GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        this.setMaximizedBounds(env.getMaximumWindowBounds());
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
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

    @SuppressWarnings("rawtypes")
	private void initComponents() {

        pnTitle = new javax.swing.JPanel();
        lbTitle = new javax.swing.JLabel();
        pnItemConceptList = new javax.swing.JPanel();
        jspItemConcept = new javax.swing.JScrollPane();
        jlsItemConcept = new javax.swing.JList();
        jbtDeactivate = new javax.swing.JButton();
        jbtClean = new javax.swing.JButton();
        lbImage = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(400, 400));
        setPreferredSize(new java.awt.Dimension(400, 400));

        pnTitle.setBackground(new java.awt.Color(255, 255, 255));
        pnTitle.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnTitle.setToolTipText("");
        pnTitle.setName("pnTitle"); // NOI18N

        lbTitle.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        lbTitle.setText("Eliminar concepto");

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

        pnItemConceptList.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Listado de conceptos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 1, 12))); // NOI18N
        pnItemConceptList.setName("pnItemConcept"); // NOI18N
        pnItemConceptList.setPreferredSize(new java.awt.Dimension(300, 200));

        jlsItemConcept.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        jlsItemConcept.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jspItemConcept.setViewportView(jlsItemConcept);

        jbtDeactivate.setBackground(new java.awt.Color(16, 135, 221));
        jbtDeactivate.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jbtDeactivate.setForeground(new java.awt.Color(255, 255, 255));
        jbtDeactivate.setText("Eliminar");
        jbtDeactivate.setPreferredSize(new java.awt.Dimension(89, 23));
        jbtDeactivate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtDeactivateActionPerformed(evt);
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

        javax.swing.GroupLayout pnItemConceptListLayout = new javax.swing.GroupLayout(pnItemConceptList);
        pnItemConceptList.setLayout(pnItemConceptListLayout);
        pnItemConceptListLayout.setHorizontalGroup(
            pnItemConceptListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jspItemConcept, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnItemConceptListLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbtClean, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jbtDeactivate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnItemConceptListLayout.setVerticalGroup(
            pnItemConceptListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnItemConceptListLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jspItemConcept, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnItemConceptListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtDeactivate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtClean, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addComponent(pnItemConceptList, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addComponent(pnItemConceptList, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbImage, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    @SuppressWarnings("unchecked")
	private void jbtDeactivateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtDeactivateActionPerformed
        if (!this.jlsItemConcept.isSelectionEmpty()) {
            final int confirm = ViewUtils.showConfirmDialog(this,
                    MSG_SAVE_QUESTION, TITLE);
            if (confirm == JOptionPane.OK_OPTION) {
                final String name = (String)this.jlsItemConcept.getSelectedValue();
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
    }//GEN-LAST:event_jbtDeactivateActionPerformed

    private void jbtCleanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtCleanActionPerformed
        this.refresh();
    }//GEN-LAST:event_jbtCleanActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jbtClean;
    private javax.swing.JButton jbtDeactivate;
    @SuppressWarnings("rawtypes")
	private javax.swing.JList jlsItemConcept;
    private javax.swing.JScrollPane jspItemConcept;
    private javax.swing.JLabel lbImage;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JPanel pnItemConceptList;
    private javax.swing.JPanel pnTitle;
    // End of variables declaration//GEN-END:variables
}

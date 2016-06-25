/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soinsoftware.billing.view;

import co.com.soinsoftware.billing.controller.MenuController;
import co.com.soinsoftware.billing.controller.ReceiptController;
import co.com.soinsoftware.billing.controller.UserController;
import co.com.soinsoftware.billing.entity.Item;
import co.com.soinsoftware.billing.entity.Receipt;
import co.com.soinsoftware.billing.entity.User;
import co.com.soinsoftware.billing.report.ThreadGenerator;
import co.com.soinsoftware.billing.util.ItemTableModel;
import java.awt.GraphicsEnvironment;
import java.math.BigDecimal;
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
public class JFReceipt extends JFrame {

    private static final long serialVersionUID = 9030665802815908970L;

    private static final String TITLE = "Facturador - Recibo";

    private static final String MSG_EMPTY_ID = "El campo identificación debe ser completado para realizar la busqueda";

    private static final String MSG_NO_CLIENT = "No existen usuarios con el número de cedula especificado";

    private static final String MSG_NO_CLIENT_SELECTED = "El pago no puede ser efectuado, debe seleccionar un usuario realizando la busqueda usando el número de identificación";

    private static final String MSG_NO_CONSECUTIVE = "Los consecutivos para las facturas están agotados.";

    private static final String MSG_NO_VALUE_SELECTED = "El pago no puede ser efectuado, debe indicar el valor pagado por el usuario";

    private static final String MSG_START_PRINT = "¿Está seguro que desea imprimir el recibo?, una vez iniciado el proceso el recibo no puede ser modificado.";

    private static final String MSG_PRINT_STARTED = "El proceso de impresión fue iniciado, en unos segundos su recibo podrá ser impreso.";

    private static final String MSG_VALUE = "El valor ha pagar no puede ser mayor a la deuda del cliente";

    private static final String MSG_ITEM_VALUE = "La suma del valor de los aportes no es igual al valor pagado";

    private final User loggedUser;

    private final UserController userController;

    private final ReceiptController receiptController;

    private User client;

    private Receipt receipt;

    private ItemTableModel tableModel;

    /**
     * Creates new form JFReceipt
     *
     * @param loggedUser
     */
    public JFReceipt(final User loggedUser) {
        super();
        this.loggedUser = loggedUser;
        this.initComponents();
        this.userController = new UserController();
        this.receiptController = new ReceiptController();
        this.setTitle(this.loggedUser.getCompany().getName() + " - " + TITLE);
        this.setMaximized();
    }

    public void addController(final MenuController controller) {
        final JMenuBar menuBar = new JMBAppMenu(controller);
        this.setJMenuBar(menuBar);
    }

    public void refresh() {
        this.client = null;
        this.receipt = null;
        this.jtfIdentification.setText("");
        this.updateTextFields();
    }
    
    private void setMaximized() {
        final GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        this.setMaximizedBounds(env.getMaximumWindowBounds());
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

    private void updateTextFields() {
        if (this.client == null) {
            this.updateTextFields("", "", "", "");
            this.jtfValue.setEnabled(false);
        } else {
            final long value = Math.round(this.client.getValue().doubleValue());
            this.updateTextFields(this.client.getName(),
                    this.client.getLastname(), String.valueOf(value), "");
        }
        this.setReceiptFieldsVisibility(false);
    }

    private void updateTextFields(final String name, final String lastName,
            final String userValue, final String value) {
        this.jtfName.setText(name);
        this.jtfLastName.setText(lastName);
        this.jtfUserValue.setText(userValue);
        this.jtfValue.setText(value);
    }

    private void fillReceiptData() {
        if (this.receipt != null) {
            final Date currentDate = new Date();
            final SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
            final StringBuilder nameBuilder = new StringBuilder();
            nameBuilder.append(this.client.getName().toUpperCase());
            nameBuilder.append(" ");
            nameBuilder.append(this.client.getLastname().toUpperCase());
            this.jlbReceiptNumber.setText("RECIBO DE CAJA NO "
                    + this.receipt.getNumber());
            this.jlbReceiptDate.setText("Fecha: " + format.format(currentDate));
            this.jlbReceiptClientId.setText("Cliente "
                    + this.client.getIdentification());
            this.jlbReceiptClientName.setText(nameBuilder.toString());

            this.tableModel = new ItemTableModel(
                    this.receiptController.buildItemConceptData(this.receipt));
            this.jtbReceiptItems.setModel(this.tableModel);
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

    private void setReceiptFieldsVisibility(final boolean visible) {
        this.pnReceipt.setVisible(visible);
    }

    private boolean doValidations() {
        boolean isValid = false;
        final int errorMsg = JOptionPane.ERROR_MESSAGE;
        if (this.client != null) {
            final String valueStr = this.jtfValue.getText().replace(".", "")
                    .replace(",", "");
            if (!valueStr.equals("")) {
                if (this.validateValue()) {
                    isValid = true;
                } else {
                    ViewUtils.showMessage(this, MSG_VALUE, TITLE, errorMsg);
                }
            } else {
                ViewUtils.showMessage(this, MSG_NO_VALUE_SELECTED, TITLE, errorMsg);
            }
        } else {
            ViewUtils.showMessage(this, MSG_NO_CLIENT_SELECTED, TITLE, errorMsg);
        }
        return isValid;
    }

    private boolean validateValue() {
        final String valueStr = this.jtfValue.getText().replace(".", "")
                .replace(",", "");
        final BigDecimal value = new BigDecimal(valueStr);
        return value.doubleValue() <= this.client.getValue().doubleValue();
    }

    private void updateUserValue() {
        final String valueStr = this.jtfValue.getText().replace(".", "")
                .replace(",", "");
        BigDecimal value = new BigDecimal(valueStr);
        value = this.client.getValue().subtract(value);
        this.client.setValue(value);
        this.client.setUpdated(new Date());
        this.userController.saveUser(this.client);
    }

    private boolean validateItemValue() {
        final String valueStr = this.jtfValue.getText().replace(".", "")
                .replace(",", "");
        final BigDecimal value = new BigDecimal(valueStr);
        BigDecimal itemValue = new BigDecimal(0);
        final Object[][] data = this.tableModel.getData();
        for (Object[] data1 : data) {
            final BigDecimal dataValue = (BigDecimal) data1[1];
            itemValue = itemValue.add(dataValue);
        }
        return value.doubleValue() == itemValue.doubleValue();
    }

    private void updateItemValue() {
        final Object[][] data = this.tableModel.getData();
        for (Object[] data1 : data) {
            final String concept = (String) data1[0];
            final BigDecimal dataValue = (BigDecimal) data1[1];
            for (final Item item : this.receipt.getItemSet()) {
                if (item.getConceptName().equals(concept)) {
                    item.setValue(dataValue);
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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnTitle = new javax.swing.JPanel();
        lbTitle = new javax.swing.JLabel();
        pnCreateReceipt = new javax.swing.JPanel();
        jlbIdentification = new javax.swing.JLabel();
        jtfIdentification = new javax.swing.JFormattedTextField();
        jbtSearchUser = new javax.swing.JButton();
        jtfName = new javax.swing.JTextField();
        jlbName = new javax.swing.JLabel();
        jlbLastName = new javax.swing.JLabel();
        jtfLastName = new javax.swing.JTextField();
        jlbUserValue = new javax.swing.JLabel();
        jtfUserValue = new javax.swing.JFormattedTextField();
        jlbValue = new javax.swing.JLabel();
        jtfValue = new javax.swing.JFormattedTextField();
        jbtPay = new javax.swing.JButton();
        jbtClean = new javax.swing.JButton();
        pnReceipt = new javax.swing.JPanel();
        jlbReceiptNumber = new javax.swing.JLabel();
        jlbReceiptDate = new javax.swing.JLabel();
        jlbReceiptClientId = new javax.swing.JLabel();
        jlbReceiptClientName = new javax.swing.JLabel();
        jbtPrint = new javax.swing.JButton();
        jspReceiptItemsTable = new javax.swing.JScrollPane();
        jtbReceiptItems = new javax.swing.JTable();
        jlbReceiptTitle = new javax.swing.JLabel();
        jlbReceiptNit = new javax.swing.JLabel();
        lbImage = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(700, 580));

        pnTitle.setBackground(new java.awt.Color(255, 255, 255));
        pnTitle.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnTitle.setName("pnTitle"); // NOI18N

        lbTitle.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        lbTitle.setText("Nuevo recibo");

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

        pnCreateReceipt.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos del recibo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 1, 12))); // NOI18N
        pnCreateReceipt.setName("pnCreateReceipt"); // NOI18N
        pnCreateReceipt.setPreferredSize(new java.awt.Dimension(300, 200));

        jlbIdentification.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jlbIdentification.setText("Identificación:");

        jtfIdentification.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0"))));
        jtfIdentification.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N

        jbtSearchUser.setBackground(new java.awt.Color(16, 135, 221));
        jbtSearchUser.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jbtSearchUser.setForeground(new java.awt.Color(255, 255, 255));
        jbtSearchUser.setText("Buscar");
        jbtSearchUser.setPreferredSize(new java.awt.Dimension(89, 23));
        jbtSearchUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtSearchUserActionPerformed(evt);
            }
        });

        jtfName.setEditable(false);
        jtfName.setBackground(new java.awt.Color(255, 255, 255));
        jtfName.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        jtfName.setFocusable(false);
        jtfName.setPreferredSize(new java.awt.Dimension(200, 20));

        jlbName.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jlbName.setText("Nombre(s):");

        jlbLastName.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jlbLastName.setText("Apellido(s):");

        jtfLastName.setEditable(false);
        jtfLastName.setBackground(new java.awt.Color(255, 255, 255));
        jtfLastName.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        jtfLastName.setFocusable(false);
        jtfLastName.setPreferredSize(new java.awt.Dimension(200, 20));

        jlbUserValue.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jlbUserValue.setText("Deuda($):");

        jtfUserValue.setEditable(false);
        jtfUserValue.setBackground(new java.awt.Color(255, 255, 255));
        jtfUserValue.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0"))));
        jtfUserValue.setFocusable(false);
        jtfUserValue.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N

        jlbValue.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jlbValue.setText("Pago($):");

        jtfValue.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0"))));
        jtfValue.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        jtfValue.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N

        jbtPay.setBackground(new java.awt.Color(16, 135, 221));
        jbtPay.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jbtPay.setForeground(new java.awt.Color(255, 255, 255));
        jbtPay.setText("Pagar");
        jbtPay.setPreferredSize(new java.awt.Dimension(89, 23));
        jbtPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtPayActionPerformed(evt);
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

        javax.swing.GroupLayout pnCreateReceiptLayout = new javax.swing.GroupLayout(pnCreateReceipt);
        pnCreateReceipt.setLayout(pnCreateReceiptLayout);
        pnCreateReceiptLayout.setHorizontalGroup(
            pnCreateReceiptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnCreateReceiptLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnCreateReceiptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlbName)
                    .addComponent(jlbIdentification)
                    .addGroup(pnCreateReceiptLayout.createSequentialGroup()
                        .addComponent(jtfIdentification, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtSearchUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jlbLastName)
                    .addComponent(jtfLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlbUserValue)
                    .addComponent(jtfUserValue, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlbValue)
                    .addComponent(jtfValue, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnCreateReceiptLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbtClean, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jbtPay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnCreateReceiptLayout.setVerticalGroup(
            pnCreateReceiptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnCreateReceiptLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlbIdentification)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnCreateReceiptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfIdentification, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtSearchUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jlbName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(jlbLastName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jlbUserValue)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfUserValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jlbValue)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnCreateReceiptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtPay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtClean, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        jbtPrint.setBackground(new java.awt.Color(16, 135, 221));
        jbtPrint.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jbtPrint.setForeground(new java.awt.Color(255, 255, 255));
        jbtPrint.setText("Imprimir");
        jbtPrint.setPreferredSize(new java.awt.Dimension(89, 23));
        jbtPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtPrintActionPerformed(evt);
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
                        .addComponent(jbtPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addComponent(jbtPrint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        lbImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/soin.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnCreateReceipt, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnReceipt, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 11, Short.MAX_VALUE))
            .addComponent(pnTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                    .addComponent(pnCreateReceipt, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnReceipt, javax.swing.GroupLayout.PREFERRED_SIZE, 422, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbImage, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtSearchUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtSearchUserActionPerformed
        final int infoMsg = JOptionPane.INFORMATION_MESSAGE;
        final String identificationStr = this.jtfIdentification.getText()
                .replace(".", "").replace(",", "");
        if (!identificationStr.equals("")) {
            final long identification = Long.parseLong(identificationStr);
            client = this.userController.selectUser(identification);
            if (client != null) {
                this.jtfValue.setEnabled(true);
            } else {
                this.client = null;
                ViewUtils.showMessage(this, MSG_NO_CLIENT, TITLE, infoMsg);
            }
        } else {
            this.client = null;
            ViewUtils.showMessage(this, MSG_EMPTY_ID, TITLE, infoMsg);
        }
        this.updateTextFields();
    }//GEN-LAST:event_jbtSearchUserActionPerformed

    private void jbtPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtPayActionPerformed
        if (this.doValidations()) {
            final String valueStr = this.jtfValue.getText().replace(".", "")
                    .replace(",", "");
            this.receipt = this.receiptController.createReceipt(
                    this.loggedUser, this.client, Integer.parseInt(valueStr));
            if (this.receipt != null) {
                this.fillReceiptData();
            } else {
                ViewUtils.showMessage(this, MSG_NO_CONSECUTIVE, TITLE,
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jbtPayActionPerformed

    private void jbtCleanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtCleanActionPerformed
        this.client = null;
        this.jtfIdentification.setText("");
        this.updateTextFields();
    }//GEN-LAST:event_jbtCleanActionPerformed

    private void jbtPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtPrintActionPerformed
        if (this.jbtPrint.isVisible()) {
            if (this.validateItemValue()) {
                final int confirm = ViewUtils.showConfirmDialog(this,
                        MSG_START_PRINT, TITLE);
                if (confirm == JOptionPane.OK_OPTION) {
                    this.updateUserValue();
                    this.updateItemValue();
                    this.receiptController.saveReceipt(this.receipt);
                    final ThreadGenerator generator = new ThreadGenerator(
                            this.receipt);
                    generator.start();
                    ViewUtils.showMessage(this, MSG_PRINT_STARTED, TITLE,
                            JOptionPane.INFORMATION_MESSAGE);
                    this.refresh();
                }
            } else {
                ViewUtils.showMessage(this, MSG_ITEM_VALUE, TITLE,
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jbtPrintActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jbtClean;
    private javax.swing.JButton jbtPay;
    private javax.swing.JButton jbtPrint;
    private javax.swing.JButton jbtSearchUser;
    private javax.swing.JLabel jlbIdentification;
    private javax.swing.JLabel jlbLastName;
    private javax.swing.JLabel jlbName;
    private javax.swing.JLabel jlbReceiptClientId;
    private javax.swing.JLabel jlbReceiptClientName;
    private javax.swing.JLabel jlbReceiptDate;
    private javax.swing.JLabel jlbReceiptNit;
    private javax.swing.JLabel jlbReceiptNumber;
    private javax.swing.JLabel jlbReceiptTitle;
    private javax.swing.JLabel jlbUserValue;
    private javax.swing.JLabel jlbValue;
    private javax.swing.JScrollPane jspReceiptItemsTable;
    private javax.swing.JTable jtbReceiptItems;
    private javax.swing.JFormattedTextField jtfIdentification;
    private javax.swing.JTextField jtfLastName;
    private javax.swing.JTextField jtfName;
    private javax.swing.JFormattedTextField jtfUserValue;
    private javax.swing.JFormattedTextField jtfValue;
    private javax.swing.JLabel lbImage;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JPanel pnCreateReceipt;
    private javax.swing.JPanel pnReceipt;
    private javax.swing.JPanel pnTitle;
    // End of variables declaration//GEN-END:variables
}

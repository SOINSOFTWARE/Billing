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
import co.com.soinsoftware.billing.report.ThreadGenerator;
import co.com.soinsoftware.billing.util.ItemTableModel;

import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 * @author Carlos Rodriguez
 * @since 30/05/2016
 * @version 1.0
 */
public class JFReportReceipt extends JFrame {

    private static final long serialVersionUID = -5816892555495529552L;

    private static final String TITLE = "Cooperativo - Ver Recibos";

    private static final String MSG_EMPTY_ID = "El campo identificación debe ser completado para realizar la busqueda";

    private static final String MSG_EMPTY_RECEIPT = "El campo número de recibo debe ser completado para realizar la busqueda";

    private static final String MSG_NO_CLIENT = "No existen usuarios con el número de cedula especificado";

    private static final String MSG_NO_RECORDS = "No se encontraron registros";

    private static final String MSG_NO_VALID_YEAR = "El año a ser consultado no posee datos.";

    private static final String MSG_NO_YEAR = "Digite un año valido. Ejemplo: "
            + Calendar.getInstance().get(Calendar.YEAR);

    private static final String MSG_PRINT_STARTED = "El proceso de impresión fue iniciado, en unos segundos su recibo podrá ser impreso.";

    private static final String[] RECEIPT_COLUMN_NAMES = {"Identificación",
        "Nombre", "Fecha", "Recibo", "Valor"};

    private final User loggedUser;

    private final UserController userController;

    private final ReceiptController receiptController;

    private User client;

    private Receipt receipt;

    private List<Receipt> receiptList;

    /**
     * Creates new form JFReportReceipt
     *
     * @param loggedUser
     */
    public JFReportReceipt(final User loggedUser) {
        super();
        this.loggedUser = loggedUser;
        this.initComponents();
        this.userController = new UserController();
        this.receiptController = new ReceiptController();
        this.initMonthList();
        this.initReceiptEnabledButtonGroup();
        this.setViewReceiptFieldsVisibility(false);
        this.setReceiptFieldsVisibility(false);
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
        this.cleanAction();
    }
    
    private void setMaximized() {
        final GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        this.setMaximizedBounds(env.getMaximumWindowBounds());
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

    private void initMonthList() {
        final Calendar calendar = Calendar.getInstance();
        this.jlsMonth.setListData(this.getMonthData());
        this.jlsMonth.setSelectedIndex(calendar.get(Calendar.MONTH));
    }

    private void initReceiptEnabledButtonGroup() {
        this.groupEnabled.add(this.rbtEnabled);
        this.groupEnabled.add(this.rbtDeactivated);
        this.groupEnabled.clearSelection();
        this.rbtEnabled.setSelected(true);
    }

    private void setViewReceiptFieldsVisibility(final boolean visible) {
        this.pnReceiptList.setVisible(visible);
        this.jbtPrintList.setVisible(visible);
        this.jspReceiptsTable.setVisible(visible);
        this.jlbViewReceiptNumber.setVisible(visible);
        this.jtfReceiptNumber.setVisible(visible);
        this.jbtViewReceipt.setVisible(visible);
        this.pnReceipt.setVisible(visible);
        if (!visible) {
            this.receiptList = null;
        }
    }

    private void setReceiptFieldsVisibility(final boolean visible) {
        this.jlbReceiptNumber.setVisible(visible);
        this.jlbReceiptDate.setVisible(visible);
        this.jlbReceiptClientId.setVisible(visible);
        this.jlbReceiptClientName.setVisible(visible);
        this.jbtPrint.setVisible(visible);
        this.jspReceiptItemsTable.setVisible(visible);
    }

    private String[] getMonthData() {
        return new String[]{"Enero", "Febrero", "Marzo", "Abril", "Mayo",
            "Junio", "Julio", "Agosto", "Septiembre", "Octubre",
            "Noviembre", "Diciembre"};
    }

    private void updateTextFields() {
        if (this.client == null) {
            this.updateTextFields("", "");
        } else {
            this.updateTextFields(this.client.getName(),
                    this.client.getLastname());
        }
    }

    private void updateTextFields(final String name, final String lastName) {
        this.jtfName.setText(name);
        this.jtfLastName.setText(lastName);
    }

    private void cleanAction() {
        final Calendar calendar = Calendar.getInstance();
        this.client = null;
        this.jtfYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        this.jlsMonth.setSelectedIndex(calendar.get(Calendar.MONTH));
        this.jtfIdentification.setText("");
        this.rbtEnabled.setSelected(true);
        this.cleanReceiptData();
    }

    private void cleanReceiptData() {
        this.updateTextFields();
        this.setReceiptFieldsVisibility(false);
        this.jtfReceiptNumber.setText("");
        this.setViewReceiptFieldsVisibility(false);
        this.removeReceiptTable();
    }

    private void removeReceiptTable() {
        this.jlbTotalMonth.setVisible(false);
        this.jlbTotal.setVisible(false);
        this.jlbDebt.setVisible(false);
        this.jlbVoluntarySave.setVisible(false);
        this.jspReceiptsTable.setVisible(false);
    }

    private boolean validateYear() {
        boolean isValid = false;
        final int infoMsg = JOptionPane.INFORMATION_MESSAGE;
        final String yearStr = this.jtfYear.getText().replace(".", "")
                .replace(",", "");
        if (!yearStr.equals("")) {
            final long year = Long.parseLong(yearStr);
            final int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            if (year >= 2000 && year <= currentYear) {
                isValid = true;
            } else {
                ViewUtils.showMessage(this, MSG_NO_VALID_YEAR, TITLE, infoMsg);
            }
        } else {
            ViewUtils.showMessage(this, MSG_NO_YEAR, TITLE, infoMsg);
        }
        return isValid;
    }

    private void fillReceiptTable() {
        final Object[][] data = this.receiptList != null ?
        		this.receiptController.buildReceiptData(this.receiptList) : null;
        if (data != null) {
        	final DefaultTableModel tableModel = new DefaultTableModel(
        			data, RECEIPT_COLUMN_NAMES);
        	this.jtbReceipts.setModel(tableModel);
        }
        this.jtbReceipts.setFillsViewportHeight(true);
        this.jtbReceipts.setEnabled(false);
        for (int i = 0; i < 2; i++) {
            final TableColumn column = this.jtbReceipts.getColumnModel()
                    .getColumn(i);
            column.setResizable(false);
            if (i == 1) {
                column.setPreferredWidth(200);
            } else {
                column.setPreferredWidth(100);
            }
        }
        this.jtbReceipts.repaint();
        this.fillTotalPerMonth(data);
        this.fillTotal();
        this.fillTotalDebt();
        this.fillTotalVoluntarySave();
        this.pnReceiptList.revalidate();
        this.pnReceiptList.repaint();
    }
    
    private void fillTotalPerMonth(final Object[][] data) {
    	final Locale locale = new Locale("es", "CO");
        final NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
        final BigDecimal totalMonth = data != null ? this.getTotalMonth(data) : new BigDecimal(0);
        final String totalMonthStr = "Total recaudado del mes: "
                + formatter.format(totalMonth.doubleValue());
        this.jlbTotalMonth.setText(totalMonthStr);
        this.jlbTotalMonth.setVisible(true);
    }
    
    private void fillTotal() {
    	final Locale locale = new Locale("es", "CO");
        final NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
    	final BigDecimal total = this.receiptController
                .selectTotal(this.client);
        final String totalStr = "Total recaudado: "
                + formatter.format(total.doubleValue());
        this.jlbTotal.setText(totalStr);
        this.jlbTotal.setVisible(true);
    }
    
    private void fillTotalDebt() {
    	final Locale locale = new Locale("es", "CO");
        final NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
    	 final BigDecimal debt = this.userController.selectDebt(this.client);
         final String debtStr = "Total de la deuda: "
                 + formatter.format(debt.doubleValue());
         this.jlbDebt.setText(debtStr);
         this.jlbDebt.setVisible(true);
    }
    
    private void fillTotalVoluntarySave() {
    	final Locale locale = new Locale("es", "CO");
        final NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
    	final BigDecimal voluntarySave = this.receiptController
                .selectVoluntarySave(this.client);
        final String voluntarySaveStr = "Total ahorro voluntario: "
                + formatter.format(voluntarySave.doubleValue());
        this.jlbVoluntarySave.setText(voluntarySaveStr);
        this.jlbVoluntarySave.setVisible(true);
    }

    private BigDecimal getTotalMonth(final Object[][] data) {
        BigDecimal total = new BigDecimal(0);
        for (Object[] data1 : data) {
            final BigDecimal value = (BigDecimal) data1[4];
            total = total.add(value);
        }
        return total;
    }

    private void fillReceiptData() {
        if (this.receipt != null) {
            final User receiptClient = this.receipt.getUserByIduser();
            final SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
            final StringBuilder nameBuilder = new StringBuilder();
            nameBuilder.append(receiptClient.getName().toUpperCase());
            nameBuilder.append(" ");
            nameBuilder.append(receiptClient.getLastname().toUpperCase());
            this.jlbReceiptNumber.setText("RECIBO DE CAJA NO "
                    + this.receipt.getNumber());
            this.jlbReceiptDate.setText("Fecha: "
                    + format.format(this.receipt.getReceiptdate()));
            this.jlbReceiptClientId.setText("Cliente "
                    + receiptClient.getIdentification());
            this.jlbReceiptClientName.setText(nameBuilder.toString());

            final Object[][] data = this.receiptController
                    .buildItemConceptData(this.receipt);
            final ItemTableModel tableModel = new ItemTableModel(data);
            this.jtbReceiptItems.setModel(tableModel);
            this.jtbReceiptItems.setFillsViewportHeight(true);
            this.jtbReceiptItems.setEnabled(false);
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        groupEnabled = new javax.swing.ButtonGroup();
        pnTitle = new javax.swing.JPanel();
        lbTitle = new javax.swing.JLabel();
        pnSearch = new javax.swing.JPanel();
        jlbYear = new javax.swing.JLabel();
        jlbName = new javax.swing.JLabel();
        jtfYear = new javax.swing.JFormattedTextField();
        jlbMonth = new javax.swing.JLabel();
        jspMonth = new javax.swing.JScrollPane();
        jlsMonth = new javax.swing.JList<String>();
        jlbEnabled = new javax.swing.JLabel();
        rbtEnabled = new javax.swing.JRadioButton();
        rbtDeactivated = new javax.swing.JRadioButton();
        jtfIdentification = new javax.swing.JFormattedTextField();
        jlbIdentification = new javax.swing.JLabel();
        jbtSearchUser = new javax.swing.JButton();
        jlbLastName = new javax.swing.JLabel();
        jtfLastName = new javax.swing.JTextField();
        jbtSearch = new javax.swing.JButton();
        jbtClean = new javax.swing.JButton();
        jtfName = new javax.swing.JTextField();
        pnReceiptList = new javax.swing.JPanel();
        jbtPrintList = new javax.swing.JButton();
        jspReceiptsTable = new javax.swing.JScrollPane();
        jtbReceipts = new javax.swing.JTable();
        jlbTotal = new javax.swing.JLabel();
        jlbTotalMonth = new javax.swing.JLabel();
        jlbDebt = new javax.swing.JLabel();
        jlbVoluntarySave = new javax.swing.JLabel();
        pnReceipt = new javax.swing.JPanel();
        jlbViewReceiptNumber = new javax.swing.JLabel();
        jtfReceiptNumber = new javax.swing.JFormattedTextField();
        jbtViewReceipt = new javax.swing.JButton();
        jlbReceiptNumber = new javax.swing.JLabel();
        jlbReceiptDate = new javax.swing.JLabel();
        jlbReceiptClientId = new javax.swing.JLabel();
        jlbReceiptClientName = new javax.swing.JLabel();
        jbtPrint = new javax.swing.JButton();
        jspReceiptItemsTable = new javax.swing.JScrollPane();
        jtbReceiptItems = new javax.swing.JTable();
        lbImage = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/invoice.png")));
        setMinimumSize(new java.awt.Dimension(1070, 690));
        setName("jfReportReceive"); // NOI18N
        setPreferredSize(new java.awt.Dimension(1070, 690));

        pnTitle.setBackground(new java.awt.Color(255, 255, 255));
        pnTitle.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnTitle.setName("pnTitle"); // NOI18N

        lbTitle.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        lbTitle.setText("Ver recibos");

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

        jlbYear.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jlbYear.setText("año:");

        jlbName.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jlbName.setText("Nombre(s):");

        jtfYear.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("####0"))));
        jtfYear.setText(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        jtfYear.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N

        jlbMonth.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jlbMonth.setText("Mes:");

        jspMonth.setMinimumSize(new java.awt.Dimension(200, 100));
        jspMonth.setPreferredSize(new java.awt.Dimension(200, 100));

        jlsMonth.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        jlsMonth.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jspMonth.setViewportView(jlsMonth);

        jlbEnabled.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jlbEnabled.setText("Estado de los recibos:");

        rbtEnabled.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        rbtEnabled.setText("Activos");

        rbtDeactivated.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        rbtDeactivated.setText("Eliminados");

        jtfIdentification.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0"))));
        jtfIdentification.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N

        jlbIdentification.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jlbIdentification.setText("Identificación:");

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

        jlbLastName.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jlbLastName.setText("Apellido(s):");

        jtfLastName.setEditable(false);
        jtfLastName.setBackground(new java.awt.Color(255, 255, 255));
        jtfLastName.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        jtfLastName.setFocusable(false);
        jtfLastName.setPreferredSize(new java.awt.Dimension(200, 20));

        jbtSearch.setBackground(new java.awt.Color(16, 135, 221));
        jbtSearch.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jbtSearch.setForeground(new java.awt.Color(255, 255, 255));
        jbtSearch.setText("Ver");
        jbtSearch.setPreferredSize(new java.awt.Dimension(89, 23));
        jbtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtSearchActionPerformed(evt);
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

        jtfName.setEditable(false);
        jtfName.setBackground(new java.awt.Color(255, 255, 255));
        jtfName.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        jtfName.setFocusable(false);
        jtfName.setPreferredSize(new java.awt.Dimension(200, 20));

        javax.swing.GroupLayout pnSearchLayout = new javax.swing.GroupLayout(pnSearch);
        pnSearch.setLayout(pnSearchLayout);
        pnSearchLayout.setHorizontalGroup(
            pnSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnSearchLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlbEnabled)
                    .addGroup(pnSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnSearchLayout.createSequentialGroup()
                            .addComponent(rbtEnabled)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(rbtDeactivated))
                        .addComponent(jspMonth, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jlbYear, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jlbName, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jtfYear, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jlbMonth, javax.swing.GroupLayout.Alignment.LEADING))
                    .addComponent(jlbIdentification)
                    .addGroup(pnSearchLayout.createSequentialGroup()
                        .addComponent(jtfIdentification, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtSearchUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jlbLastName)
                    .addGroup(pnSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(pnSearchLayout.createSequentialGroup()
                            .addComponent(jbtClean, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jbtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jtfLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jtfName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        pnSearchLayout.setVerticalGroup(
            pnSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnSearchLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlbYear)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jlbMonth)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jspMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jlbEnabled)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbtEnabled)
                    .addComponent(rbtDeactivated))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jlbIdentification)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
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
                .addGap(18, 18, 18)
                .addGroup(pnSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtClean, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnReceiptList.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Listado de recibos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 1, 12))); // NOI18N
        pnReceiptList.setName("pnReceiptList"); // NOI18N
        pnReceiptList.setPreferredSize(new java.awt.Dimension(300, 200));

        jbtPrintList.setBackground(new java.awt.Color(16, 135, 221));
        jbtPrintList.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jbtPrintList.setForeground(new java.awt.Color(255, 255, 255));
        jbtPrintList.setText("Imprimir");
        jbtPrintList.setPreferredSize(new java.awt.Dimension(89, 23));
        jbtPrintList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtPrintListActionPerformed(evt);
            }
        });

        jtbReceipts.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        jtbReceipts.setModel(new javax.swing.table.DefaultTableModel(
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
        jspReceiptsTable.setViewportView(jtbReceipts);

        jlbTotal.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jlbTotal.setText("Total pagado: ");

        jlbTotalMonth.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jlbTotalMonth.setText("Total del mes: ");

        jlbDebt.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jlbDebt.setText("Total de la deuda: ");

        jlbVoluntarySave.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jlbVoluntarySave.setText("Total ahorro voluntario: ");

        javax.swing.GroupLayout pnReceiptListLayout = new javax.swing.GroupLayout(pnReceiptList);
        pnReceiptList.setLayout(pnReceiptListLayout);
        pnReceiptListLayout.setHorizontalGroup(
            pnReceiptListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnReceiptListLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnReceiptListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jspReceiptsTable, javax.swing.GroupLayout.DEFAULT_SIZE, 638, Short.MAX_VALUE)
                    .addGroup(pnReceiptListLayout.createSequentialGroup()
                        .addComponent(jbtPrintList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnReceiptListLayout.createSequentialGroup()
                        .addGroup(pnReceiptListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jlbTotal)
                            .addComponent(jlbDebt))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnReceiptListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlbVoluntarySave, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jlbTotalMonth, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        pnReceiptListLayout.setVerticalGroup(
            pnReceiptListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnReceiptListLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbtPrintList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jspReceiptsTable, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnReceiptListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlbTotal)
                    .addComponent(jlbTotalMonth))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnReceiptListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlbVoluntarySave)
                    .addComponent(jlbDebt))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnReceipt.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Recibo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 1, 12))); // NOI18N
        pnReceipt.setName("pnReceipt"); // NOI18N
        pnReceipt.setPreferredSize(new java.awt.Dimension(300, 200));

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

        javax.swing.GroupLayout pnReceiptLayout = new javax.swing.GroupLayout(pnReceipt);
        pnReceipt.setLayout(pnReceiptLayout);
        pnReceiptLayout.setHorizontalGroup(
            pnReceiptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnReceiptLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnReceiptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlbReceiptDate)
                    .addComponent(jlbReceiptClientId)
                    .addComponent(jlbReceiptClientName)
                    .addGroup(pnReceiptLayout.createSequentialGroup()
                        .addGroup(pnReceiptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnReceiptLayout.createSequentialGroup()
                                .addComponent(jlbViewReceiptNumber)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtfReceiptNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jlbReceiptNumber))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnReceiptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jbtPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jbtViewReceipt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jspReceiptItemsTable, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnReceiptLayout.setVerticalGroup(
            pnReceiptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnReceiptLayout.createSequentialGroup()
                .addGroup(pnReceiptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnReceiptLayout.createSequentialGroup()
                        .addGroup(pnReceiptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlbViewReceiptNumber)
                            .addComponent(jtfReceiptNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jbtViewReceipt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jlbReceiptNumber))
                    .addComponent(jbtPrint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jlbReceiptDate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlbReceiptClientId)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlbReceiptClientName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jspReceiptItemsTable, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                .addContainerGap())
        );

        lbImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/soin.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnReceipt, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                        .addComponent(lbImage, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnReceiptList, javax.swing.GroupLayout.PREFERRED_SIZE, 670, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnReceiptList, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(lbImage, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(pnReceipt, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
                                .addContainerGap())))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 466, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
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
            if (client == null) {
                this.client = null;
                this.jtfIdentification.setText("");
                ViewUtils.showMessage(this, MSG_NO_CLIENT, TITLE, infoMsg);
            }
        } else {
            this.client = null;
            ViewUtils.showMessage(this, MSG_EMPTY_ID, TITLE, infoMsg);
        }
        this.updateTextFields();
    }//GEN-LAST:event_jbtSearchUserActionPerformed

    private void jbtCleanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtCleanActionPerformed
        this.cleanAction();
    }//GEN-LAST:event_jbtCleanActionPerformed

    private void jbtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtSearchActionPerformed
        this.cleanReceiptData();
        if (validateYear() && !this.jlsMonth.isSelectionEmpty()) {
            final String yearStr = this.jtfYear.getText().replace(".", "")
                    .replace(",", "");
            final int year = Integer.parseInt(yearStr);
            final int month = this.jlsMonth.getSelectedIndex() + 1;
            final boolean enabled = this.rbtEnabled.isSelected();
            this.receiptList = this.receiptController.select(year, month,
                    this.client, enabled);
            if (this.receiptList == null || this.receiptList.size() == 0) {
                ViewUtils.showMessage(this, MSG_NO_RECORDS, TITLE,
                        JOptionPane.INFORMATION_MESSAGE);
            }
            this.fillReceiptTable();
            this.setViewReceiptFieldsVisibility(true);
        }
    }//GEN-LAST:event_jbtSearchActionPerformed

    private void jbtPrintListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtPrintListActionPerformed
        if (this.jbtPrintList.isVisible() && this.receiptList != null
        		&& this.receiptList.size() > 0) {
            final ThreadGenerator generator = new ThreadGenerator(this.receiptList);
            generator.start();
            ViewUtils.showMessage(this, MSG_PRINT_STARTED, TITLE,
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jbtPrintListActionPerformed

    private void jbtViewReceiptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtViewReceiptActionPerformed
        this.receipt = null;
        final int infoMsg = JOptionPane.INFORMATION_MESSAGE;
        final String receiptNumber = this.jtfReceiptNumber.getText().replace(".", "");
        if (!receiptNumber.equals("")) {
            this.receipt = this.receiptController.select(Long.parseLong(receiptNumber));
            if (this.receipt != null) {
                this.fillReceiptData();
            } else {
                ViewUtils.showMessage(this, MSG_NO_RECORDS, TITLE, infoMsg);
            }
        } else {
            ViewUtils.showMessage(this, MSG_EMPTY_RECEIPT, TITLE, infoMsg);
        }
    }//GEN-LAST:event_jbtViewReceiptActionPerformed

    private void jbtPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtPrintActionPerformed
        if (this.jbtPrint.isVisible() && this.receipt != null) {
            final ThreadGenerator generator = new ThreadGenerator(this.receipt);
            generator.start();
            ViewUtils.showMessage(this, MSG_PRINT_STARTED, TITLE,
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jbtPrintActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup groupEnabled;
    private javax.swing.JButton jbtClean;
    private javax.swing.JButton jbtPrint;
    private javax.swing.JButton jbtPrintList;
    private javax.swing.JButton jbtSearch;
    private javax.swing.JButton jbtSearchUser;
    private javax.swing.JButton jbtViewReceipt;
    private javax.swing.JLabel jlbDebt;
    private javax.swing.JLabel jlbEnabled;
    private javax.swing.JLabel jlbIdentification;
    private javax.swing.JLabel jlbLastName;
    private javax.swing.JLabel jlbMonth;
    private javax.swing.JLabel jlbName;
    private javax.swing.JLabel jlbReceiptClientId;
    private javax.swing.JLabel jlbReceiptClientName;
    private javax.swing.JLabel jlbReceiptDate;
    private javax.swing.JLabel jlbReceiptNumber;
    private javax.swing.JLabel jlbTotal;
    private javax.swing.JLabel jlbTotalMonth;
    private javax.swing.JLabel jlbViewReceiptNumber;
    private javax.swing.JLabel jlbVoluntarySave;
    private javax.swing.JLabel jlbYear;
    private javax.swing.JList<String> jlsMonth;
    private javax.swing.JScrollPane jspMonth;
    private javax.swing.JScrollPane jspReceiptItemsTable;
    private javax.swing.JScrollPane jspReceiptsTable;
    private javax.swing.JTable jtbReceiptItems;
    private javax.swing.JTable jtbReceipts;
    private javax.swing.JFormattedTextField jtfIdentification;
    private javax.swing.JTextField jtfLastName;
    private javax.swing.JTextField jtfName;
    private javax.swing.JFormattedTextField jtfReceiptNumber;
    private javax.swing.JFormattedTextField jtfYear;
    private javax.swing.JLabel lbImage;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JPanel pnReceipt;
    private javax.swing.JPanel pnReceiptList;
    private javax.swing.JPanel pnSearch;
    private javax.swing.JPanel pnTitle;
    private javax.swing.JRadioButton rbtDeactivated;
    private javax.swing.JRadioButton rbtEnabled;
    // End of variables declaration//GEN-END:variables
}

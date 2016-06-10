package co.com.soinsoftware.billing.util;

import java.math.BigDecimal;

import javax.swing.table.AbstractTableModel;

/**
 * @author Carlos Rodriguez
 * @since 09/06/2016
 * @version 1.0
 */
public class ItemTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 8408209589620109955L;

	private static final String[] COLUMN_NAMES = { "Concepto", "Valor" };

	private final Object[][] data;

	public ItemTableModel(final Object[][] data) {
		super();
		this.data = data;
	}

	@Override
	public int getColumnCount() {
		return COLUMN_NAMES.length;
	}

	@Override
	public int getRowCount() {
		return this.data.length;
	}

	@Override
	public Object getValueAt(final int row, final int col) {
		return this.data[row][col];
	}

	@Override
	public String getColumnName(final int col) {
		return COLUMN_NAMES[col];
	}

	@Override
	public boolean isCellEditable(final int row, final int col) {
		if (col == 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void setValueAt(final Object value, int row, int col) {
		String valueStr = value.toString();
		valueStr = valueStr.replace(".", "").replace(",", "");
		try {
			this.data[row][col] = new BigDecimal(valueStr);
			this.fireTableCellUpdated(row, col);
		} catch (NumberFormatException ex) {
			System.out.println(ex.getMessage());
			this.setValueAt("0", row, col);
		}
	}

	public Object[][] getData() {
		return this.data;
	}
}
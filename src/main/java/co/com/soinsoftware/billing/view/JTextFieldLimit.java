package co.com.soinsoftware.billing.view;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class JTextFieldLimit extends PlainDocument {

	private static final long serialVersionUID = 7587561499885595864L;

	private final int limit;

	JTextFieldLimit(final int limit) {
		super();
		this.limit = limit;
	}

	JTextFieldLimit(final int limit, final boolean upper) {
		super();
		this.limit = limit;
	}

	public void insertString(final int offset, final String str,
			final AttributeSet attr) throws BadLocationException {
		if (str == null) {
			return;
		}

		if ((this.getLength() + str.length()) <= this.limit) {
			super.insertString(offset, str, attr);
		}
	}
}

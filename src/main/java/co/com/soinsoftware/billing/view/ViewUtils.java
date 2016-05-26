package co.com.soinsoftware.billing.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JOptionPane;

public class ViewUtils {

	protected static final Color GREY = new Color(249, 249, 249);

	protected static final Color BLUE = new Color(16, 135, 221);

	protected static final Font VERDANA_BOLD = new Font("Verdana", Font.BOLD,
			12);

	protected static final Font VERDANA_PLAIN = new Font("Verdana", Font.PLAIN,
			12);

	private ViewUtils() {
		super();
	}

	protected static void showMessage(final Component component,
			final Object message, final String title, final int type) {
		JOptionPane.showMessageDialog(component, message, title, type);
	}
}
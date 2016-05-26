package co.com.soinsoftware.billing;

import java.awt.EventQueue;

import javax.swing.UIManager;

import co.com.soinsoftware.billing.view.JDLogin;

/**
 * @author Carlos Rodriguez
 * @since 26/05/2016
 * @version 1.0
 */
public class App {

	private static final String LOOK_AND_FEEL = "com.jtattoo.plaf.luna.LunaLookAndFeel";

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(LOOK_AND_FEEL);
					final JDLogin login = new JDLogin();
					login.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}

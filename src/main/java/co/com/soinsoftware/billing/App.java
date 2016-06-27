package co.com.soinsoftware.billing;

import java.awt.EventQueue;

import javax.swing.UIManager;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import co.com.soinsoftware.billing.dao.SessionController;
import co.com.soinsoftware.billing.view.JFLogin;
import co.com.soinsoftware.billing.view.JFLogo;

/**
 * @author Carlos Rodriguez
 * @since 26/05/2016
 * @version 1.0
 */
public class App {

	private static final String LOOK_AND_FEEL = "com.jtattoo.plaf.luna.LunaLookAndFeel";

	public static void main(String[] args) {
		final JFLogo logo = new JFLogo();
		logo.setVisible(true);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SessionController.getInstance();
					System.out.println("Initializing jasper report context");
					DefaultJasperReportsContext.getInstance();
					System.out.println("Finalizing initialization of jasper report context");
					UIManager.setLookAndFeel(LOOK_AND_FEEL);
					final JFLogin login = new JFLogin();
					login.setVisible(true);
					logo.setVisible(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}

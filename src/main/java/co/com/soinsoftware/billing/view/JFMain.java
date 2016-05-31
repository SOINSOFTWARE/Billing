package co.com.soinsoftware.billing.view;

import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import co.com.soinsoftware.billing.controller.MenuController;
import co.com.soinsoftware.billing.entity.User;

/**
 * @author Carlos Rodriguez
 * @since 30/05/2016
 * @version 1.0
 */
public class JFMain extends JFrame {

	private static final long serialVersionUID = -5816892555495529552L;

	private final User loggedUser;

	public JFMain(final User loggedUser) {
		super();
		this.loggedUser = loggedUser;
		this.setBackground(ViewUtils.GREY);
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle(this.loggedUser.getCompany().getName());
	}
	
	public void addController(final MenuController controller) {
		final JMenuBar menuBar = new JMBAppMenu(controller);
		this.setJMenuBar(menuBar);
	}
}
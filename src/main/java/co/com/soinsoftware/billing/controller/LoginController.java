package co.com.soinsoftware.billing.controller;

import co.com.soinsoftware.billing.BLL.UserBLL;
import co.com.soinsoftware.billing.entity.User;


public class LoginController {
	
	private final UserBLL userBLL;
	
	public LoginController() {
		super();
		this.userBLL = UserBLL.getInstance();
	}

	
	public User selectUser(final String login, final char[] password) {
		String strPassword = String.valueOf(password);
		return this.userBLL.select(login, strPassword);
	}
}
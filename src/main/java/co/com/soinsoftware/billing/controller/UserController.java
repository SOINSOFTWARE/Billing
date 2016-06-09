package co.com.soinsoftware.billing.controller;

import java.math.BigDecimal;
import java.util.Date;

import co.com.soinsoftware.billing.bll.UserBLL;
import co.com.soinsoftware.billing.entity.Company;
import co.com.soinsoftware.billing.entity.User;

/**
 * @author Carlos Rodriguez
 * @since 31/05/2016
 * @version 1.0
 */
public class UserController {

	private final UserBLL userBLL;

	public UserController() {
		super();
		this.userBLL = UserBLL.getInstance();
	}

	public boolean isExistingUser(final long identification) {
		return (this.selectUser(identification) != null);
	}

	public User selectUser(final long identification) {
		return this.userBLL.select(identification);
	}

	public void saveUser(final Company company, final long identification,
			final String name, final String lastName, final BigDecimal value) {
		User user = this.selectUser(identification);
		final Date currentDate = new Date();
		if (user == null) {
			user = new User(company, identification, name, lastName, value,
					currentDate, currentDate, true);
		} else {
			user.setUpdated(currentDate);
		}
		this.saveUser(user);
	}

	public void saveUser(final User user) {
		this.userBLL.save(user);
	}
}
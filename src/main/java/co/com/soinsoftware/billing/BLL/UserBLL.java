package co.com.soinsoftware.billing.BLL;

import co.com.soinsoftware.billing.DAO.UserDAO;
import co.com.soinsoftware.billing.entity.User;

/**
 * @author Carlos Rodriguez
 * @since 26/05/2016
 * @version 1.0
 */
public class UserBLL {
	
	private static UserBLL instance; 
	
	private final UserDAO dao;
	
	public static UserBLL getInstance() {
		if (instance == null) {
			instance = new UserBLL();
		}
		return instance;
	}
	
	public User select(final String login, final String password) {
		return this.dao.select(login, password);
	}
	
	private UserBLL() {
		super();
		dao = new UserDAO();
	}
}
package co.com.soinsoftware.billing.bll;

import co.com.soinsoftware.billing.dao.ConfigurationDAO;
import co.com.soinsoftware.billing.entity.Configuration;

/**
 * @author Carlos Rodriguez
 * @since 31/05/2016
 * @version 1.0
 */
public class ConfigurationBLL {
	
	private static ConfigurationBLL instance;
	
	private final ConfigurationDAO dao;
	
	public static ConfigurationBLL getInstance() {
		if (instance == null) {
			instance = new ConfigurationBLL();
		}
		return instance;
	}
	
	public Configuration select(final int idCompany) {
		return this.dao.select(idCompany);
	}
	
	public void save(final Configuration configuration) {
		this.dao.save(configuration);
	}

	private ConfigurationBLL() {
		super();
		this.dao = new ConfigurationDAO();
	}
}
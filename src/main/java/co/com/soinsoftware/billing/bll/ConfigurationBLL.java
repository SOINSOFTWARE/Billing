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
	
	private Configuration configuration;
	
	public static ConfigurationBLL getInstance() {
		if (instance == null) {
			instance = new ConfigurationBLL();
		}
		return instance;
	}
	
	public Configuration select(final int idCompany) {
		if (this.configuration == null) {
			this.configuration = this.dao.select(idCompany);
		}
		return this.configuration;
	}
	
	public void save(final Configuration configuration) {
		this.dao.save(configuration);
		this.configuration = configuration;
	}

	private ConfigurationBLL() {
		super();
		this.dao = new ConfigurationDAO();
	}
}
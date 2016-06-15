package co.com.soinsoftware.billing.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import co.com.soinsoftware.billing.bll.ConfigurationBLL;
import co.com.soinsoftware.billing.bll.ItemConceptBLL;
import co.com.soinsoftware.billing.entity.Company;
import co.com.soinsoftware.billing.entity.Configuration;
import co.com.soinsoftware.billing.entity.Itemconcept;
import co.com.soinsoftware.billing.entity.User;

/**
 * @author Carlos Rodriguez
 * @since 15/06/2016
 * @version 1.0
 */
public class ItemConceptController {

	private final ConfigurationBLL configBLL;

	private final ItemConceptBLL itemConceptBLL;

	public ItemConceptController() {
		super();
		this.configBLL = ConfigurationBLL.getInstance();
		this.itemConceptBLL = ItemConceptBLL.getInstance();
	}

	public List<Itemconcept> getItemConceptList(final User loggedUser,
			final boolean enabled) {
		final Configuration config = this.getConfiguration(loggedUser);
		final Set<Itemconcept> itemConceptSet = config.getItemconcepts();
		return this.getItemConceptList(itemConceptSet, enabled);
	}

	public void saveItemConcept(final User loggedUser, final String name) {
		final Configuration config = this.getConfiguration(loggedUser);
		final Date currentDate = new Date();
		final Itemconcept itemConcept = new Itemconcept(config, name,
				new BigDecimal(0), currentDate, currentDate, true);
		this.saveItemConcept(loggedUser, itemConcept);
	}

	public void saveItemConcept(final User loggedUser,
			final Itemconcept itemConcept) {
		final Configuration config = this.getConfiguration(loggedUser);
		this.itemConceptBLL.save(itemConcept);
		final Set<Itemconcept> itemConceptSet = config.getItemconcepts();
		itemConceptSet.add(itemConcept);
		config.setItemconcepts(itemConceptSet);
	}

	public Itemconcept getItemConceptByName(final User loggedUser,
			final String name) {
		Itemconcept itemConcept = null;
		final Configuration config = this.getConfiguration(loggedUser);
		final Set<Itemconcept> itemConceptSet = config.getItemconcepts();
		for (final Itemconcept itemConceptConfig : itemConceptSet) {
			if (itemConceptConfig.getName().equals(name)) {
				itemConcept = itemConceptConfig;
				break;
			}
		}
		return itemConcept;
	}

	private Configuration getConfiguration(final User loggedUser) {
		final Company company = loggedUser.getCompany();
		return this.configBLL.select(company.getId());
	}

	private List<Itemconcept> getItemConceptList(
			final Set<Itemconcept> itemConceptSet, final boolean enabled) {
		List<Itemconcept> itemConceptList = new ArrayList<>();
		if (itemConceptSet != null) {
			for (final Itemconcept itemConcept : itemConceptSet) {
				if (itemConcept.isEnabled() == enabled) {
					itemConceptList.add(itemConcept);
				}
			}
		}
		Collections.sort(itemConceptList);
		return itemConceptList;
	}
}
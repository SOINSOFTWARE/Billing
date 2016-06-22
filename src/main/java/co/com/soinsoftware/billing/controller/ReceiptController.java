package co.com.soinsoftware.billing.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import co.com.soinsoftware.billing.bll.ConfigurationBLL;
import co.com.soinsoftware.billing.bll.ItemBLL;
import co.com.soinsoftware.billing.bll.ReceiptBLL;
import co.com.soinsoftware.billing.entity.Configuration;
import co.com.soinsoftware.billing.entity.Item;
import co.com.soinsoftware.billing.entity.ItemId;
import co.com.soinsoftware.billing.entity.Itemconcept;
import co.com.soinsoftware.billing.entity.Receipt;
import co.com.soinsoftware.billing.entity.User;

/**
 * @author Carlos Rodriguez
 * @since 31/05/2016
 * @version 1.0
 */
public class ReceiptController {

	private final ConfigurationBLL configBLL;

	private final ReceiptBLL receiptBLL;

	private final ItemBLL itemBLL;

	public ReceiptController() {
		super();
		this.configBLL = ConfigurationBLL.getInstance();
		this.receiptBLL = ReceiptBLL.getInstance();
		this.itemBLL = ItemBLL.getInstance();
	}

	public Receipt createReceipt(final User loggedUser, final User client,
			final int value) {
		Receipt receipt = null;
		final Configuration config = this.configBLL.select(client.getCompany()
				.getId());
		if (config.getNumberto() == 0
				|| config.getNumberto() >= config.getNumbercurrent()) {
			final Date currentDate = new Date();
			receipt = new Receipt(client.getCompany(), config, client,
					loggedUser, loggedUser, config.getNumbercurrent(),
					currentDate, currentDate, currentDate, true);
			this.createReceiptItems(receipt, config, value);
		}
		return receipt;
	}

	public boolean saveReceipt(final Receipt receipt) {
		boolean saved = false;
		boolean updateConfig = false;
		this.receiptBLL.save(receipt);
		if (receipt.getId() != null && receipt.getId() > 0) {
			final int idreceipt = receipt.getId();
			for (final Item item : receipt.getItemSet()) {
				if (item.getId().getIdreceipt() == 0) {
					item.getId().setIdreceipt(idreceipt);
					this.itemBLL.save(item);
					updateConfig = true;
				}
			}
			if (updateConfig) {
				final Configuration config = this.configBLL.select(receipt
						.getCompany().getId());
				this.saveConfiguration(config);
			}
			saved = true;
		}
		return saved;
	}

	public Object[][] buildReceiptData(final List<Receipt> receiptList) {
		final int itemSize = receiptList.size();
		final Object[][] data = new Object[itemSize][5];
		int index = 0;
		for (final Receipt receipt : receiptList) {
			final User client = receipt.getUserByIduser();
			client.fillNonDbFields();
			data[index][0] = client.getIdentification();
			data[index][1] = client.getFullName();
			data[index][2] = receipt.getFormatedReceiptDate();
			data[index][3] = receipt.getNumber();
			data[index][4] = receipt.getValue();
			index++;
		}
		return data;
	}

	public Object[][] buildItemConceptData(final Receipt receipt) {
		final List<Item> itemList = new ArrayList<Item>(receipt.getItemSet());
		final int itemSize = receipt.getItemSet().size();
		final Object[][] data = new Object[itemSize][2];
		this.fillItemConceptName(receipt, itemList);
		Collections.sort(itemList);
		int index = 0;
		for (final Item item : itemList) {
			data[index][0] = item.getConceptName();
			data[index][1] = item.getValue();
			index++;
		}
		return data;
	}

	public List<Receipt> select(final int year, final int month,
			final User client, final boolean enabled) {
		return this.receiptBLL.select(year, month, client, enabled);
	}

	public Receipt select(final long number) {
		return this.receiptBLL.select(number);
	}

	public BigDecimal selectTotal(final User client) {
		return this.receiptBLL.selectTotal(client);
	}
	
	public BigDecimal selectVoluntarySave(final User client) {
		return this.receiptBLL.selectVoluntarySave(client);
	}

	private void fillItemConceptName(final Receipt receipt,
			final List<Item> itemList) {
		final Set<Itemconcept> itemConceptSet = receipt.getConfiguration()
				.getItemconcepts();
		for (final Item item : itemList) {
			final String name = this.getItemConceptName(item.getId()
					.getIditemconcept(), itemConceptSet);
			item.setConceptName(name);
		}
	}

	private String getItemConceptName(final int idItemConcept,
			final Set<Itemconcept> itemConceptSet) {
		String name = "";
		for (final Itemconcept concept : itemConceptSet) {
			if (concept.getId().equals(idItemConcept)) {
				name = concept.getName();
				break;
			}
		}
		return name;
	}

	private void saveConfiguration(final Configuration configuration) {
		final long current = configuration.getNumbercurrent() + 1;
		configuration.setNumbercurrent(current);
		this.configBLL.save(configuration);
	}

	private void createReceiptItems(final Receipt receipt,
			final Configuration config, final int value) {
		final Date currentDate = new Date();
		if (config.getItemconcepts() != null) {
			for (final Itemconcept itemConcept : config.getItemconcepts()) {
				if (itemConcept.isEnabled()) {
					final BigDecimal valBigDec = new BigDecimal(0);
					final ItemId itemId = new ItemId(0, itemConcept.getId());
					final Item item = new Item(itemId, valBigDec, currentDate,
							currentDate, true);
					receipt.addItemSet(item);
				}
			}
		}
	}
}
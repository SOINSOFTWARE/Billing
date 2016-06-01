package co.com.soinsoftware.billing.controller;

import java.math.BigDecimal;
import java.util.Date;

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
		final Configuration config = this.configBLL.select(client.getCompany()
				.getId());
		final Date currentDate = new Date();
		final Receipt receipt = new Receipt(client.getCompany(), config,
				client, loggedUser, loggedUser, config.getNumbercurrent(),
				currentDate, currentDate, currentDate, true);
		this.createReceiptItems(receipt, config, value);
		return receipt;
	}

	public boolean saveReceipt(final Receipt receipt) {
		boolean saved = false;
		this.receiptBLL.save(receipt);
		if (receipt.getId() != null && receipt.getId() > 0) {
			final int idreceipt = receipt.getId();
			for (final Item item : receipt.getItemSet()) {
				item.getId().setIdreceipt(idreceipt);
				this.itemBLL.save(item);
			}
			final Configuration config = this.configBLL.select(receipt
					.getCompany().getId());
			this.saveConfiguration(config);
			saved = true;
		}
		return saved;
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
				double itemVal = (double) value
						* (itemConcept.getPercentage().doubleValue() / 100);
				final BigDecimal valBigDec = new BigDecimal(itemVal);
				final ItemId itemId = new ItemId(0, itemConcept.getId());
				final Item item = new Item(itemId, valBigDec, currentDate,
						currentDate, true);
				receipt.addItemSet(item);
			}
		}
	}
}
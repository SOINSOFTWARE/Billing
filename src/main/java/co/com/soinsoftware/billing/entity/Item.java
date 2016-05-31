package co.com.soinsoftware.billing.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Carlos Rodriguez
 * @since 26/05/2016
 * @version 1.0
 */
public class Item implements Serializable {

	private static final long serialVersionUID = -1270506565047901571L;

	private ItemId id;

	private BigDecimal value;

	private Date creation;

	private Date updated;

	private boolean enabled;

	public Item() {
		super();
	}

	public Item(final ItemId id, final BigDecimal value, final Date creation,
			final Date updated, final boolean enabled) {
		this.id = id;
		this.value = value;
		this.creation = creation;
		this.updated = updated;
		this.enabled = enabled;
	}

	public ItemId getId() {
		return this.id;
	}

	public void setId(final ItemId id) {
		this.id = id;
	}

	public BigDecimal getValue() {
		return this.value;
	}

	public void setValue(final BigDecimal value) {
		this.value = value;
	}

	public Date getCreation() {
		return this.creation;
	}

	public void setCreation(final Date creation) {
		this.creation = creation;
	}

	public Date getUpdated() {
		return this.updated;
	}

	public void setUpdated(final Date updated) {
		this.updated = updated;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}
}
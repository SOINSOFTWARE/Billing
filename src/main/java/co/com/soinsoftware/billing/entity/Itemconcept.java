package co.com.soinsoftware.billing.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Carlos Rodriguez
 * @since 26/05/2016
 * @version 1.0
 */
public class Itemconcept implements Serializable, Comparable<Itemconcept> {

	private static final long serialVersionUID = 5079622598994961247L;

	private Integer id;

	private Configuration configuration;

	private String name;

	private BigDecimal percentage;

	private Date creation;

	private Date updated;

	private boolean enabled;

	public Itemconcept() {
		super();
	}

	public Itemconcept(final Configuration configuration, final String name,
			final BigDecimal percentage, final Date creation,
			final Date updated, final boolean enabled) {
		this.configuration = configuration;
		this.name = name;
		this.percentage = percentage;
		this.creation = creation;
		this.updated = updated;
		this.enabled = enabled;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public Configuration getConfiguration() {
		return this.configuration;
	}

	public void setConfiguration(final Configuration configuration) {
		this.configuration = configuration;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public BigDecimal getPercentage() {
		return this.percentage;
	}

	public void setPercentage(final BigDecimal percentage) {
		this.percentage = percentage;
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

	@Override
	public int compareTo(final Itemconcept other) {
		final String otherName = other.getName();
		return this.name.compareToIgnoreCase(otherName);
	}
}
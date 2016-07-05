package co.com.soinsoftware.billing.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Carlos Rodriguez
 * @since 30/06/2016
 * @version 1.0
 */
public class Credit implements Serializable, Comparable<Credit> {

	private static final long serialVersionUID = -1956125793683830530L;

	private Integer id;

	private Credittype creditType;

	private User user;
	
	private BigDecimal value;

	private Date creation;

	private Date updated;

	private boolean enabled;

	public Credit() {
		super();
	}

	public Credit(final Credittype creditType, final User user,
			final BigDecimal value, final Date creation, final Date updated,
			final boolean enabled) {
		this.creditType = creditType;
		this.user = user;
		this.value = value;
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

	public Credittype getCreditType() {
		return this.creditType;
	}

	public void setCreditType(final Credittype creditType) {
		this.creditType = creditType;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(final User user) {
		this.user = user;
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

	@Override
	public int compareTo(final Credit other) {
		final Date otherCreditDate = other.getCreation();
		return this.creation.compareTo(otherCreditDate) * -1;
	}
}
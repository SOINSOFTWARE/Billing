package co.com.soinsoftware.billing.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Carlos Rodriguez
 * @since 30/06/2016
 * @version 1.0
 */
public class Credittype implements Serializable {

	private static final long serialVersionUID = -1240552216426461100L;

	private Integer id;

	private String name;

	private Date creation;

	private Date updated;

	private boolean enabled;

	public Credittype() {
		super();
	}

	public Credittype(final String name, final Date creation,
			final Date updated, final boolean enabled) {
		super();
		this.name = name;
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

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
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
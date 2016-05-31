package co.com.soinsoftware.billing.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Carlos Rodriguez
 * @since 30/05/2016
 * @version 1.0
 */
public class Configuration implements Serializable {

	private static final long serialVersionUID = 275465406322280111L;

	private Integer id;

	private Company company;

	private String name;

	private long from;

	private long to;

	private long current;

	private Date creation;

	private Date updated;

	private boolean enabled;

	private Set<Receipt> receipts = new HashSet<Receipt>(0);

	private Set<Itemconcept> itemconcepts = new HashSet<Itemconcept>(0);

	public Configuration() {
		super();
	}

	public Configuration(final Company company, final String name,
			final long from, final long to, final long current,
			final Date creation, final Date updated, final boolean enabled) {
		this.company = company;
		this.name = name;
		this.from = from;
		this.to = to;
		this.current = current;
		this.creation = creation;
		this.updated = updated;
		this.enabled = enabled;
	}

	public Configuration(final Company company, final String name,
			final long from, final long to, final long current,
			final Date creation, final Date updated, final boolean enabled,
			final Set<Receipt> receipts, final Set<Itemconcept> itemconcepts) {
		this.company = company;
		this.name = name;
		this.from = from;
		this.to = to;
		this.current = current;
		this.creation = creation;
		this.updated = updated;
		this.enabled = enabled;
		this.receipts = receipts;
		this.itemconcepts = itemconcepts;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public Company getCompany() {
		return this.company;
	}

	public void setCompany(final Company company) {
		this.company = company;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public long getFrom() {
		return this.from;
	}

	public void setFrom(final long from) {
		this.from = from;
	}

	public long getTo() {
		return this.to;
	}

	public void setTo(final long to) {
		this.to = to;
	}

	public long getCurrent() {
		return this.current;
	}

	public void setCurrent(final long current) {
		this.current = current;
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

	public Set<Receipt> getReceipts() {
		return this.receipts;
	}

	public void setReceipts(final Set<Receipt> receipts) {
		this.receipts = receipts;
	}

	public Set<Itemconcept> getItemconcepts() {
		return this.itemconcepts;
	}

	public void setItemconcepts(final Set<Itemconcept> itemconcepts) {
		this.itemconcepts = itemconcepts;
	}
}
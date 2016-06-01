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

	private long numberfrom;

	private long numberto;

	private long numbercurrent;

	private Date creation;

	private Date updated;

	private boolean enabled;

	private Set<Itemconcept> itemconcepts = new HashSet<Itemconcept>(0);

	public Configuration() {
		super();
	}

	public Configuration(final Company company, final String name,
			final long numberfrom, final long numberto,
			final long numbercurrent, final Date creation, final Date updated,
			final boolean enabled) {
		this.company = company;
		this.name = name;
		this.numberfrom = numberfrom;
		this.numberto = numberto;
		this.numbercurrent = numbercurrent;
		this.creation = creation;
		this.updated = updated;
		this.enabled = enabled;
	}

	public Configuration(final Company company, final String name,
			final long numberfrom, final long numberto,
			final long numbercurrent, final Date creation, final Date updated,
			final boolean enabled, final Set<Itemconcept> itemconcepts) {
		this.company = company;
		this.name = name;
		this.numberfrom = numberfrom;
		this.numberto = numberto;
		this.numbercurrent = numbercurrent;
		this.creation = creation;
		this.updated = updated;
		this.enabled = enabled;
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

	public long getNumberfrom() {
		return this.numberfrom;
	}

	public void setNumberfrom(final long numberfrom) {
		this.numberfrom = numberfrom;
	}

	public long getNumberto() {
		return this.numberto;
	}

	public void setNumberto(final long numberto) {
		this.numberto = numberto;
	}

	public long getNumbercurrent() {
		return this.numbercurrent;
	}

	public void setNumbercurrent(final long numbercurrent) {
		this.numbercurrent = numbercurrent;
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

	public Set<Itemconcept> getItemconcepts() {
		return this.itemconcepts;
	}

	public void setItemconcepts(final Set<Itemconcept> itemconcepts) {
		this.itemconcepts = itemconcepts;
	}
}
package co.com.soinsoftware.billing.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Carlos Rodriguez
 * @since 26/05/2016
 * @version 1.0
 */
public class Receipt implements Serializable {

	private static final long serialVersionUID = -1019948042230110690L;

	private Integer id;

	private Company company;

	private Configuration configuration;

	private User userByIduser;

	private User userByIdcreatoruser;

	private User userByIdlastchangeuser;

	private long number;

	private Date date;

	private Date creation;

	private Date updated;

	private boolean enabled;
	
	private Set<Item> itemSet;

	public Receipt() {
		super();
	}

	public Receipt(final Company company, final Configuration configuration,
			final User userByIduser, final User userByIdcreatoruser,
			final User userByIdlastchangeuser, final long number,
			final Date date, final Date creation, final Date updated,
			final boolean enabled) {
		this.company = company;
		this.configuration = configuration;
		this.userByIduser = userByIduser;
		this.userByIdcreatoruser = userByIdcreatoruser;
		this.userByIdlastchangeuser = userByIdlastchangeuser;
		this.number = number;
		this.date = date;
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

	public Company getCompany() {
		return this.company;
	}

	public void setCompany(final Company company) {
		this.company = company;
	}

	public Configuration getConfiguration() {
		return this.configuration;
	}

	public void setConfiguration(final Configuration configuration) {
		this.configuration = configuration;
	}

	public User getUserByIduser() {
		return this.userByIduser;
	}

	public void setUserByIduser(final User userByIduser) {
		this.userByIduser = userByIduser;
	}

	public User getUserByIdcreatoruser() {
		return this.userByIdcreatoruser;
	}

	public void setUserByIdcreatoruser(final User userByIdcreatoruser) {
		this.userByIdcreatoruser = userByIdcreatoruser;
	}

	public User getUserByIdlastchangeuser() {
		return this.userByIdlastchangeuser;
	}

	public void setUserByIdlastchangeuser(final User userByIdlastchangeuser) {
		this.userByIdlastchangeuser = userByIdlastchangeuser;
	}

	public long getNumber() {
		return this.number;
	}

	public void setNumber(final long number) {
		this.number = number;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(final Date date) {
		this.date = date;
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

	public Set<Item> getItemSet() {
		return itemSet;
	}

	public void addItemSet(final Item item) {
		if (this.itemSet == null) {
			this.itemSet = new HashSet<Item>();
		}
		this.itemSet.add(item);
	}
}
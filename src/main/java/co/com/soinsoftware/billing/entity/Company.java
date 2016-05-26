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
public class Company implements Serializable {

	private static final long serialVersionUID = 2049688411124360740L;

	private Integer id;

	private String name;

	private String nickname;

	private String nit;

	private Date creation;

	private Date updated;

	private boolean enabled;

	private Set<Configuration> configurations = new HashSet<Configuration>(0);

	private Set<User> users = new HashSet<User>(0);

	private Set<Receipt> receipts = new HashSet<Receipt>(0);

	public Company() {
		super();
	}

	public Company(final String name, final String nit, final Date creation,
			final Date updated, final boolean enabled) {
		super();
		this.name = name;
		this.nit = nit;
		this.creation = creation;
		this.updated = updated;
		this.enabled = enabled;
	}

	public Company(final String name, final String nickname, final String nit,
			final Date creation, final Date updated, final boolean enabled,
			final Set<Configuration> configurations, final Set<User> users,
			final Set<Receipt> receipts) {
		super();
		this.name = name;
		this.nickname = nickname;
		this.nit = nit;
		this.creation = creation;
		this.updated = updated;
		this.enabled = enabled;
		this.configurations = configurations;
		this.users = users;
		this.receipts = receipts;
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

	public String getNickname() {
		return this.nickname;
	}

	public void setNickname(final String nickname) {
		this.nickname = nickname;
	}

	public String getNit() {
		return this.nit;
	}

	public void setNit(final String nit) {
		this.nit = nit;
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

	public Set<Configuration> getConfigurations() {
		return this.configurations;
	}

	public void setConfigurations(final Set<Configuration> configurations) {
		this.configurations = configurations;
	}

	public Set<User> getUsers() {
		return this.users;
	}

	public void setUsers(final Set<User> users) {
		this.users = users;
	}

	public Set<Receipt> getReceipts() {
		return this.receipts;
	}

	public void setReceipts(final Set<Receipt> receipts) {
		this.receipts = receipts;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((nit == null) ? 0 : nit.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Company other = (Company) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (nit == null) {
			if (other.nit != null)
				return false;
		} else if (!nit.equals(other.nit))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Company [id=" + id + ", name=" + name + ", nickname="
				+ nickname + ", nit=" + nit + ", creation=" + creation
				+ ", updated=" + updated + ", enabled=" + enabled
				+ ", configurations=" + configurations + ", users=" + users
				+ ", receipts=" + receipts + "]";
	}
}
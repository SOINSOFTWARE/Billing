package co.com.soinsoftware.billing.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Carlos Rodriguez
 * @since 26/05/2016
 * @version 1.0
 */
public class User implements Serializable {

	private static final long serialVersionUID = 8038182437700662639L;

	private Integer id;

	private Company company;

	private long identification;

	private String name;

	private String lastname;

	private BigDecimal value;

	private String login;

	private String password;

	private Date creation;

	private Date updated;

	private boolean enabled;

	private Set<Receipt> receiptsForIduser = new HashSet<Receipt>(0);

	private Set<Receipt> receiptsForIdcreatoruser = new HashSet<Receipt>(0);

	private Set<Receipt> receiptsForIdlastchangeuser = new HashSet<Receipt>(0);

	private volatile String fullName;

	public User() {
		super();
	}

	public User(final Company company, final long identification,
			final String name, final String lastname, final BigDecimal value,
			final Date creation, final Date updated, final boolean enabled) {
		super();
		this.company = company;
		this.identification = identification;
		this.name = name;
		this.lastname = lastname;
		this.value = value;
		this.creation = creation;
		this.updated = updated;
		this.enabled = enabled;
		this.fullName = this.name + " " + this.lastname;
	}

	public User(final Company company, final long identification,
			final String name, final String lastname, final BigDecimal value,
			final String login, final String password, final Date creation,
			final Date updated, final boolean enabled,
			final Set<Receipt> receiptsForIduser,
			final Set<Receipt> receiptsForIdcreatoruser,
			final Set<Receipt> receiptsForIdlastchangeuser) {
		super();
		this.company = company;
		this.identification = identification;
		this.name = name;
		this.lastname = lastname;
		this.value = value;
		this.login = login;
		this.password = password;
		this.creation = creation;
		this.updated = updated;
		this.enabled = enabled;
		this.receiptsForIduser = receiptsForIduser;
		this.receiptsForIdcreatoruser = receiptsForIdcreatoruser;
		this.receiptsForIdlastchangeuser = receiptsForIdlastchangeuser;
		this.fullName = this.name + " " + this.lastname;
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

	public long getIdentification() {
		return this.identification;
	}

	public void setIdentification(final long identification) {
		this.identification = identification;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getLastname() {
		return this.lastname;
	}

	public void setLastname(final String lastname) {
		this.lastname = lastname;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(final String login) {
		this.login = login;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(final String password) {
		this.password = password;
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

	public Set<Receipt> getReceiptsForIduser() {
		return this.receiptsForIduser;
	}

	public void setReceiptsForIduser(final Set<Receipt> receiptsForIduser) {
		this.receiptsForIduser = receiptsForIduser;
	}

	public Set<Receipt> getReceiptsForIdcreatoruser() {
		return this.receiptsForIdcreatoruser;
	}

	public void setReceiptsForIdcreatoruser(
			final Set<Receipt> receiptsForIdcreatoruser) {
		this.receiptsForIdcreatoruser = receiptsForIdcreatoruser;
	}

	public Set<Receipt> getReceiptsForIdlastchangeuser() {
		return this.receiptsForIdlastchangeuser;
	}

	public void setReceiptsForIdlastchangeuser(
			final Set<Receipt> receiptsForIdlastchangeuser) {
		this.receiptsForIdlastchangeuser = receiptsForIdlastchangeuser;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (int) (identification ^ (identification >>> 32));
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
		User other = (User) obj;
		if (identification != other.identification)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", company=" + company + ", identification="
				+ identification + ", name=" + name + ", lastname=" + lastname
				+ ", login=" + login + ", password=" + password + ", creation="
				+ creation + ", updated=" + updated + ", enabled=" + enabled
				+ ", receipts=" + receiptsForIduser + "]";
	}

	public void fillNonDbFields() {
		this.fullName = this.name + " " + this.lastname;
	}
}
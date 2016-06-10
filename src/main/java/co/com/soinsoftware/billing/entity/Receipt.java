package co.com.soinsoftware.billing.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Carlos Rodriguez
 * @since 26/05/2016
 * @version 1.0
 */
public class Receipt implements Serializable, Comparable<Receipt> {

	private static final long serialVersionUID = -1019948042230110690L;

	private static final SimpleDateFormat FORMAT = new SimpleDateFormat(
			"dd-MMM-yyyy");

	private Integer id;

	private Company company;

	private Configuration configuration;

	private User userByIduser;

	private User userByIdcreatoruser;

	private User userByIdlastchangeuser;

	private long number;

	private Date receiptdate;

	private Date creation;

	private Date updated;

	private boolean enabled;

	private volatile String formatedReceiptDate;

	private volatile BigDecimal value;

	private Set<Item> itemSet = new HashSet<>(0);

	public Receipt() {
		super();
	}

	public Receipt(final Company company, final Configuration configuration,
			final User userByIduser, final User userByIdcreatoruser,
			final User userByIdlastchangeuser, final long number,
			final Date receiptdate, final Date creation, final Date updated,
			final boolean enabled) {
		this.company = company;
		this.configuration = configuration;
		this.userByIduser = userByIduser;
		this.userByIdcreatoruser = userByIdcreatoruser;
		this.userByIdlastchangeuser = userByIdlastchangeuser;
		this.number = number;
		this.receiptdate = receiptdate;
		this.creation = creation;
		this.updated = updated;
		this.enabled = enabled;
		this.setValue(new BigDecimal(0));
		this.setFormatedReceiptDate(FORMAT.format(receiptdate));
	}

	public Receipt(final Company company, final Configuration configuration,
			final User userByIduser, final User userByIdcreatoruser,
			final User userByIdlastchangeuser, final long number,
			final Date receiptdate, final Date creation, final Date updated,
			final boolean enabled, final Set<Item> itemSet) {
		this.company = company;
		this.configuration = configuration;
		this.userByIduser = userByIduser;
		this.userByIdcreatoruser = userByIdcreatoruser;
		this.userByIdlastchangeuser = userByIdlastchangeuser;
		this.number = number;
		this.receiptdate = receiptdate;
		this.creation = creation;
		this.updated = updated;
		this.enabled = enabled;
		this.itemSet = itemSet;
		this.setValue(this.getTotalValue());
		this.setFormatedReceiptDate(FORMAT.format(receiptdate));
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

	public Date getReceiptdate() {
		return this.receiptdate;
	}

	public void setReceiptdate(final Date receiptdate) {
		this.receiptdate = receiptdate;
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

	public void setItemSet(final Set<Item> itemSet) {
		this.itemSet = itemSet;
	}

	public void addItemSet(final Item item) {
		if (this.itemSet == null) {
			this.itemSet = new HashSet<Item>();
		}
		this.itemSet.add(item);
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getFormatedReceiptDate() {
		return formatedReceiptDate;
	}

	public void setFormatedReceiptDate(String formatedReceiptDate) {
		this.formatedReceiptDate = formatedReceiptDate;
	}

	@Override
	public int compareTo(final Receipt other) {
		final Date otherReceiptDate = other.getReceiptdate();
		return this.receiptdate.compareTo(otherReceiptDate) * -1;
	}

	public void fillNonDbFields() {
		this.setValue(this.getTotalValue());
		this.setFormatedReceiptDate(FORMAT.format(receiptdate));
	}

	private BigDecimal getTotalValue() {
		BigDecimal total = new BigDecimal(0);
		if (this.itemSet != null) {
			for (final Item item : this.itemSet) {
				total = total.add(item.getValue());
			}
		}
		return total;
	}
}
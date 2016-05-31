package co.com.soinsoftware.billing.entity;

import java.io.Serializable;

/**
 * @author Carlos Rodriguez
 * @since 26/05/2016
 * @version 1.0
 */
public class ItemId implements Serializable {

	private static final long serialVersionUID = -6259532121290575513L;

	private int idreceipt;

	private int iditemconcept;

	public ItemId() {
		super();
	}

	public ItemId(final int idreceipt, final int iditemconcept) {
		this.idreceipt = idreceipt;
		this.iditemconcept = iditemconcept;
	}

	public int getIdreceipt() {
		return this.idreceipt;
	}

	public void setIdreceipt(final int idreceipt) {
		this.idreceipt = idreceipt;
	}

	public int getIditemconcept() {
		return this.iditemconcept;
	}

	public void setIditemconcept(final int iditemconcept) {
		this.iditemconcept = iditemconcept;
	}

	public boolean equals(final Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ItemId))
			return false;
		ItemId castOther = (ItemId) other;

		return (this.getIdreceipt() == castOther.getIdreceipt())
				&& (this.getIditemconcept() == castOther.getIditemconcept());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getIdreceipt();
		result = 37 * result + this.getIditemconcept();
		return result;
	}
}
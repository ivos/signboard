package net.sf.pms.user.model;


import javax.persistence.Entity;
import java.io.Serializable;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Version;
import java.lang.Override;@Entity public class User implements java.io.Serializable {

	@Id
	private @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	Long id = null;
	@Version
	private @Column(name = "version")
	int version = 0;

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(final int version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object that) {
		if (this == that) {
			return true;
		}
		if (that == null) {
			return false;
		}
		if (getClass() != that.getClass()) {
			return false;
		}
		if (id != null) {
			return id.equals(((User) that).id);
		}
		return super.equals(that);
	}

	@Override
	public int hashCode() {
		if (id != null) {
			return id.hashCode();
		}
		return super.hashCode();
	}

	@Column
	private String email;

	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	@Column
	private String firstName;

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	@Column
	private String lastName;

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	@Column
	private String phone;

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(final String phone) {
		this.phone = phone;
	}

	@Column
	private String skype;

	public String getSkype() {
		return this.skype;
	}

	public void setSkype(final String skype) {
		this.skype = skype;
	}

	public String toString() {
		String result = "";
		if (email != null && !email.trim().isEmpty())
			result += email;
		if (firstName != null && !firstName.trim().isEmpty())
			result += " " + firstName;
		if (lastName != null && !lastName.trim().isEmpty())
			result += " " + lastName;
		if (phone != null && !phone.trim().isEmpty())
			result += " " + phone;
		if (skype != null && !skype.trim().isEmpty())
			result += " " + skype;
		return result;
	} }
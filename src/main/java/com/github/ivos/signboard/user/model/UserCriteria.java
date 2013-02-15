package com.github.ivos.signboard.user.model;

import java.util.Date;

import javax.validation.constraints.Past;

public class UserCriteria {

	private String email;

	private String firstName;

	private String lastName;

	private String phone;

	private UserStatus status;

	private UserSort sort = UserSort.alphabetically;

	@Past
	private Date registered__From;

	@Past
	private Date registered__To;

	@Past
	private Date lastLogin__From;

	@Past
	private Date lastLogin__To;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public UserSort getSort() {
		return sort;
	}

	public void setSort(UserSort sort) {
		this.sort = sort;
	}

	public Date getRegistered__From() {
		return registered__From;
	}

	public void setRegistered__From(Date registered__From) {
		this.registered__From = registered__From;
	}

	public Date getRegistered__To() {
		return registered__To;
	}

	public void setRegistered__To(Date registered__To) {
		this.registered__To = registered__To;
	}

	public Date getLastLogin__From() {
		return lastLogin__From;
	}

	public void setLastLogin__From(Date lastLogin__From) {
		this.lastLogin__From = lastLogin__From;
	}

	public Date getLastLogin__To() {
		return lastLogin__To;
	}

	public void setLastLogin__To(Date lastLogin__To) {
		this.lastLogin__To = lastLogin__To;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result
				+ ((lastLogin__From == null) ? 0 : lastLogin__From.hashCode());
		result = prime * result
				+ ((lastLogin__To == null) ? 0 : lastLogin__To.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime
				* result
				+ ((registered__From == null) ? 0 : registered__From.hashCode());
		result = prime * result
				+ ((registered__To == null) ? 0 : registered__To.hashCode());
		result = prime * result + ((sort == null) ? 0 : sort.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		UserCriteria other = (UserCriteria) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastLogin__From == null) {
			if (other.lastLogin__From != null)
				return false;
		} else if (!lastLogin__From.equals(other.lastLogin__From))
			return false;
		if (lastLogin__To == null) {
			if (other.lastLogin__To != null)
				return false;
		} else if (!lastLogin__To.equals(other.lastLogin__To))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (registered__From == null) {
			if (other.registered__From != null)
				return false;
		} else if (!registered__From.equals(other.registered__From))
			return false;
		if (registered__To == null) {
			if (other.registered__To != null)
				return false;
		} else if (!registered__To.equals(other.registered__To))
			return false;
		if (sort != other.sort)
			return false;
		if (status != other.status)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserCriteria [email=" + email + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", phone=" + phone + ", status="
				+ status + ", sort=" + sort + ", registered__From="
				+ registered__From + ", registered__To=" + registered__To
				+ ", lastLogin__From=" + lastLogin__From + ", lastLogin__To="
				+ lastLogin__To + "]";
	}

}

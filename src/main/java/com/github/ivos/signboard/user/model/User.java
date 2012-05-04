package com.github.ivos.signboard.user.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.codec.digest.DigestUtils;

@Entity
public class User implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Version
	private int version;

	@NotNull
	@Size(max = 100)
	@Column(unique = true, updatable = false)
	private String email;

	@NotNull
	@Size(max = 100)
	private String firstName;

	@NotNull
	@Size(max = 100)
	private String lastName;

	@Size(max = 30)
	private String phone;

	@Size(max = 100)
	private String skype;

	@NotNull
	@Size(min = 4, max = 100)
	private String password;

	/**
	 * Convert password to MD5 digest.
	 */
	public void digestPassword() {
		password = DigestUtils.md5Hex(password);
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(final int version) {
		this.version = version;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(final String phone) {
		this.phone = phone;
	}

	public String getSkype() {
		return skype;
	}

	public void setSkype(final String skype) {
		this.skype = skype;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
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
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", version=" + version + ", email=" + email
				+ ", firstName=" + firstName + ", lastName=" + lastName
				+ ", phone=" + phone + ", skype=" + skype + ", password="
				+ password + "]";
	}

	public String toLog() {
		return "User [id=" + id + ", version=" + version + ", email=" + email
				+ ", firstName=" + firstName + ", lastName=" + lastName
				+ ", phone=" + phone + ", skype=" + skype + "]";
	}

	private static final long serialVersionUID = 1L;

}

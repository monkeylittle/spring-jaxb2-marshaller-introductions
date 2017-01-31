package net.thoughtforge.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

public class Person implements Serializable {

	private static final long serialVersionUID = 8465162879793776395L;

	private Calendar dateOfBirth;
	
	private String firstName;
	
	private BigDecimal height;
	
	private String lastName;
	
	private BigDecimal weight;

	public Calendar getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Calendar dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public BigDecimal getHeight() {
		return height;
	}

	public void setHeight(BigDecimal height) {
		this.height = height;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
}

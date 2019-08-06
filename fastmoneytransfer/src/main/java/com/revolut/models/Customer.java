package com.revolut.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Customer {

	@JsonProperty(required = true)
    private long customerId ;


    @JsonProperty(required = true)
    private String customerName;

    
    @JsonProperty(required = true)
    private long contactNumber;

    @JsonProperty(required = true)
    private String emailAddress;


    public Customer() {}

    public Customer(String userName, String emailAddress) {
        this.customerName = userName;
        this.emailAddress = emailAddress;
    }

    public Customer(long userId, String userName, long contactNumber, String emailAddress) {
        this.customerId = userId;
        this.customerName = userName;
        this.contactNumber = contactNumber;
        this.emailAddress = emailAddress;
    }

    public long getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (contactNumber ^ (contactNumber >>> 32));
		result = prime * result + ((emailAddress == null) ? 0 : emailAddress.hashCode());
		result = prime * result + (int) (customerId ^ (customerId >>> 32));
		result = prime * result + ((customerName == null) ? 0 : customerName.hashCode());
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
		Customer other = (Customer) obj;
		if (contactNumber != other.contactNumber)
			return false;
		if (emailAddress == null) {
			if (other.emailAddress != null)
				return false;
		} else if (!emailAddress.equals(other.emailAddress))
			return false;
		if (customerId != other.customerId)
			return false;
		if (customerName == null) {
			if (other.customerName != null)
				return false;
		} else if (!customerName.equals(other.customerName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Customer [userId=" + customerId + ", customerName=" + customerName + ", contactNumber=" + contactNumber
				+ ", emailAddress=" + emailAddress + "]";
	}

	public long getContactNumber() {
		return contactNumber;
	}

  
}

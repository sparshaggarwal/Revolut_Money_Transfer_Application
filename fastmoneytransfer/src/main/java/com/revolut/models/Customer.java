package com.revolut.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Customer {

	@JsonProperty(required = true)
    private long userId ;


    @JsonProperty(required = true)
    private String userName;

    
    @JsonProperty(required = true)
    private long contactNumber;

    @JsonProperty(required = true)
    private String emailAddress;


    public Customer() {}

    public Customer(String userName, String emailAddress) {
        this.userName = userName;
        this.emailAddress = emailAddress;
    }

    public Customer(long userId, String userName, long contactNumber, String emailAddress) {
        this.userId = userId;
        this.userName = userName;
        this.contactNumber = contactNumber;
        this.emailAddress = emailAddress;
    }

    public long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
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
		result = prime * result + (int) (userId ^ (userId >>> 32));
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
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
		if (userId != other.userId)
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Customer [userId=" + userId + ", userName=" + userName + ", contactNumber=" + contactNumber
				+ ", emailAddress=" + emailAddress + "]";
	}

	public long getContactNumber() {
		return contactNumber;
	}

  
}

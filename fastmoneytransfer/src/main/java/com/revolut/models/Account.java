package com.revolut.models;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Account {

    @JsonProperty(required=true)
    private long accountId;

    @JsonProperty(required = true)
    private long userId;

    @JsonProperty(required = true)
    private BigDecimal balance;

    public Account() {
    }

    public Account(long accountId, BigDecimal balance) {
        this.accountId = accountId;
        this.balance = balance;
    }

    public Account(long accountId, long userId, BigDecimal balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
    }

    public long getAccountId() {
        return accountId;
    }

    public long getUserId() {
        return userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

	@Override
	public String toString() {
		return "Account [accountId=" + accountId + ", userId=" + userId + ", balance=" + balance + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (accountId ^ (accountId >>> 32));
		result = prime * result + ((balance == null) ? 0 : balance.hashCode());
		result = prime * result + (int) (userId ^ (userId >>> 32));
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
		Account other = (Account) obj;
		if (accountId != other.accountId)
			return false;
		if (balance == null) {
			if (other.balance != null)
				return false;
		} else if (!balance.equals(other.balance))
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}

	
}

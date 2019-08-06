package com.revolut.dao;

public interface DatabaseObject {
	
	public CustomerRepository getCustomerRespository();

	public AccountRepository getAccountRepository();

	public void populateTestData();
}

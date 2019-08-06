package com.revolut.dao;

public abstract class DataAccessObjectFactory {

	public abstract CustomerRepository getCustomerRespository();

	public abstract AccountRepository getAccountRepository();

	public abstract void populateTestData();

	public static DataAccessObjectFactory getDatabase(String factoryType) {

		switch (factoryType) {
		case "H2":
			return new H2Database();
		default:
			return new H2Database();
		}
	}
}

package com.revolut.dao;

public abstract class DatabaseObjectFactory {

	public static DatabaseObject getDatabase(String factoryType) {

		switch (factoryType) {
		case "H2":
			return new H2Database();
		default:
			return new H2Database();
		}
	}
}

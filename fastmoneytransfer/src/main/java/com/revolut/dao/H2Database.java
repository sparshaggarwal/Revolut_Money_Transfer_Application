package com.revolut.dao;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;
import org.h2.tools.RunScript;

import com.revolut.utility.ApplicationConstants;
import com.revolut.utility.Utility;

/**
 * H2 DAO
 */
public class H2Database extends DataAccessObjectFactory {
	private static final String h2_driver = Utility.getStringProperty("h2_driver");
	private static final String h2_connection_url = Utility.getStringProperty("h2_connection_url");
	private static final String h2_user = Utility.getStringProperty("h2_user");
	private static final String h2_password = Utility.getStringProperty("h2_password");
	private static Logger log = Logger.getLogger(H2Database.class);

	private final CustomerRepositoryImpl customerRepo = new CustomerRepositoryImpl();
	private final AccountRepositoryImpl accountRepo = new AccountRepositoryImpl();

	H2Database() {
		DbUtils.loadDriver(h2_driver);
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(h2_connection_url, h2_user, h2_password);

	}

	public CustomerRepository getCustomerRespository() {
		return customerRepo;
	}

	public AccountRepository getAccountRepository() {
		return accountRepo;
	}

	@Override
	public void populateTestData() {
		log.info("Populating Data ");
		Connection conn = null;
		try {
			conn = H2Database.getConnection();
			RunScript.execute(conn, new FileReader(ApplicationConstants.SQL_PATH));
		} catch (SQLException e) {
			log.error("Not able to populate Data", e);
			throw new RuntimeException(e);
		} catch (FileNotFoundException e) {
			log.error("SQL Script file not found", e);
			throw new RuntimeException(e);
		} finally {
			Utility.closeConnection(conn);
		}
	}

}

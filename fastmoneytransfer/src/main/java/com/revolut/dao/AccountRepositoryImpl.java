package com.revolut.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.revolut.exception.FastMoneyTransferApplicationException;
import com.revolut.models.Account;
import com.revolut.models.MoneyUtil;
import com.revolut.models.CustomerTransaction;
import com.revolut.utility.ApplicationConstants;
import com.revolut.utility.QueryConstants;
import com.revolut.utility.Utility;

/**
 * Implementation of Account Repository interface
 * 
 * @author Sparsh Agarwal
 */
public class AccountRepositoryImpl implements AccountRepository {

	private static Logger log = Logger.getLogger(AccountRepositoryImpl.class);

	private Connection connection = null;

	private PreparedStatement stmt = null;

	/**
	 * It will get data of all accounts in the account table.
	 */
	public List<Account> getAccountsData() throws FastMoneyTransferApplicationException {
		ResultSet rs = null;
		Account account = null;
		List<Account> accountList = new ArrayList<Account>();
		try {
			connection = H2Database.getConnection();
			stmt = connection.prepareStatement(QueryConstants.ALL_ACCOUNTS_QUERY);
			rs = stmt.executeQuery();
			while (rs.next()) {
				account = new Account(rs.getLong(ApplicationConstants.ACCOUNTID),
						rs.getLong(ApplicationConstants.USERID), rs.getBigDecimal(ApplicationConstants.BALANCE));
				log.debug("Account Details --> " + account);
				accountList.add(account);
			}
			return accountList;
		} catch (SQLException e) {
			throw new FastMoneyTransferApplicationException("Failed to read Account(s) Data", e);
		} finally {
			Utility.closeResources(rs, stmt, connection);
		}
	}

	/**
	 * Get Data of Account by AccountId.
	 */
	public Account findAccountById(long accountId) throws FastMoneyTransferApplicationException {
		ResultSet rs = null;
		Account acc = null;
		try {
			connection = H2Database.getConnection();
			stmt = connection.prepareStatement(QueryConstants.GET_ACCONT_QUERY);
			stmt.setLong(1, accountId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				acc = new Account(rs.getLong(ApplicationConstants.ACCOUNTID),
						rs.getLong(ApplicationConstants.USERID), rs.getBigDecimal(ApplicationConstants.BALANCE));
				log.debug("Account Details --> " + acc);
			}
			return acc;
		} catch (SQLException e) {
			throw new FastMoneyTransferApplicationException("Failed to read Account Data", e);
		} finally {
			Utility.closeResources(rs, stmt, connection);
		}

	}

	/**
	 * Inserts entry into the account table.
	 */
	public long openNewAccount(Account account) throws FastMoneyTransferApplicationException {
		ResultSet generatedKeys = null;
		try {
			connection = H2Database.getConnection();
			stmt = connection.prepareStatement(QueryConstants.CREATE_ACCOUNT_QUERY);
			stmt.setLong(1, account.getUserId());
			stmt.setBigDecimal(2, account.getBalance());
			int affectedRows = stmt.executeUpdate();
			if (affectedRows == 0) {
				log.error("Failed to Create Account");
				throw new FastMoneyTransferApplicationException("Failed to Create Account");
			}
			generatedKeys = stmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				return generatedKeys.getLong(1);
			} else {
				log.error("Account Creation Failed");
				throw new FastMoneyTransferApplicationException("Account Cannot be created");
			}
		} catch (SQLException e) {
			log.error("Error Inserting Account  " + account);
			throw new FastMoneyTransferApplicationException("Filead to create account " + account,
					e);
		} finally {
			Utility.closeResources(generatedKeys, stmt, connection);
		}
	}

	/**
	 * Delete account by id
	 */
	public int deleteAccountById(long accountId) throws FastMoneyTransferApplicationException {
		try {
			connection = H2Database.getConnection();
			stmt = connection.prepareStatement(QueryConstants.DELETE_ACCOUNT_QUERY);
			stmt.setLong(1, accountId);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			throw new FastMoneyTransferApplicationException(
					"Failed to delete account" + accountId, e);
		} finally {
			Utility.closeResources(stmt, connection);
		}
	}

	/**
	 * Update account balance
	 */
	public int updateAccountBalance(long accountId, BigDecimal deltaAmount)
			throws FastMoneyTransferApplicationException {
		PreparedStatement accountDetailStatement = null;
		PreparedStatement updateStmt = null;
		ResultSet rs = null;
		Account targetAccount = null;
		int updateCount = -1;
		try {
			connection = H2Database.getConnection();
			connection.setAutoCommit(false);
			accountDetailStatement = connection.prepareStatement(QueryConstants.GET_ACCONT_QUERY);
			accountDetailStatement.setLong(1, accountId);
			rs = accountDetailStatement.executeQuery();
			if (rs.next()) {
				targetAccount = new Account(rs.getLong("AccountId"), rs.getLong("UserId"),
						rs.getBigDecimal("Balance"));
				log.debug("updateAccountBalance from Account: " + targetAccount);
			}

			if (targetAccount == null) {
				throw new FastMoneyTransferApplicationException(
						"updateAccountBalance(): fail to lock account : " + accountId);
			}
			// update account upon success locking
			BigDecimal balance = targetAccount.getBalance().add(deltaAmount);
			if (balance.compareTo(MoneyUtil.zeroAmount) < 0) {
				throw new FastMoneyTransferApplicationException("Not sufficient Fund for account: " + accountId);
			}

			updateStmt = connection.prepareStatement(QueryConstants.UPDATE_ACCOUNT_BALANCE_QUERY);
			updateStmt.setBigDecimal(1, balance);
			updateStmt.setLong(2, accountId);
			updateCount = updateStmt.executeUpdate();
			connection.commit();
			if (log.isDebugEnabled())
				log.debug("New Balance after Update: " + targetAccount);
			return updateCount;
		} catch (SQLException se) {
			// rollback transaction if exception occurs
			log.error("updateAccountBalance(): User Transaction Failed, rollback initiated for: " + accountId, se);
			try {
				if (connection != null)
					connection.rollback();
			} catch (SQLException re) {
				throw new FastMoneyTransferApplicationException("Fail to rollback transaction", re);
			}
		} finally {
			Utility.closeResources(rs, accountDetailStatement, connection);
			Utility.closeStatement(updateStmt);
		}
		return updateCount;
	}

	/**
	 * Transfer balance between two accounts.
	 */
	public Boolean transferMoney(CustomerTransaction userTransaction) throws FastMoneyTransferApplicationException {
		int result = -1;
		Connection conn = null;
		PreparedStatement accountDetailStatement = null;
		PreparedStatement updateStmt = null;
		ResultSet rs = null;
		Account fromAccount = null;
		Account toAccount = null;
		int[] updatedRow = null;
		try {
			conn = H2Database.getConnection();
			conn.setAutoCommit(false);
			accountDetailStatement = conn.prepareStatement(QueryConstants.GET_ACCONT_QUERY);
			accountDetailStatement.setLong(1, userTransaction.getFromAccountId());
			rs = accountDetailStatement.executeQuery();
			if (rs.next()) {
				fromAccount = new Account(rs.getLong(ApplicationConstants.ACCOUNTID), rs.getLong(ApplicationConstants.USERID),
					rs.getBigDecimal(ApplicationConstants.BALANCE));
			log.debug("Balance would be decuted from Account " + fromAccount);
			}
			BigDecimal leftOverBalance = fromAccount.getBalance().subtract(userTransaction.getAmount());
			if (leftOverBalance.compareTo(MoneyUtil.zeroAmount) < 0) {
				throw new FastMoneyTransferApplicationException("Not enough Money");
			}
			accountDetailStatement.setLong(1, userTransaction.getToAccountId());
			rs = accountDetailStatement.executeQuery();
			if (rs.next()) {
				toAccount = new Account(rs.getLong("AccountId"), rs.getLong("UserId"), rs.getBigDecimal("Balance"));
				log.debug("transferAccountBalance to Account: " + toAccount);
			}
			if (fromAccount == null || toAccount == null) {
				throw new FastMoneyTransferApplicationException("Fail to lock both accounts for write");
			}
			updateStmt = conn.prepareStatement(QueryConstants.UPDATE_ACCOUNT_BALANCE_QUERY);
			updateStmt.setBigDecimal(1, leftOverBalance);
			updateStmt.setLong(2, userTransaction.getFromAccountId());
			updateStmt.addBatch();
			updateStmt.setBigDecimal(1, toAccount.getBalance().add(userTransaction.getAmount()));
			updateStmt.setLong(2, userTransaction.getToAccountId());
			updateStmt.addBatch();
			updatedRow = updateStmt.executeBatch();
			if( (updatedRow[0] + updatedRow[1])!=2){
				throw new FastMoneyTransferApplicationException("Not able to transfer money");
			}
			conn.commit();
		} catch (SQLException se) {
			log.error("Roll back" + userTransaction,
					se);
			try {
				if (conn != null)
					conn.rollback();
			} catch (SQLException re) {
				throw new FastMoneyTransferApplicationException("Fail to rollback transaction", re);
			}
		} finally {
			Utility.closeResources(rs, accountDetailStatement, conn);
			Utility.closeStatement(updateStmt);
		}
		return true;
	}

}

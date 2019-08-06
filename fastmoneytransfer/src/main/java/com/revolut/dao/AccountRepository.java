package com.revolut.dao;

import java.math.BigDecimal;
import java.util.List;

import com.revolut.exception.FastMoneyTransferApplicationException;
import com.revolut.models.Account;
import com.revolut.models.CustomerTransaction;

/**
 * The Account Repository interface which defines the methods/specifies the contract
 * to be followed by the implementation class.
 * @author Sparsh Agarwal
 */
public interface AccountRepository {

	/**
	 * It will get data of all accounts in the account table. 
	 * @return
	 * @throws FastMoneyTransferApplicationException
	 */
    List<Account> getAccountsData() throws FastMoneyTransferApplicationException;
    
    /**
     * Get Data of Account by Id.
     * @param accountId
     * @return
     * @throws FastMoneyTransferApplicationException
     */
    Account findAccountById(long accountId) throws FastMoneyTransferApplicationException;
    
    /**
     * It creates an account.
     * @param account
     * @return
     * @throws FastMoneyTransferApplicationException
     */
    long openNewAccount(Account account) throws FastMoneyTransferApplicationException;
    
    /**
     * Delete Account by Id. 
     * @param accountId
     * @return
     * @throws FastMoneyTransferApplicationException
     */
    int deleteAccountById(long accountId) throws FastMoneyTransferApplicationException;
    
    /**
     * Update balance of an account.
     * @param accountId
     * @param deltaAmount
     * @return
     * @throws FastMoneyTransferApplicationException
     */
    int updateAccountBalance(long accountId, BigDecimal deltaAmount) throws FastMoneyTransferApplicationException;
    
    /**
     * Transfer money from one account to another.
     * @param userTransaction
     * @return
     * @throws FastMoneyTransferApplicationException
     */
    Boolean transferMoney(CustomerTransaction userTransaction) throws FastMoneyTransferApplicationException;
}

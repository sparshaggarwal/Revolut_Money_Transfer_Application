package com.revolut.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.revolut.dao.DataAccessObjectFactory;
import com.revolut.exception.FastMoneyTransferApplicationException;
import com.revolut.models.Account;
import com.revolut.models.MoneyUtil;

/**
 * Account Service 
 */
@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
public class AccountService {
	
    private final DataAccessObjectFactory daoFactory = DataAccessObjectFactory.getDatabase("H2");
    
    private static Logger log = Logger.getLogger(AccountService.class);

    
    /**
     * Find all accounts
     * @return
     * @throws FastMoneyTransferApplicationException
     */
    @GET
    @Path("/all")
    public List<Account> getAllAccounts() throws FastMoneyTransferApplicationException {
        return daoFactory.getAccountRepository().getAccountsData();
    }

    /**
     * Find by account id
     * @param accountId
     * @return
     * @throws FastMoneyTransferApplicationException
     */
    @GET
    @Path("/{accountId}")
    public Account getAccount(@PathParam("accountId") long accountId) throws FastMoneyTransferApplicationException {
        return daoFactory.getAccountRepository().findAccountById(accountId);
    }
    
    /**
     * Find balance by account Id
     * @param accountId
     * @return
     * @throws FastMoneyTransferApplicationException
     */
    @GET
    @Path("/{accountId}/balance")
    public BigDecimal getBalance(@PathParam("accountId") long accountId) throws FastMoneyTransferApplicationException {
        final Account account = daoFactory.getAccountRepository().findAccountById(accountId);

        if(account == null){
            throw new WebApplicationException("Account not found", Response.Status.NOT_FOUND);
        }
        return account.getBalance();
    }
    
    /**
     * Create Account
     * @param account
     * @return
     * @throws FastMoneyTransferApplicationException
     */
    @PUT
    @Path("/create")
    public Account createAccount(Account account) throws FastMoneyTransferApplicationException {
        final long accountId = daoFactory.getAccountRepository().openNewAccount(account);
        return daoFactory.getAccountRepository().findAccountById(accountId);
    }

    /**
     * Deposit amount by account Id
     * @param accountId
     * @param amount
     * @return
     * @throws FastMoneyTransferApplicationException
     */
    @PUT
    @Path("/{accountId}/deposit/{amount}")
    public Account deposit(@PathParam("accountId") long accountId,@PathParam("amount") BigDecimal amount) throws FastMoneyTransferApplicationException {

        if (amount.compareTo(MoneyUtil.zeroAmount) <=0){
            throw new WebApplicationException("Invalid Deposit amount", Response.Status.BAD_REQUEST);
        }

        daoFactory.getAccountRepository().updateAccountBalance(accountId,amount.setScale(4, RoundingMode.HALF_EVEN));
        return daoFactory.getAccountRepository().findAccountById(accountId);
    }

    /**
     * Withdraw amount by account Id
     * @param accountId
     * @param amount
     * @return
     * @throws FastMoneyTransferApplicationException
     */
    @PUT
    @Path("/{accountId}/withdraw/{amount}")
    public Account withdraw(@PathParam("accountId") long accountId,@PathParam("amount") BigDecimal amount) throws FastMoneyTransferApplicationException {

        if (amount.compareTo(MoneyUtil.zeroAmount) <=0){
            throw new WebApplicationException("Invalid Deposit amount", Response.Status.BAD_REQUEST);
        }
        BigDecimal delta = amount.negate();
        if (log.isDebugEnabled())
            log.debug("Withdraw service: delta change to account  " + delta + " Account ID = " +accountId);
        daoFactory.getAccountRepository().updateAccountBalance(accountId,delta.setScale(4, RoundingMode.HALF_EVEN));
        return daoFactory.getAccountRepository().findAccountById(accountId);
    }


    /**
     * Delete amount by account Id
     * @param accountId
     * @param amount
     * @return
     * @throws FastMoneyTransferApplicationException
     */
    @DELETE
    @Path("/{accountId}")
    public Response deleteAccount(@PathParam("accountId") long accountId) throws FastMoneyTransferApplicationException {
        int deleteCount = daoFactory.getAccountRepository().deleteAccountById(accountId);
        if (deleteCount == 1) {
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}

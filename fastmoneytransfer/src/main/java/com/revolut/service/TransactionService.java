package com.revolut.service;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.revolut.dao.DataAccessObjectFactory;
import com.revolut.exception.FastMoneyTransferApplicationException;
import com.revolut.models.UserTransaction;

@Path("/transaction")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionService {

	private final DataAccessObjectFactory daoFactory = DataAccessObjectFactory
			.getDatabase("H2");

	/**
	 * Money transfer between two accounts.
	 * 
	 * @param transaction
	 * @return
	 * @throws FastMoneyTransferApplicationException
	 */
	@POST
	public Response transferFund(UserTransaction transaction) throws FastMoneyTransferApplicationException {
		
		if(transaction.getFromAccountId().compareTo(transaction.getToAccountId()) == 0){
			throw new WebApplicationException("Cannot transfer to same account", Response.Status.BAD_REQUEST);
		}
		if (daoFactory.getAccountRepository().transferMoney(transaction)) {
			return Response.status(Response.Status.OK).build();
		} else {
			throw new WebApplicationException("Transaction failed", Response.Status.BAD_REQUEST);
		}
	}

}

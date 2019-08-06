package com.revolut.dao;

import java.util.List;

import com.revolut.exception.FastMoneyTransferApplicationException;
import com.revolut.models.Customer;

/**
 * The Customer Repository interface which defines the methods/specifies the contract
 * to be followed by the implementation class.
 * @author Sparsh Agarwal
 */
public interface CustomerRepository {
	
	/**
	 * Get all customer data.
	 * @return
	 * @throws FastMoneyTransferApplicationException
	 */
	List<Customer> getCustomers() throws FastMoneyTransferApplicationException;

	/**
	 * Get customer By Id.
	 * @param customerId
	 * @return
	 * @throws FastMoneyTransferApplicationException
	 */
	Customer getCustomerById(long customerId) throws FastMoneyTransferApplicationException;

	/**
	 * Create customer. 
	 * @param customer
	 * @return
	 * @throws FastMoneyTransferApplicationException
	 */
	long insertCustomer(Customer customer) throws FastMoneyTransferApplicationException;

	/**
	 * Delete Customer.
	 * @param customerId
	 * @return
	 * @throws FastMoneyTransferApplicationException
	 */
	int deleteCustomer(long customerId) throws FastMoneyTransferApplicationException;

}

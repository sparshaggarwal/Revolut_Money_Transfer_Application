package com.revolut.dao.test;

import java.util.List;

import javax.validation.constraints.AssertTrue;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;

import com.revolut.dao.CustomerRepository;
import com.revolut.dao.DatabaseObject;
import com.revolut.dao.DatabaseObjectFactory;
import com.revolut.exception.FastMoneyTransferApplicationException;
import com.revolut.models.Customer;
import com.revolut.utility.Utility;

import static junit.framework.TestCase.assertTrue;

public class TestClas {

	
	/*private static DatabaseObject h2Object = null;
	
	private static CustomerRepository customerRepository = null;
	
	@BeforeClass
	public static void setup() {
		h2Object = DatabaseObjectFactory.getDatabase("H2");
		customerRepository = h2Object.getCustomerRespository();
		h2Object.populateTestData();
	}

	@After
	public void tearDown() {
	}
	
	@Test
	public void getAllCustomersTest() throws FastMoneyTransferApplicationException {
		List<Customer> allCustomers = customerRepository.getCustomers();
		assertTrue(allCustomers.size()==2);
	}

	@Test
	public void getCutomerByIdTest() throws FastMoneyTransferApplicationException {
		Customer customer = customerRepository.getCustomerById(1L);
		assertTrue(customer.getCustomerName().equals("sparsh"));
		assertTrue(customer.getEmailAddress().equals("Sparsh@yahoo.com"));
	}

	@Test
	public void customerNotExistTest() throws FastMoneyTransferApplicationException {
		Customer customer = customerRepository.getCustomerById(500L);
		if(customer==null){
			assertTrue(true);
		}
	}

	@Test
	public void insertCustomerTest() throws FastMoneyTransferApplicationException {
		Customer customer = new Customer("James", "james@gmail.com");
		customerRepository.insertCustomer(customer);
		assertTrue(customerRepository.getCustomers().size()==3);
	}

	@Test
	public void deleteCustomerTest() throws FastMoneyTransferApplicationException {
		customerRepository.deleteCustomer(1L);
		assertTrue(customerRepository.getCustomers().size()==2);
	}*/

}

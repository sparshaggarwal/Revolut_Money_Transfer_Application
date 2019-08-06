package com.revolut.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.revolut.exception.FastMoneyTransferApplicationException;
import com.revolut.models.Customer;
import com.revolut.utility.QueryConstants;
import com.revolut.utility.Utility;


public class CustomerRepositoryImpl implements CustomerRepository {
	
    private static Logger log = Logger.getLogger(CustomerRepositoryImpl.class);
    
    private Connection connection = null;
	
	private PreparedStatement stmt = null;
    /**
     * Find all users
     */
    public List<Customer> getCustomers() throws FastMoneyTransferApplicationException {
        ResultSet rs = null;
        List<Customer> customers = new ArrayList<Customer>();
        try {
        	connection = H2Database.getConnection();
            stmt = connection.prepareStatement(QueryConstants.SQL_GET_ALL_USERS);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Customer u = new Customer(rs.getLong("UserId"), rs.getString("UserName"),rs.getLong("ContactNumber"),rs.getString("EmailAddress"));
                customers.add(u);
                if (log.isDebugEnabled())
                    log.debug("getAllUsers() Retrieve User: " + u);
            }
            return customers;
        } catch (SQLException e) {
            throw new FastMoneyTransferApplicationException("Error reading user data", e);
        } finally {
        	Utility.closeResources(rs, stmt, connection);
        }
    }
    
    /**
     * Find user by userId
     */
    public Customer getCustomerById(long userId) throws FastMoneyTransferApplicationException {
        ResultSet rs = null;
        Customer customer = null;
        try {
        	connection = H2Database.getConnection();
            stmt = connection.prepareStatement(QueryConstants.SQL_GET_USER_BY_ID);
            stmt.setLong(1, userId);
            rs = stmt.executeQuery();
            if (rs.next()) {
            	customer = new Customer(rs.getLong("UserId"), rs.getString("UserName"),rs.getLong("ContactNumber"), rs.getString("EmailAddress"));
                if (log.isDebugEnabled())
                    log.debug("getUserById(): Retrieve User: " + customer);
            }
            return customer;
        } catch (SQLException e) {
            throw new FastMoneyTransferApplicationException("Error reading user data", e);
        } finally {
        	Utility.closeResources(rs, stmt, connection);
        }
    }
    
    /**
     * Save User
     */
    public long insertCustomer(Customer user) throws FastMoneyTransferApplicationException {
        ResultSet generatedKeys = null;
        try {
        	connection = H2Database.getConnection();
            stmt = connection.prepareStatement(QueryConstants.SQL_INSERT_USER, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getUserName());
            stmt.setLong(2, user.getContactNumber());
            stmt.setString(3, user.getEmailAddress());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                log.error("insertUser(): Creating user failed, no rows affected." + user);
                throw new FastMoneyTransferApplicationException("Users Cannot be created");
            }
            generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getLong(1);
            } else {
                log.error("insertUser():  Creating user failed, no ID obtained." + user);
                throw new FastMoneyTransferApplicationException("Users Cannot be created");
            }
        } catch (SQLException e) {
            log.error("Error Inserting User :" + user);
            throw new FastMoneyTransferApplicationException("Error creating user data", e);
        } finally {
        	Utility.closeResources(generatedKeys, stmt, connection);
        }

    }
    
    /**
     * Delete User
     */
    public int deleteCustomer(long userId) throws FastMoneyTransferApplicationException {
        try {
        	connection = H2Database.getConnection();
            stmt = connection.prepareStatement(QueryConstants.SQL_DELETE_USER_BY_ID);
            stmt.setLong(1, userId);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error Deleting User :" + userId);
            throw new FastMoneyTransferApplicationException("Error Deleting User ID:"+ userId, e);
        } finally {
        	Utility.closeResources(stmt, connection);
        }
    }

}

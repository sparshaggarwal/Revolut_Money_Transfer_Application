package com.revolut.service;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.revolut.dao.DatabaseObject;
import com.revolut.dao.DatabaseObjectFactory;
import com.revolut.exception.FastMoneyTransferApplicationException;
import com.revolut.models.Customer;


@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class CustomerService {
 
	private final DatabaseObject daoFactory = DatabaseObjectFactory.getDatabase("H2");
    
    /**
	 * Find by all
	 * @param userName
	 * @return
	 * @throws FastMoneyTransferApplicationException
	 */
    @GET
    @Path("/all")
    public List<Customer> getAllUsers() throws FastMoneyTransferApplicationException {
        return daoFactory.getCustomerRespository().getCustomers();
    }
    
    /**
     * Create User
     * @param user
     * @return
     * @throws FastMoneyTransferApplicationException
     */
    @POST
    @Path("/create")
    public Customer createUser(Customer user) throws FastMoneyTransferApplicationException {
        if (daoFactory.getCustomerRespository().getCustomerById(user.getCustomerId()) != null) {
            throw new WebApplicationException("User name already exist", Response.Status.BAD_REQUEST);
        }
        final long uId = daoFactory.getCustomerRespository().insertCustomer(user);
        return daoFactory.getCustomerRespository().getCustomerById(uId);
    }
    
    /**
     * Delete by User Id
     * @param userId
     * @return
     * @throws FastMoneyTransferApplicationException
     */
    @DELETE
    @Path("/{userId}")
    public Response deleteUser(@PathParam("userId") long customerId) throws FastMoneyTransferApplicationException {
        int deleteCount = daoFactory.getCustomerRespository().deleteCustomer(customerId);
        if (deleteCount == 1) {
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


}

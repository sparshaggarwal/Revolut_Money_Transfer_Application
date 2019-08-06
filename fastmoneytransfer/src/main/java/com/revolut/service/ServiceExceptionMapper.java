package com.revolut.service;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import com.revolut.exception.FastMoneyTransferApplicationException;
import com.revolut.exception.ErrorResponse;

@Provider
public class ServiceExceptionMapper implements ExceptionMapper<FastMoneyTransferApplicationException> {
	private static Logger log = Logger.getLogger(ServiceExceptionMapper.class);

	public Response toResponse(FastMoneyTransferApplicationException daoException) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorMessage(daoException.getMessage());
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).type(MediaType.APPLICATION_JSON).build();
	}

}

package com.revolut.exception;

public class FastMoneyTransferApplicationException extends Exception {

	private static final long serialVersionUID = 1L;

	public FastMoneyTransferApplicationException(String msg) {
		super(msg);
	}

	public FastMoneyTransferApplicationException(Throwable cause) {
		super(cause);
	}
	
	public FastMoneyTransferApplicationException(String msg, Throwable cause) {
		super(msg, cause);
	}
}

package com.revolut.utility;

public class QueryConstants {

	public final static String GET_ACCONT_QUERY = "SELECT * FROM Account WHERE AccountId = ? ";
	public final static String CREATE_ACCOUNT_QUERY = "INSERT INTO Account (UserId, Balance) VALUES (?, ?)";
	public final static String UPDATE_ACCOUNT_BALANCE_QUERY = "UPDATE Account SET Balance = ? WHERE AccountId = ? ";
	public final static String ALL_ACCOUNTS_QUERY = "SELECT * FROM Account";
	public final static String DELETE_ACCOUNT_QUERY = "DELETE FROM Account WHERE AccountId = ?";
	
	public final static String SQL_GET_USER_BY_ID = "SELECT * FROM User WHERE UserId = ? ";
    public final static String SQL_GET_ALL_USERS = "SELECT * FROM User";
    public final static String SQL_GET_USER_BY_NAME = "SELECT * FROM User WHERE UserName = ? ";
    public final static String SQL_INSERT_USER = "INSERT INTO User (UserName, ContactNumber,EmailAddress) VALUES (?, ?,?)";
    public final static String SQL_UPDATE_USER = "UPDATE User SET UserName = ?, EmailAddress = ? WHERE UserId = ? ";
    public final static String SQL_DELETE_USER_BY_ID = "DELETE FROM User WHERE UserId = ? ";
}

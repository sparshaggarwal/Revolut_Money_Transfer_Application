package com.revolut.utility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.h2.util.Utils;

public class Utility {

	private static Properties properties = new Properties();

    static Logger log = Logger.getLogger(Utils.class);

    public static void loadConfig(String fileName) {
        if (fileName == null) {
            log.warn("loadConfig: config file name cannot be null");
        } else {
            try {
                log.info("loadConfig(): Loading config file: " + fileName );
                final InputStream fis = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
                properties.load(fis);

            } catch (FileNotFoundException fne) {
                log.error("loadConfig(): file name not found " + fileName, fne);
            } catch (IOException ioe) {
                log.error("loadConfig(): error when reading the config " + fileName, ioe);
            }
        }

    }


    public static String getStringProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            value = System.getProperty(key);
        }
        return value;
    }

    /**
     * @param key:       property key
     * @param defaultVal the default value if the key not present in config file
     * @return string property based on lookup key
     */
    public static String getStringProperty(String key, String defaultVal) {
        String value = getStringProperty(key);
        return value != null ? value : defaultVal;
    }


    public static int getIntegerProperty(String key, int defaultVal) {
        String valueStr = getStringProperty(key);
        if (valueStr == null) {
            return defaultVal;
        } else {
            try {
                return Integer.parseInt(valueStr);

            } catch (Exception e) {
                log.warn("getIntegerProperty(): cannot parse integer from properties file for: " + key + "fail over to default value: " + defaultVal, e);
                return defaultVal;
            }
        }
    }

    //initialise

    static {
        String configFileName = System.getProperty("application.properties");

        if (configFileName == null) {
            configFileName = "application.properties";
        }
        loadConfig(configFileName);

    }

    /**
     * This method is responsible for closing Statement.
     * @param statement
     */
    public static void closeStatement(Statement statement){
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * This method is responsible for closing ResultSet.
     * @param statement
     */
	public static void closeResultSet(ResultSet rs){
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
     * This method is responsible for closing Connection.
     * @param statement
     */
	public static void closeConnection(Connection connection ){
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void closeResources(ResultSet rs, Statement stmt, Connection conn){
		Utility.closeResultSet(rs);
		Utility.closeStatement(stmt);
		Utility.closeConnection(conn);
	}
	
	public static void closeResources(Statement stmt, Connection conn){
		Utility.closeStatement(stmt);
		Utility.closeConnection(conn);
	}
}

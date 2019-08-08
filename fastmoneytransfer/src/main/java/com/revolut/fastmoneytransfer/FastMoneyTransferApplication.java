package com.revolut.fastmoneytransfer;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

import com.revolut.dao.DatabaseObject;
import com.revolut.dao.DatabaseObjectFactory;
import com.revolut.service.AccountService;
import com.revolut.service.CustomerService;
import com.revolut.service.ServiceExceptionMapper;
import com.revolut.service.TransactionService;

public class FastMoneyTransferApplication {
	
	private static Logger log = Logger.getLogger(FastMoneyTransferApplication.class);

	public static void main(String[] args) throws Exception {
		log.info("Creating tables with some sample data");
		DatabaseObject h2 = DatabaseObjectFactory.getDatabase("H2");
		h2.populateTestData();
		log.info("Initialisation Complete....");
		startService();
	}

	private static void startService() throws Exception {
		String port = System.getenv("PORT");
		if(port == null || port.isEmpty()){
			port="8081";
		}
		Server server = new Server(Integer.parseInt(port));
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/revolut");
		server.setHandler(context);
		ServletHolder servletHolder = context.addServlet(ServletContainer.class, "/*");
		servletHolder.setInitParameter("jersey.config.server.provider.classnames",
				CustomerService.class.getCanonicalName() + "," + AccountService.class.getCanonicalName() + ","
						+ ServiceExceptionMapper.class.getCanonicalName() + ","
						+ TransactionService.class.getCanonicalName());
		try {
			server.start();
			server.join();
		} finally {
			server.destroy();
		}
	}
}

package com.revolut.fastmoneytransfer;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

import com.revolut.dao.DataAccessObjectFactory;
import com.revolut.service.AccountService;
import com.revolut.service.ServiceExceptionMapper;
import com.revolut.service.TransactionService;
import com.revolut.service.CustomerService;

public class FastMoneyTransferApplication {
	
	private static Logger log = Logger.getLogger(FastMoneyTransferApplication.class);

	public static void main(String[] args) throws Exception {
		log.info("Creating tables with some sample data");
		DataAccessObjectFactory h2DaoFactory = DataAccessObjectFactory.getDatabase("H2");
		h2DaoFactory.populateTestData();
		log.info("Initialisation Complete....");
		// Host service on jetty
		startService();
	}

	private static void startService() throws Exception {
		Server server = new Server(8080);
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
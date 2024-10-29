package a01183994.database.util;

import java.awt.Component;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a01183994.database.dao.CustomerDao;

public class DatabaseShutdown {
	private static final Logger LOG = LogManager.getLogger();

	public static void shutdownDatabase(CustomerDao customerDao, Component parentComponent)
			throws ApplicationException {
		try {
			if (customerDao != null) {
				customerDao.saveChanges();
				customerDao.commitAndClose();
			}
		} catch (SQLException | IOException e) {
			LOG.error("Error during database shutdown: " + e.getMessage());
			JOptionPane.showMessageDialog(parentComponent, "Error saving changes to database: " + e.getMessage(),
					"Database Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}

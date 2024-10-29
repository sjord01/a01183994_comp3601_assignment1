package a01183994;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import a01183994.data.Customer;
import a01183994.database.Database;
import a01183994.database.dao.CustomerDao;
import a01183994.database.util.ApplicationException;
import a01183994.database.util.DbUtil;
import a01183994.ui.BooksAppFrame;

public class Assignment1 {

	private static final String LOG4J_CONFIG_FILENAME = "log4j2.xml";
	private static final String DB_PROPERTIES_FILENAME = "db.properties";
	private static final String DATA_TO_READ_TXT_FILE = "files//customers.dat";
	private static final String TXT_FILE_DELIMETER = "\\|";

	static {
		configureLogging();
	}

	private static final Logger LOG = LogManager.getLogger();
	private static Database database;
	private CustomerDao customerDao;
	private final Properties dbProperties;
	private Connection connection;

	public static void main(String[] args) {
		File dbPropertiesFile = new File(DB_PROPERTIES_FILENAME);
		if (!dbPropertiesFile.exists()) {
			showUsage();
			System.exit(-1);
		}

		Assignment1 app = null;
		try {
			app = new Assignment1(dbPropertiesFile);
			app.run();
		} catch (Exception e) {
			LOG.error("Error occurred: ", e);
		} finally {
			if (app != null) {
				app.closeDatabase();
			}
		}
	}

	private Assignment1(File dbPropertiesFile) throws IOException {
		dbProperties = new Properties();
		dbProperties.load(new FileReader(dbPropertiesFile));
		database = new Database(dbProperties);
	}

	private void run() throws SQLException, ClassNotFoundException, IOException, ApplicationException {
		connect();
		initializeDatabase();
		loadCustomerFrame();
	}

	private void connect() throws SQLException, ClassNotFoundException {
		database.connect();
		connection = database.getConnection();
		customerDao = new CustomerDao(database);
	}

	private void initializeDatabase() throws SQLException, IOException, ApplicationException {
		if (!DbUtil.tableExists(connection, CustomerDao.TABLE_NAME)) {
			createTable();
			insertCustomers();
		}
	}

	private void createTable() throws SQLException {
		customerDao.create();
	}

	private void insertCustomers() throws SQLException, IOException, ApplicationException {
		String fileName = DATA_TO_READ_TXT_FILE;
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			String line;
			reader.readLine(); // Skip the header line
			while ((line = reader.readLine()) != null) {
				String[] data = line.split(TXT_FILE_DELIMETER);
				Customer customer = new Customer.Builder(data[0], data[6]).setFirstName(data[1]).setLastName(data[2])
						.setStreetName(data[3]).setCityName(data[4]).setPostalCode(data[5]).setEmail(data[7])
						.setJoinDate(data[8]).build();
				customerDao.add(customer);
			}
		}
		LOG.info("Customers added successfully from file.");
	}

	private void loadCustomerFrame() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
						if ("Nimbus".equals(info.getName())) {
							UIManager.setLookAndFeel(info.getClassName());
							break;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				BooksAppFrame frame = new BooksAppFrame(database);
				frame.setVisible(true);
			}
		});
	}

	private void closeDatabase() {
		try {
			if (customerDao != null) {
				customerDao.commitAndClose();
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
				LOG.info("Database connection closed.");
			}
		} catch (SQLException | ApplicationException e) {
			LOG.error("Error closing database: ", e);
		}
	}

	private static void configureLogging() {
		ConfigurationSource source;
		try {
			source = new ConfigurationSource(new FileInputStream(LOG4J_CONFIG_FILENAME));
			Configurator.initialize(null, source);
		} catch (IOException e) {
			System.out.println(
					String.format("Can't find the log4j logging configuration file %s.", LOG4J_CONFIG_FILENAME));
		}
	}

	private static void showUsage() {
		System.err.println("The database properties file db.properties must be present.");
	}
}

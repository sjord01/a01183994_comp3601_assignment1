package a01183994;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
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

		try {
			database = new Database(new Properties());
			new Assignment1(dbPropertiesFile).run();
		} catch (SQLException | ClassNotFoundException | IOException e) {
			LOG.error("Error occurred: ", e);
		} finally {
//			if (database != null) {
//				database.shutdown();
//			}
			//loadCustomerFrame();
		}
	}

	public static void loadCustomerFrame() {
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

	private Assignment1(File dbPropertiesFile) throws IOException {
		dbProperties = new Properties();
		dbProperties.load(new FileReader(dbPropertiesFile));
		database = new Database(dbProperties);
	}

	private void run() throws SQLException, ClassNotFoundException, IOException {
		connect();

		try {
			if (DbUtil.tableExists(connection, CustomerDao.TABLE_NAME)) {
				dropTable();
			}
			createTable();
			insertCustomers();
			loadCustomerFrame();

		} finally {
			if (customerDao != null) {
//				try {
//					customerDao.cleanup();
//				} catch (SQLException e) {
//					LOG.error("Error during CustomerDao cleanup: " + e.getMessage());
//				}
			}

//			if (connection != null && !connection.isClosed()) {
//				connection.close();
//				LOG.info("Database connection closed.");
//			}

			LOG.info("Assignment1 execution completed.");
		}
	}

	private void connect() throws SQLException, ClassNotFoundException {
		database.connect(); // This will load the driver and establish the connection
		connection = database.getConnection();
		customerDao = new CustomerDao(database);
	}

	private void dropTable() throws SQLException {
		Statement statement = connection.createStatement();
		String sql = "DROP TABLE " + CustomerDao.TABLE_NAME;
		DbUtil.executeUpdate(statement, sql);
		DbUtil.close(statement);
	}

	private void createTable() throws SQLException {
		customerDao.create();
	}

	private void insertCustomers() throws SQLException, IOException {
	    String fileName = DATA_TO_READ_TXT_FILE;
	    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
	        String line;
	        reader.readLine(); // Skip the header line
	        while ((line = reader.readLine()) != null) {
	            String[] data = line.split(TXT_FILE_DELIMETER);
	            Customer customer = new Customer.Builder(data[0], data[6])
	                    .setFirstName(data[1])
	                    .setLastName(data[2])
	                    .setStreetName(data[3])
	                    .setCityName(data[4])
	                    .setPostalCode(data[5])
	                    .setEmail(data[7])
	                    .setJoinDate(data[8])
	                    .build();
	            customerDao.add(customer);
	        }
	    }
	    LOG.info("Customers added successfully from file.");
	}
}

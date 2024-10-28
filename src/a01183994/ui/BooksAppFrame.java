package a01183994.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import a01183994.database.Database;
import a01183994.database.dao.CustomerDao;
import a01183994.database.util.SortByListState;
import net.miginfocom.swing.MigLayout;

public class BooksAppFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private CustomerDao customerDao;

	/**
	 * Create the frame.
	 */
	public BooksAppFrame(Database database) {
		this.customerDao = new CustomerDao(database);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);
		setVisible(true);
		
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
					shutdownDatabase();
				} catch (SQLException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu menuFile = new JMenu("File");
		menuBar.add(menuFile);

		JMenuItem menuFileDrop = new JMenuItem("Drop");
		menuFileDrop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(BooksAppFrame.this,
		                "Are you sure you want to delete all Customer input data?", "Confirm Drop",
		                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		        if (result == JOptionPane.YES_OPTION) {
		            try {
		                customerDao.cleanup();
		                JOptionPane.showMessageDialog(BooksAppFrame.this, "All customer data has been deleted.",
		                        "Data Dropped", JOptionPane.INFORMATION_MESSAGE);
		                System.exit(0);  // Exit the application after dropping the table
		            } catch (SQLException ex) {
		                JOptionPane.showMessageDialog(BooksAppFrame.this,
		                        "Error dropping customer data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		            }
		        }
	            }
		});
		menuFile.add(menuFileDrop);

		JSeparator separatorFile_1 = new JSeparator();
		menuFile.add(separatorFile_1);

		JMenuItem menuFileQuit = new JMenuItem("Quit");
		menuFileQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					shutdownDatabase();
				} catch (SQLException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        dispose();
				System.exit(0);
			}
		});
		menuFileQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
		menuFile.add(menuFileQuit);

		JMenu menuCustomers = new JMenu("Count");
		menuBar.add(menuCustomers);

		JMenuItem menuCustomersCount = new JMenuItem("Count");
		menuCustomersCount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				  try {
			            int count = customerDao.getCustomerCount();
			            JOptionPane.showMessageDialog(BooksAppFrame.this,
			                "Total number of customers: " + count,
			                "Customer Count",
			                JOptionPane.INFORMATION_MESSAGE);
			        } catch (SQLException ex) {
			            JOptionPane.showMessageDialog(BooksAppFrame.this,
			                "Error getting customer count: " + ex.getMessage(),
			                "Error",
			                JOptionPane.ERROR_MESSAGE);
			        }
			}
		});
		menuCustomers.add(menuCustomersCount);

		JCheckBoxMenuItem chckbxmntmByJoinDate = new JCheckBoxMenuItem("By Join Date");
		chckbxmntmByJoinDate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SortByListState.getInstance().setSortByJoinDate(chckbxmntmByJoinDate.isSelected());
			}
		});
		menuCustomers.add(chckbxmntmByJoinDate);

		JSeparator separatorCustomers_1 = new JSeparator();
		menuCustomers.add(separatorCustomers_1);

		JMenuItem menuCustomersList = new JMenuItem("List");
		menuCustomersList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CustomerListDialog dialog = new CustomerListDialog(customerDao);
				dialog.setVisible(true);
			}
		});
		menuCustomers.add(menuCustomersList);

		JMenu menuHelp = new JMenu("Help");
		menuBar.add(menuHelp);

		JMenuItem menuHelpAbout = new JMenuItem("About");
		menuHelpAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(BooksAppFrame.this, "<html><h1>Customer Management App</h1>"
						+ "<p>Version: 1.0</p>"
						+ "<p>This application helps you manage a list of customers. You can add, view, edit, and delete customer details easily. "
						+ "Additionally, the app sorts customers by ID and provides detailed information when you select a customer.</p>"
						+ "<p>Developed by: Samson James Ordonez</p></html>", "About", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		menuHelpAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		menuHelp.add(menuHelpAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[][]", "[][]"));
	}

	private void shutdownDatabase() throws SQLException, IOException {
        customerDao.saveChanges();
 
    }
	

	@Override
	public void dispose() {
		try {
			shutdownDatabase();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.dispose();
	}
	
}

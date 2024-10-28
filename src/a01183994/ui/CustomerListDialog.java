package a01183994.ui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import a01183994.data.Customer;
import a01183994.database.dao.CustomerDao;
import a01183994.database.util.SortByListState;
import a01183994.ui.util.CustomerListUpdateListener;
import a01183994.io.CustomerReport;
import net.miginfocom.swing.MigLayout;

public class CustomerListDialog extends JDialog implements CustomerListUpdateListener {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private DefaultListModel<Customer> listModel;
    private JList<Customer> list;
	/**
	 * Create the dialog.
	 */
	public CustomerListDialog(CustomerDao customerDao) {
		setSize(800, 600);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new MigLayout("", "[grow]", "[grow][39px]"));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, "cell 0 0,grow");
		
		// Create and populate the customer list
		  listModel = new DefaultListModel<>();
		  list = new JList<>(listModel);

		// Fetch and sort the customer list
		try {
	        List<Customer> customers = customerDao.getAllCustomers();
	        sortCustomers(customers);
	        for (Customer customer : customers) {
	            listModel.addElement(customer);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(
	            this, 
	            "Error loading customers: " + e.getMessage() + "\n" + 
	            "SQL State: " + e.getSQLState() + "\n" + 
	            "Error Code: " + e.getErrorCode(),
	            "Database Error", 
	            JOptionPane.ERROR_MESSAGE
	        );
	    }

		contentPanel.setLayout(new MigLayout("", "[grow]", "[grow]"));
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			contentPanel.add(scrollPane, "cell 0 0,alignx center,aligny top, grow, hmin 300px");
			{
				list.setCellRenderer(new CustomerListCellRenderer());
				list.addListSelectionListener(new ListSelectionListener() {
			        public void valueChanged(ListSelectionEvent e) {
			            if (!e.getValueIsAdjusting()) {
			                Customer selectedCustomer = list.getSelectedValue();
			                if (selectedCustomer != null) {
			                    CustomerDetailsDialog dialog = new CustomerDetailsDialog(selectedCustomer, customerDao, CustomerListDialog.this);
			                    dialog.setVisible(true);
			                }
			            }
			        }
			    });
				list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				scrollPane.setViewportView(list);
			}
			{
				JTextArea textArea = new JTextArea(CustomerReport.getReportHeader());
				textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
				textArea.setEditable(false);
				scrollPane.setColumnHeaderView(textArea);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, "cell 0 1,growx,aligny top");
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		
		

	}

	private class CustomerListCellRenderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			if (value instanceof Customer) {
				Customer customer = (Customer) value;
				label.setText(CustomerReport.formatSingleCustomer(customer));
				label.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
			}
			return label;
		}
	}
	
	private void sortCustomers(List<Customer> customers) {
	    boolean sortByJoinDate = SortByListState.getInstance().isSortByJoinDate();
	    if (sortByJoinDate) {
	        customers.sort(Comparator.comparing(Customer::getJoinDate));
	    } else {
	        customers.sort(Comparator.comparing(Customer::getId));
	    }
	}

	@Override
	public void onCustomerUpdated(Customer updatedCustomer) {
        // Find the customer in the list model and update it
        for (int i = 0; i < listModel.getSize(); i++) {
            Customer customer = listModel.getElementAt(i);
            if (customer.getId().equals(updatedCustomer.getId())) {
                listModel.setElementAt(updatedCustomer, i);
                break;
            }
        }
        // Refresh the list
        list.repaint();
    }
}
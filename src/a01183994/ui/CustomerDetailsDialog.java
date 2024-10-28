package a01183994.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import a01183994.data.Customer;
import a01183994.database.dao.CustomerDao;
import a01183994.ui.util.CustomerListUpdateListener;
import net.miginfocom.swing.MigLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class CustomerDetailsDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField_id;
	private JTextField textField_firstName;
	private JTextField textField_lastName;
	private JTextField textField_street;
	private JTextField textField_city;
	private JTextField textField_postalCode;
	private JTextField textField_phone;
	private JTextField textField_email;
	private JTextField textField_joinDate;
	private Customer customer;
    private CustomerDao customerDao;
    private CustomerListUpdateListener updateListener;

	/**
	 * Create the dialog.
	 */
    public CustomerDetailsDialog(Customer customer, CustomerDao customerDao, CustomerListUpdateListener updateListener) {
        this.customer = customer;
        this.customerDao = customerDao;
        this.updateListener = updateListener;
		setSize(800, 600);
		setLocationRelativeTo(null);
	    setModalityType(ModalityType.APPLICATION_MODAL);
	    
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][grow]", "[][][][][][][][][][][]"));
		{
			JLabel lbl_id = new JLabel("ID");
			contentPanel.add(lbl_id, "cell 0 0,alignx trailing");
		}
		{
			textField_id = new JTextField(String.valueOf(customer.getId()));
			textField_id.setFont(new Font("Monospaced", Font.PLAIN, 12));
			textField_id.setEnabled(false);
			textField_id.setEditable(false);
			contentPanel.add(textField_id, "cell 1 0,growx");
			textField_id.setColumns(10);
		}
		{
			JLabel lbl_firstName = new JLabel("First Name");
			contentPanel.add(lbl_firstName, "cell 0 1,alignx trailing");
		}
		{
			textField_firstName = new JTextField(customer.getFirstName());
			textField_firstName.setFont(new Font("Monospaced", Font.PLAIN, 12));
			contentPanel.add(textField_firstName, "cell 1 1,growx");
			textField_firstName.setColumns(10);
		}
		{
			JLabel lbl_lastName = new JLabel("Last Name");
			contentPanel.add(lbl_lastName, "cell 0 2,alignx trailing");
		}
		{
			textField_lastName = new JTextField(customer.getLastName());
			textField_lastName.setFont(new Font("Monospaced", Font.PLAIN, 12));
			contentPanel.add(textField_lastName, "cell 1 2,growx");
			textField_lastName.setColumns(10);
		}
		{
			JLabel lbl_street = new JLabel("Street");
			contentPanel.add(lbl_street, "cell 0 3,alignx trailing");
		}
		{
			textField_street = new JTextField(customer.getStreetName());
			textField_street.setFont(new Font("Monospaced", Font.PLAIN, 12));
			contentPanel.add(textField_street, "cell 1 3,growx");
			textField_street.setColumns(10);
		}
		{
			JLabel lbl_City = new JLabel("City");
			contentPanel.add(lbl_City, "cell 0 4,alignx trailing");
		}
		{
			textField_city = new JTextField(customer.getCityName());
			textField_city.setFont(new Font("Monospaced", Font.PLAIN, 12));
			contentPanel.add(textField_city, "cell 1 4,growx");
			textField_city.setColumns(10);
		}
		{
			JLabel lbl_postalCode = new JLabel("Postal Code");
			contentPanel.add(lbl_postalCode, "cell 0 5,alignx trailing");
		}
		{
			textField_postalCode = new JTextField(customer.getPostalCode());
			textField_postalCode.setFont(new Font("Monospaced", Font.PLAIN, 12));
			contentPanel.add(textField_postalCode, "cell 1 5,growx");
			textField_postalCode.setColumns(10);
		}
		{
			JLabel lbl_phone = new JLabel("Phone");
			contentPanel.add(lbl_phone, "cell 0 6,alignx trailing");
		}
		{
			textField_phone = new JTextField(customer.getPhoneNumber());
			textField_phone.setFont(new Font("Monospaced", Font.PLAIN, 12));
			contentPanel.add(textField_phone, "cell 1 6,growx");
			textField_phone.setColumns(10);
		}
		{
			JLabel lbl_Email = new JLabel("Email");
			contentPanel.add(lbl_Email, "cell 0 7,alignx trailing");
		}
		{
			textField_email = new JTextField(customer.getEmail());
			textField_email.setFont(new Font("Monospaced", Font.PLAIN, 12));
			contentPanel.add(textField_email, "cell 1 7,growx");
			textField_email.setColumns(10);
		}
		{
			textField_joinDate = new JTextField(customer.getJoinDate().toString());
			textField_joinDate.setFont(new Font("Monospaced", Font.PLAIN, 12));
			contentPanel.add(textField_joinDate, "cell 1 8,growx");
			textField_joinDate.setColumns(10);
		}
		{
			JLabel lbl_joinDate = new JLabel("Join Date");
			contentPanel.add(lbl_joinDate, "cell 0 8,alignx trailing");
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						saveChanges();
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
    private void saveChanges() {
        Customer updatedCustomer = new Customer.Builder(customer.getId(), textField_phone.getText())
            .setFirstName(textField_firstName.getText())
            .setLastName(textField_lastName.getText())
            .setStreetName(textField_street.getText())
            .setCityName(textField_city.getText())
            .setPostalCode(textField_postalCode.getText())
            .setEmail(textField_email.getText())
            .setJoinDate(textField_joinDate.getText())
            .build();
        try {
            customerDao.update(updatedCustomer);
            if (updateListener != null) {
                updateListener.onCustomerUpdated(updatedCustomer);
            }
            JOptionPane.showMessageDialog(this, "Customer updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating customer: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


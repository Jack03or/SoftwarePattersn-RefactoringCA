import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class AdminAccountHelper {

	// Find an account in a list using the account number
	public CustomerAccount findAccountByNumber(ArrayList<CustomerAccount> accounts, String accountNumber)
	{
		String targetAccountNumber = "";
		if(accountNumber != null)
		{
			targetAccountNumber = accountNumber.trim();
		}
		for (CustomerAccount account : accounts)
		{
			if(account.getNumber() != null && account.getNumber().trim().equalsIgnoreCase(targetAccountNumber))
			{
				return account;
			}
		}
		return null;
	}

	// Find customers with matching surname
	public ArrayList<Customer> findCustomersBySurname(ArrayList<Customer> customerList, String surname)
	{
		ArrayList<Customer> matches = new ArrayList<Customer>();
		for (Customer currentCustomer : customerList)
		{
			if(currentCustomer.getSurname().equalsIgnoreCase(surname))
			{
				matches.add(currentCustomer);
			}
		}
		return matches;
	}

	// Find any account in the whole bank by account number
	public CustomerAccount findAnyAccountByNumber(ArrayList<Customer> customerList, String accountNumber)
	{
		for (Customer currentCustomer : customerList)
		{
			CustomerAccount account = findAccountByNumber(currentCustomer.getAccounts(), accountNumber);
			if(account != null)
			{
				return account;
			}
		}
		return null;
	}

	// Find the customer who owns an account number
	public Customer findCustomerByAccountNumber(ArrayList<Customer> customerList, String accountNumber)
	{
		for (Customer currentCustomer : customerList)
		{
			CustomerAccount account = findAccountByNumber(currentCustomer.getAccounts(), accountNumber);
			if(account != null)
			{
				return currentCustomer;
			}
		}
		return null;
	}

	// Show all accounts in a sortable and non-editable table
	public void showAllAccountsTable(JFrame sourceFrame, final ArrayList<Customer> customerList, final Runnable onReturn, final Runnable onWindowClose)
	{
		sourceFrame.dispose();
		final JFrame tableFrame = new JFrame("All Accounts");
		tableFrame.setSize(850, 500);
		tableFrame.setLocation(200, 200);
		tableFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				onWindowClose.run();
			}
		});
		tableFrame.setVisible(true);

		String[] columns = {"Account Number", "Account Type", "Customer ID", "Surname", "First Name", "Balance"};
		DefaultTableModel model = new DefaultTableModel(columns, 0) {
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};

		for (Customer currentCustomer : customerList)
		{
			for (CustomerAccount account : currentCustomer.getAccounts())
			{
				Object[] row = {
						account.getNumber(),
						account.getAccountType(),
						currentCustomer.getCustomerID(),
						currentCustomer.getSurname(),
						currentCustomer.getFirstName(),
						account.getBalance()
				};
				model.addRow(row);
			}
		}

		JTable table = new JTable(model);
		table.setAutoCreateRowSorter(true);
		JScrollPane scrollPane = new JScrollPane(table);

		JButton returnButton = new JButton("Return");
		returnButton.addActionListener(new ActionListener(  ) {
			public void actionPerformed(ActionEvent ae) {
				tableFrame.dispose();
				onReturn.run();
			}
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(returnButton);

		Container pageContent = tableFrame.getContentPane();
		pageContent.setLayout(new BorderLayout());
		pageContent.add(scrollPane, BorderLayout.CENTER);
		pageContent.add(buttonPanel, BorderLayout.SOUTH);
	}
}

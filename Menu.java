import java.awt.*;

import java.awt.event.*;
import java.util.ArrayList;
import java.io.IOException;
import java.io.File;

import javax.swing.*;

public class Menu extends JFrame{
	
	private ArrayList<Customer> customerList = new ArrayList<Customer>();
	private AdminAccountHelper adminAccountHelper = new AdminAccountHelper();
	private CustomerActionHelper customerActionHelper = new CustomerActionHelper();
	private DialogInputHelper dialogInputHelper = new DialogInputHelper();
	private BankFile bankFile = new BankFile();
	private String currentFilePath = null;
	private boolean hasUnsavedChanges = false;
    private int position = 0;
	private String password;
	private Customer customer = null;
	private CustomerAccount acc = new CustomerAccount();
	JFrame f, f1;
	 JLabel firstNameLabel, surnameLabel, pPPSLabel, dOBLabel;
	 JTextField firstNameTextField, surnameTextField, pPSTextField, dOBTextField;
		JLabel customerIDLabel, passwordLabel;
		JTextField customerIDTextField, passwordTextField;
	Container content;
		Customer e;


	 JPanel panel2;
		JButton add;
		String 	PPS,firstName,surname,DOB,CustomerID;
	
	public static void main(String[] args)
	{
		Menu driver = new Menu();
		driver.menuStart();
	}
	
	public void menuStart()
	{
		   /*The menuStart method asks the user if they are a new customer, an existing customer or an admin. It will then start the create customer process
		  if they are a new customer, or will ask them to log in if they are an existing customer or admin.*/
		
			
		
			
			f = new JFrame("User Type");
			f.setSize(400, 300);
			f.setLocation(200, 200);
			f.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent we) { exitApplicationWithSavePrompt(); }
			});

			JPanel userTypePanel = new JPanel();
			final ButtonGroup userType = new ButtonGroup();
			JRadioButton radioButton;
			userTypePanel.add(radioButton = new JRadioButton("Existing Customer"));
			radioButton.setActionCommand("Customer");
			userType.add(radioButton);
			
			userTypePanel.add(radioButton = new JRadioButton("Administrator"));
			radioButton.setActionCommand("Administrator");
			userType.add(radioButton);
			
			userTypePanel.add(radioButton = new JRadioButton("New Customer"));
			radioButton.setActionCommand("New Customer");
			userType.add(radioButton);

			JPanel continuePanel = new JPanel();
			JButton continueButton = new JButton("Continue");
			continuePanel.add(continueButton);

			Container content = f.getContentPane();
			content.setLayout(new GridLayout(2, 1));
			content.add(userTypePanel);
			content.add(continuePanel);



			continueButton.addActionListener(new ActionListener(  ) {
				public void actionPerformed(ActionEvent ae) {
					String user = userType.getSelection().getActionCommand(  );
					
					//if user selects NEW CUSTOMER--------------------------------------------------------------------------------------
					if(user.equals("New Customer"))
					{
						f.dispose();		
						f1 = new JFrame("Create New Customer");
						f1.setSize(400, 300);
						f1.setLocation(200, 200);
						f1.addWindowListener(new WindowAdapter() {
							public void windowClosing(WindowEvent we) { exitApplicationWithSavePrompt(); }
						});
							Container content = f1.getContentPane();
							content.setLayout(new BorderLayout());
							
							firstNameLabel = new JLabel("First Name:", SwingConstants.RIGHT);
							surnameLabel = new JLabel("Surname:", SwingConstants.RIGHT);
							pPPSLabel = new JLabel("PPS Number:", SwingConstants.RIGHT);
							dOBLabel = new JLabel("Date of birth", SwingConstants.RIGHT);
							firstNameTextField = new JTextField(20);
							surnameTextField = new JTextField(20);
							pPSTextField = new JTextField(20);
							dOBTextField = new JTextField(20);
							JPanel panel = new JPanel(new GridLayout(6, 2));
							panel.add(firstNameLabel);
							panel.add(firstNameTextField);
							panel.add(surnameLabel);
							panel.add(surnameTextField);
							panel.add(pPPSLabel);
							panel.add(pPSTextField);
							panel.add(dOBLabel);
							panel.add(dOBTextField);
								
							panel2 = new JPanel();
							add = new JButton("Add");
							
							 add.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									PPS = pPSTextField.getText();
									firstName = firstNameTextField.getText();
									surname = surnameTextField.getText();
									DOB = dOBTextField.getText();
									password = "";
									CustomerID = "ID" + PPS;

									boolean loop = true;
									while(loop){
										password = JOptionPane.showInputDialog(f, "Enter 7 character Password;");
										if(password.length() != 7)//Making sure password is 7 characters
										{
											JOptionPane.showMessageDialog(null, null, "Password must be 7 charatcers long", JOptionPane.OK_OPTION);
										}
										else
										{
											loop = false;
										}
									}

									ArrayList<CustomerAccount> accounts = new ArrayList<CustomerAccount> ();
									Customer customer = new Customer(PPS, surname, firstName, DOB, CustomerID, password, accounts);
									customerList.add(customer);
									markDataChanged();

									JOptionPane.showMessageDialog(f, "CustomerID = " + CustomerID +"\n Password = " + password  ,"Customer created.",  JOptionPane.INFORMATION_MESSAGE);
									f1.dispose();
									menuStart();
								}
							});						
							JButton cancel = new JButton("Cancel");					
							cancel.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									f1.dispose();
									menuStart();
								}
							});	
							
							panel2.add(add);
							panel2.add(cancel);
							
							content.add(panel, BorderLayout.CENTER);
							content.add(panel2, BorderLayout.SOUTH);
					
							f1.setVisible(true);		
						
					}
					
					
					//------------------------------------------------------------------------------------------------------------------
					
					//if user select ADMIN----------------------------------------------------------------------------------------------
					if(user.equals("Administrator")	)
					{
						boolean loop = true, loop2 = true;
						boolean cont = false;
					    while(loop)
					    {
					    Object adminUsername = JOptionPane.showInputDialog(f, "Enter Administrator Username:");

					    if(!adminUsername.equals("admin"))//search admin list for admin with matching admin username
					    {
					    	int reply  = JOptionPane.showConfirmDialog(null, null, "Incorrect Username. Try again?", JOptionPane.YES_NO_OPTION);
					    	if (reply == JOptionPane.YES_OPTION) {
					    		loop = true;
					    	}
					    	else if(reply == JOptionPane.NO_OPTION)
					    	{
					    		f.dispose();
					    		loop = false;
					    		loop2 = false;
					    		menuStart();
					    	}
					    }
					    else
					    {
					    	loop = false;
					    }				    
					    }
					    
					    while(loop2)
					    {
					    	Object adminPassword = JOptionPane.showInputDialog(f, "Enter Administrator Password;");
					    	
					    	   if(!adminPassword.equals("admin11"))//search admin list for admin with matching admin password
							    {
							    	int reply  = JOptionPane.showConfirmDialog(null, null, "Incorrect Password. Try again?", JOptionPane.YES_NO_OPTION);
							    	if (reply == JOptionPane.YES_OPTION) {
							    		
							    	}
							    	else if(reply == JOptionPane.NO_OPTION){
							    		f.dispose();
							    		loop2 = false;
							    		menuStart();
							    	}
							    }
					    	   else
					    	   {
					    		   loop2 =false;
					    		   cont = true;
					    	   }
					    }
					    	
					    if(cont)
					    {
						f.dispose();
					    	loop = false;
					    admin();					    
					    }					    
					}
					//----------------------------------------------------------------------------------------------------------------
					
					
					
					//if user selects CUSTOMER ---------------------------------------------------------------------------------------- 
					if(user.equals("Customer")	)
					{
						boolean loop = true, loop2 = true;
						boolean cont = false;
						boolean found = false;
						Customer customer = null;
					    while(loop)
					    {
					    Object customerID = JOptionPane.showInputDialog(f, "Enter Customer ID:");
					    // Reuse helper method for customer lookup by ID
					    customer = findCustomerById(String.valueOf(customerID));
					    if(customer != null)
					    {
					    	found = true;
					    }
					    
					    if(found == false)
					    {
					    	int reply  = JOptionPane.showConfirmDialog(null, null, "User not found. Try again?", JOptionPane.YES_NO_OPTION);
					    	if (reply == JOptionPane.YES_OPTION) {
					    		loop = true;
					    	}
					    	else if(reply == JOptionPane.NO_OPTION)
					    	{
					    		f.dispose();
					    		loop = false;
					    		loop2 = false;
					    		menuStart();
					    	}
					    }
					    else
					    {
					    	loop = false;
					    }
					   
					    }
					    
					    while(loop2)
					    {
					    	Object customerPassword = JOptionPane.showInputDialog(f, "Enter Customer Password;");
					    	
					    	   if(!customer.getPassword().equals(customerPassword))//check if custoemr password is correct
							    {
							    	int reply  = JOptionPane.showConfirmDialog(null, null, "Incorrect password. Try again?", JOptionPane.YES_NO_OPTION);
							    	if (reply == JOptionPane.YES_OPTION) {
							    		
							    	}
							    	else if(reply == JOptionPane.NO_OPTION){
							    		f.dispose();
							    		loop2 = false;
							    		menuStart();
							    	}
							    }
					    	   else
					    	   {
					    		   loop2 =false;
					    		   cont = true;
					    	   }
					    }
					    	
					    if(cont)
					    {
						f.dispose();
					    	loop = false;
					    	customer(customer);				    
					    }				    
					}
					//-----------------------------------------------------------------------------------------------------------------------
				}
			});f.setVisible(true);	
	}
	

	
	public void admin()
	{
		dispose();
		
		f = new JFrame("Administrator Menu");
		f.setSize(400, 400);
		f.setLocation(200, 200);
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				f.dispose();
				menuStart();
			}
		});          
		f.setVisible(true);
		
		JPanel deleteCustomerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton deleteCustomer = new JButton("Delete Customer");	
		deleteCustomer.setPreferredSize(new Dimension(250, 20));
		deleteCustomerPanel.add(deleteCustomer);
		
		JPanel deleteAccountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton deleteAccount = new JButton("Delete Account");
		deleteAccount.setPreferredSize(new Dimension(250, 20));	
		deleteAccountPanel.add(deleteAccount);
		
		JPanel bankChargesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton bankChargesButton = new JButton("Apply Bank Charges");
		bankChargesButton.setPreferredSize(new Dimension(250, 20));	
		bankChargesPanel.add(bankChargesButton);
		
		JPanel interestPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton interestButton = new JButton("Apply Interest");
		interestPanel.add(interestButton);
		interestButton.setPreferredSize(new Dimension(250, 20));

		JPanel setOverdraftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton setOverdraftButton = new JButton("Set Overdraft");
		setOverdraftPanel.add(setOverdraftButton);
		setOverdraftButton.setPreferredSize(new Dimension(250, 20));

		JPanel calculateInterestPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton calculateInterestButton = new JButton("Calculate Interest (All Deposit)");
		calculateInterestPanel.add(calculateInterestButton);
		calculateInterestButton.setPreferredSize(new Dimension(250, 20));
		
		JPanel editCustomerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton editCustomerButton = new JButton("Edit existing Customer");
		editCustomerPanel.add(editCustomerButton);
		editCustomerButton.setPreferredSize(new Dimension(250, 20));
		
		JPanel navigatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton navigateButton = new JButton("Navigate Customer Collection");
		navigatePanel.add(navigateButton);
		navigateButton.setPreferredSize(new Dimension(250, 20));
		
		JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton summaryButton = new JButton("Display Summary Of All Accounts");
		summaryPanel.add(summaryButton);
		summaryButton.setPreferredSize(new Dimension(250, 20));

		JPanel findAccountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton findAccountButton = new JButton("Find By Account Number");
		findAccountPanel.add(findAccountButton);
		findAccountButton.setPreferredSize(new Dimension(250, 20));

		JPanel findSurnamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton findSurnameButton = new JButton("Find By Surname");
		findSurnamePanel.add(findSurnameButton);
		findSurnameButton.setPreferredSize(new Dimension(250, 20));
		
		JPanel accountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton accountButton = new JButton("Add an Account to a Customer");
		accountPanel.add(accountButton);
		accountButton.setPreferredSize(new Dimension(250, 20));

		JPanel openPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton openButton = new JButton("Open File");
		openPanel.add(openButton);
		openButton.setPreferredSize(new Dimension(250, 20));

		JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton saveButton = new JButton("Save File");
		savePanel.add(saveButton);
		saveButton.setPreferredSize(new Dimension(250, 20));

		JPanel saveAsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton saveAsButton = new JButton("Save File As");
		saveAsPanel.add(saveAsButton);
		saveAsButton.setPreferredSize(new Dimension(250, 20));
		
		JPanel returnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton returnButton = new JButton("Exit Admin Menu");
		returnPanel.add(returnButton);

		JLabel label1 = new JLabel("Please select an option");
		
		content = f.getContentPane();
		content.setLayout(new GridLayout(16, 1));
		content.add(label1);
		content.add(openPanel);
		content.add(savePanel);
		content.add(saveAsPanel);
		content.add(accountPanel);
		content.add(bankChargesPanel);
		content.add(interestPanel);
		content.add(setOverdraftPanel);
		content.add(calculateInterestPanel);
		content.add(findAccountPanel);
		content.add(findSurnamePanel);
		content.add(editCustomerPanel);
		content.add(navigatePanel);
		content.add(summaryPanel);	
		content.add(deleteCustomerPanel);
	//	content.add(deleteAccountPanel);
		content.add(returnPanel);
		
		openButton.addActionListener(new ActionListener(  ) {
			public void actionPerformed(ActionEvent ae) {
				openDataFromFile();
			}
		});

		saveButton.addActionListener(new ActionListener(  ) {
			public void actionPerformed(ActionEvent ae) {
				saveData();
			}
		});

		saveAsButton.addActionListener(new ActionListener(  ) {
			public void actionPerformed(ActionEvent ae) {
				saveDataAs();
			}
		});

		setOverdraftButton.addActionListener(new ActionListener(  ) {
			public void actionPerformed(ActionEvent ae) {
				setOverdraftForCurrentAccount();
			}
		});

		calculateInterestButton.addActionListener(new ActionListener(  ) {
			public void actionPerformed(ActionEvent ae) {
				calculateInterestForAllDepositAccounts();
			}
		});
		
		
		bankChargesButton.addActionListener(new ActionListener(  ) {
			public void actionPerformed(ActionEvent ae) {
				// Use one shared check before running admin actions
				if(!hasCustomersForAdminAction())
				{
					return;
				}
				// Reuse helper method for customer lookup by ID
				customer = promptForCustomerByIdWithRetry("Customer ID of Customer You Wish to Apply Charges to:");
				if(customer == null)
				{
					return;
				}
				f.dispose();
				f = new JFrame("Administrator Menu");
				f.setSize(400, 300);
				f.setLocation(200, 200);
				f.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent we) { exitApplicationWithSavePrompt(); }
				});          
				f.setVisible(true);
				
				
			    JComboBox<String> box = new JComboBox<String>();
			    for (int i =0; i < customer.getAccounts().size(); i++)
			    {
			    	box.addItem(customer.getAccounts().get(i).getNumber());
			    }
					
				    
			    box.getSelectedItem();
				
			    JPanel boxPanel = new JPanel();
				boxPanel.add(box);
					
				JPanel buttonPanel = new JPanel();
				JButton continueButton = new JButton("Apply Charge");
				JButton returnButton = new JButton("Return");
				buttonPanel.add(continueButton);
				buttonPanel.add(returnButton);
				Container content = f.getContentPane();
				content.setLayout(new GridLayout(2, 1));
					
				content.add(boxPanel);
				content.add(buttonPanel);
					
			
				if(customer.getAccounts().isEmpty())
				{
					JOptionPane.showMessageDialog(f, "This customer has no accounts! \n The admin must add acounts to this customer."   ,"Oops!",  JOptionPane.INFORMATION_MESSAGE);
					f.dispose();
					admin();
				}
				else
				{
						
					// Reuse helper method for account lookup by account number
					acc = adminAccountHelper.findAccountByNumber(customer.getAccounts(), String.valueOf(box.getSelectedItem()));
										
					continueButton.addActionListener(new ActionListener(  ) {
						public void actionPerformed(ActionEvent ae) {
							String euro = "\u20ac";
						 	// Use account type behaviour for admin charge
							acc.applyAdminCharge();
							markDataChanged();
							JOptionPane.showMessageDialog(f, acc.getAdminChargeDisplayAmount() + euro + " account fee aplied."  ,"",  JOptionPane.INFORMATION_MESSAGE);
							JOptionPane.showMessageDialog(f, "New balance = " + acc.getBalance() ,"Success!",  JOptionPane.INFORMATION_MESSAGE);
							
							f.dispose();				
						admin();				
						}		
				     });
					
					returnButton.addActionListener(new ActionListener(  ) {
						public void actionPerformed(ActionEvent ae) {
							f.dispose();		
							menuStart();				
						}
				     });	
					
				}
			    
			}		
	     });
		
		interestButton.addActionListener(new ActionListener(  ) {
			public void actionPerformed(ActionEvent ae) {
			
				// Use one shared check before running admin actions
				if(!hasCustomersForAdminAction())
				{
					return;
				}
				// Reuse helper method for customer lookup by ID
				customer = promptForCustomerByIdWithRetry("Customer ID of Customer You Wish to Apply Interest to:");
				if(customer == null)
				{
					return;
				}
				f.dispose();
				f = new JFrame("Administrator Menu");
				f.setSize(400, 300);
				f.setLocation(200, 200);
				f.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent we) { exitApplicationWithSavePrompt(); }
				});          
				f.setVisible(true);
				
				
			    JComboBox<String> box = new JComboBox<String>();
			    for (int i =0; i < customer.getAccounts().size(); i++)
			    {
			    	box.addItem(customer.getAccounts().get(i).getNumber());
			    }
					
				    
			    box.getSelectedItem();
				
			    JPanel boxPanel = new JPanel();
					
				JLabel label = new JLabel("Select an account to apply interest to:");
				boxPanel.add(label);
				boxPanel.add(box);
				JPanel buttonPanel = new JPanel();
				JButton continueButton = new JButton("Apply Interest");
				JButton returnButton = new JButton("Return");
				buttonPanel.add(continueButton);
				buttonPanel.add(returnButton);
				Container content = f.getContentPane();
				content.setLayout(new GridLayout(2, 1));
					
				content.add(boxPanel);
				content.add(buttonPanel);
					
			
				if(customer.getAccounts().isEmpty())
				{
					JOptionPane.showMessageDialog(f, "This customer has no accounts! \n The admin must add acounts to this customer."   ,"Oops!",  JOptionPane.INFORMATION_MESSAGE);
					f.dispose();
					admin();
				}
				else
				{
						
					// Reuse helper method for account lookup by account number
					acc = adminAccountHelper.findAccountByNumber(customer.getAccounts(), String.valueOf(box.getSelectedItem()));
										
					continueButton.addActionListener(new ActionListener(  ) {
						public void actionPerformed(ActionEvent ae) {
							String euro = "\u20ac";
						 	// Reuse helper method for numeric input with retry
							Double interestValue = dialogInputHelper.promptForNumericInputWithRetry(f, "Enter interest percentage you wish to apply: \n NOTE: Please enter a numerical value. (with no percentage sign) \n E.g: If you wish to apply 8% interest, enter '8'");
							if(interestValue == null)
							{
								return;
							}
							double interest = interestValue.doubleValue();
							// Use account method for interest update
							acc.applyInterestPercentage(interest);
							markDataChanged();
							JOptionPane.showMessageDialog(f, interest + "% interest applied. \n new balance = " + acc.getBalance() + euro ,"Success!",  JOptionPane.INFORMATION_MESSAGE);
							
							f.dispose();				
						admin();				
						}		
				     });
					
					returnButton.addActionListener(new ActionListener(  ) {
						public void actionPerformed(ActionEvent ae) {
							f.dispose();		
							menuStart();				
						}
				     });	
					
				}
			    
			}	
	     });
		
		editCustomerButton.addActionListener(new ActionListener(  ) {
			public void actionPerformed(ActionEvent ae) {
				// Use one shared check before running admin actions
				if(!hasCustomersForAdminAction())
				{
					return;
				}
				// Reuse helper method for customer lookup by ID
				customer = promptForCustomerByIdWithRetry("Enter Customer ID:");
				if(customer == null)
				{
					return;
				}
				f.dispose();
				f = new JFrame("Administrator Menu");
				f.setSize(400, 300);
				f.setLocation(200, 200);
				f.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent we) { exitApplicationWithSavePrompt(); }
				});       
				
				firstNameLabel = new JLabel("First Name:", SwingConstants.LEFT);
				surnameLabel = new JLabel("Surname:", SwingConstants.LEFT);
				pPPSLabel = new JLabel("PPS Number:", SwingConstants.LEFT);
				dOBLabel = new JLabel("Date of birth", SwingConstants.LEFT);
				customerIDLabel = new JLabel("CustomerID:", SwingConstants.LEFT);
				passwordLabel = new JLabel("Password:", SwingConstants.LEFT);
				firstNameTextField = new JTextField(20);
				surnameTextField = new JTextField(20);
				pPSTextField = new JTextField(20);
				dOBTextField = new JTextField(20);
				customerIDTextField = new JTextField(20);
				passwordTextField = new JTextField(20);
				
				JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
				JPanel cancelPanel = new JPanel();
				
				textPanel.add(firstNameLabel);
				textPanel.add(firstNameTextField);
				textPanel.add(surnameLabel);
				textPanel.add(surnameTextField);
				textPanel.add(pPPSLabel);
				textPanel.add(pPSTextField);
				textPanel.add(dOBLabel);
				textPanel.add(dOBTextField);
				textPanel.add(customerIDLabel);
				textPanel.add(customerIDTextField);
				textPanel.add(passwordLabel);
				textPanel.add(passwordTextField);

				// Reuse helper method to fill customer details in text fields
				populateCustomerDetailsFields(customer);
				
				//JLabel label1 = new JLabel("Edit customer details below. The save");
				
			
				JButton saveButton = new JButton("Save");
				JButton cancelButton = new JButton("Exit");
				
				cancelPanel.add(cancelButton, BorderLayout.SOUTH);
				cancelPanel.add(saveButton, BorderLayout.SOUTH);
			//	content.removeAll();
				Container content = f.getContentPane();
				content.setLayout(new GridLayout(2, 1));
				content.add(textPanel, BorderLayout.NORTH);
				content.add(cancelPanel, BorderLayout.SOUTH);
				
				f.setContentPane(content);
				f.setSize(340, 350);
				f.setLocation(200, 200);
				f.setVisible(true);
				f.setResizable(false);
				
				saveButton.addActionListener(new ActionListener(  ) {
					public void actionPerformed(ActionEvent ae) {
					
						customer.setFirstName(firstNameTextField.getText());
						customer.setSurname(surnameTextField.getText());
						customer.setPPS(pPSTextField.getText());
						customer.setDOB(dOBTextField.getText());
						customer.setCustomerID(customerIDTextField.getText());
						customer.setPassword(passwordTextField.getText());		
						markDataChanged();
						
						JOptionPane.showMessageDialog(null, "Changes Saved.");
							}		
					     });
				
					cancelButton.addActionListener(new ActionListener(  ) {
						public void actionPerformed(ActionEvent ae) {
							f.dispose();
							
							admin();				
						}		
				     });		
					}
			     });
		
		summaryButton.addActionListener(new ActionListener(  ) {
			public void actionPerformed(ActionEvent ae) {
				// Show all accounts in a sortable non-editable table
				showAllAccountsTable();
			}	
	     });

		findAccountButton.addActionListener(new ActionListener(  ) {
			public void actionPerformed(ActionEvent ae) {
				String accountNumber = JOptionPane.showInputDialog(f, "Enter Account Number:");
				if(accountNumber == null || accountNumber.trim().length() == 0)
				{
					return;
				}
				Customer owner = adminAccountHelper.findCustomerByAccountNumber(customerList, accountNumber.trim());
				CustomerAccount account = adminAccountHelper.findAnyAccountByNumber(customerList, accountNumber.trim());
				if(account == null || owner == null)
				{
					JOptionPane.showMessageDialog(f, "Account not found." ,"Find Account",  JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				String accountType = account.getAccountType();
				JOptionPane.showMessageDialog(f,
						"Account Number = " + account.getNumber()
						+ "\nType = " + accountType
						+ "\nBalance = " + account.getBalance()
						+ "\nCustomer ID = " + owner.getCustomerID()
						+ "\nSurname = " + owner.getSurname()
						+ "\nFirst Name = " + owner.getFirstName(),
						"Find Account", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		findSurnameButton.addActionListener(new ActionListener(  ) {
			public void actionPerformed(ActionEvent ae) {
				String surname = JOptionPane.showInputDialog(f, "Enter Surname:");
				if(surname == null || surname.trim().length() == 0)
				{
					return;
				}
				ArrayList<Customer> matches = adminAccountHelper.findCustomersBySurname(customerList, surname.trim());
				if(matches.isEmpty())
				{
					JOptionPane.showMessageDialog(f, "No customers found for that surname." ,"Find Surname",  JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				String message = "";
				for(Customer matchedCustomer : matches)
				{
					message = message + "Customer ID = " + matchedCustomer.getCustomerID()
						+ ", Name = " + matchedCustomer.getFirstName() + " " + matchedCustomer.getSurname()
						+ ", Accounts = " + matchedCustomer.getAccounts().size() + "\n";
				}
				JOptionPane.showMessageDialog(f, message, "Find Surname", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		navigateButton.addActionListener(new ActionListener(  ) {
			public void actionPerformed(ActionEvent ae) {
				f.dispose();
				
				if(customerList.isEmpty())
				{
					JOptionPane.showMessageDialog(null, "There are currently no customers to display. ");
					admin();
				}
				else
				{
		
				JButton first, previous, next, last, cancel;
				JPanel gridPanel, buttonPanel, cancelPanel;			
	
				Container content = getContentPane();
				
				content.setLayout(new BorderLayout());
				
				buttonPanel = new JPanel();
				gridPanel = new JPanel(new GridLayout(8, 2));
				cancelPanel = new JPanel();
								
				firstNameLabel = new JLabel("First Name:", SwingConstants.LEFT);
				surnameLabel = new JLabel("Surname:", SwingConstants.LEFT);
				pPPSLabel = new JLabel("PPS Number:", SwingConstants.LEFT);
				dOBLabel = new JLabel("Date of birth", SwingConstants.LEFT);
				customerIDLabel = new JLabel("CustomerID:", SwingConstants.LEFT);
				passwordLabel = new JLabel("Password:", SwingConstants.LEFT);
				firstNameTextField = new JTextField(20);
				surnameTextField = new JTextField(20);
				pPSTextField = new JTextField(20);
				dOBTextField = new JTextField(20);
				customerIDTextField = new JTextField(20);
				passwordTextField = new JTextField(20);
				
				first = new JButton("First");
				previous = new JButton("Previous");
				next = new JButton("Next");
				last = new JButton("Last");
				cancel = new JButton("Cancel");
				
				// Reuse helper method to fill customer details in text fields
				populateCustomerDetailsFields(customerList.get(0));
				
				firstNameTextField.setEditable(false);
				surnameTextField.setEditable(false);
				pPSTextField.setEditable(false);
				dOBTextField.setEditable(false);
				customerIDTextField.setEditable(false);
				passwordTextField.setEditable(false);
				
				gridPanel.add(firstNameLabel);
				gridPanel.add(firstNameTextField);
				gridPanel.add(surnameLabel);
				gridPanel.add(surnameTextField);
				gridPanel.add(pPPSLabel);
				gridPanel.add(pPSTextField);
				gridPanel.add(dOBLabel);
				gridPanel.add(dOBTextField);
				gridPanel.add(customerIDLabel);
				gridPanel.add(customerIDTextField);
				gridPanel.add(passwordLabel);
				gridPanel.add(passwordTextField);
				
				buttonPanel.add(first);
				buttonPanel.add(previous);
				buttonPanel.add(next);
				buttonPanel.add(last);
				
				cancelPanel.add(cancel);
		
				content.add(gridPanel, BorderLayout.NORTH);
				content.add(buttonPanel, BorderLayout.CENTER);
				content.add(cancelPanel, BorderLayout.AFTER_LAST_LINE);
				first.addActionListener(new ActionListener(  ) {
					public void actionPerformed(ActionEvent ae) {
						position = 0;
						// Reuse helper method to fill customer details in text fields
						populateCustomerDetailsFields(customerList.get(0));			
							}		
					     });
				
				previous.addActionListener(new ActionListener(  ) {
					public void actionPerformed(ActionEvent ae) {
								
						if(position < 1)
						{
							//don't do anything
						}
						else
						{
							position = position - 1;
							
						// Reuse helper method to fill customer details in text fields
						populateCustomerDetailsFields(customerList.get(position));
						}			
							}		
					     });
				
				next.addActionListener(new ActionListener(  ) {
					public void actionPerformed(ActionEvent ae) {
					
						if(position == customerList.size()-1)
						{
							//don't do anything
						}
						else
						{
							position = position + 1;
							
						// Reuse helper method to fill customer details in text fields
						populateCustomerDetailsFields(customerList.get(position));
						}		
						
						
												
							}		
					     });
				
				last.addActionListener(new ActionListener(  ) {
					public void actionPerformed(ActionEvent ae) {
					
						position = customerList.size() - 1;
				
						// Reuse helper method to fill customer details in text fields
						populateCustomerDetailsFields(customerList.get(position));							
							}		
					     });
				
				cancel.addActionListener(new ActionListener(  ) {
					public void actionPerformed(ActionEvent ae) {				
						dispose();
						admin();
							}		
					     });			
				setContentPane(content);
				setSize(400, 300);
			       setVisible(true);
					}		
			}
		});
		
		accountButton.addActionListener(new ActionListener(  ) {
			public void actionPerformed(ActionEvent ae) {
				f.dispose();
				
				// Use one shared check before running admin actions
				if(!hasCustomersForAdminAction())
				{
					return;
				}
				else
				{
					// Reuse helper method for customer lookup by ID
					customer = promptForCustomerByIdWithRetry("Customer ID of Customer You Wish to Add an Account to:");
					if(customer == null)
					{
						return;
					}
			    	//a combo box in an dialog box that asks the admin what type of account they wish to create (deposit/current)
				    String[] choices = { "Current Account", "Deposit Account" };
				    String account = (String) JOptionPane.showInputDialog(null, "Please choose account type",
				        "Account Type", JOptionPane.QUESTION_MESSAGE, null, choices, choices[1]); 
				    
				    if(account.equals("Current Account"))
				    {
				    	//create current account
				    	boolean valid = true;
				    	double balance = 0;
				    	String number = String.valueOf("C" + (customerList.indexOf(customer)+1) * 10 + (customer.getAccounts().size()+1));//this simple algorithm generates the account number
				    	ArrayList<AccountTransaction> transactionList = new ArrayList<AccountTransaction>();
				    	int randomPIN = (int)(Math.random()*9000)+1000;
				        String pin = String.valueOf(randomPIN);
				    
				        ATMCard atm = new ATMCard(randomPIN, valid);
				    	
				    	CustomerCurrentAccount current = new CustomerCurrentAccount(atm, number, balance, transactionList);
				    	
				    	customer.getAccounts().add(current);
				    	markDataChanged();
				    	JOptionPane.showMessageDialog(f, "Account number = " + number +"\n PIN = " + pin  ,"Account created.",  JOptionPane.INFORMATION_MESSAGE);
				    	
				    	f.dispose();
				    	admin();
				    }
				    
				    if(account.equals("Deposit Account"))
				    {
				    	//create deposit account
				    	
				    	double balance = 0, interest = 0;
				    	String number = String.valueOf("D" + (customerList.indexOf(customer)+1) * 10 + (customer.getAccounts().size()+1));//this simple algorithm generates the account number
				    	ArrayList<AccountTransaction> transactionList = new ArrayList<AccountTransaction>();
				        	
				    	CustomerDepositAccount deposit = new CustomerDepositAccount(interest, number, balance, transactionList);
				    	
				    	customer.getAccounts().add(deposit);
				    	markDataChanged();
				    	JOptionPane.showMessageDialog(f, "Account number = " + number ,"Account created.",  JOptionPane.INFORMATION_MESSAGE);
				    	
				    	f.dispose();
				    	admin();
				    }
				}
			}
	     });		

		deleteCustomer.addActionListener(new ActionListener(  ) {
			public void actionPerformed(ActionEvent ae) {
				boolean found = true, loop = true;
				
				if(customerList.isEmpty())
				{
					JOptionPane.showMessageDialog(null, "There are currently no customers to display. ");
					dispose();
					admin();
				}
				else
				{
					 {
						    Object customerID = JOptionPane.showInputDialog(f, "Customer ID of Customer You Wish to Delete:");
						    // Reuse helper method for customer lookup by ID
						    customer = findCustomerById(String.valueOf(customerID));
						    if(customer != null)
						    {
						    	found = true;
						    	loop = false;
						    }
						    else
						    {
						    	found = false;
						    }
						    
						    if(found == false)
						    {
						    	int reply  = JOptionPane.showConfirmDialog(null, null, "User not found. Try again?", JOptionPane.YES_NO_OPTION);
						    	if (reply == JOptionPane.YES_OPTION) {
						    		loop = true;
						    	}
						    	else if(reply == JOptionPane.NO_OPTION)
						    	{
						    		f.dispose();
						    		loop = false;
						    		
						    		admin();
						    	}
						    }  
						    else
						    {
						    	if(customer.getAccounts().size()>0)
						    	{
						    		JOptionPane.showMessageDialog(f, "This customer has accounts. \n You must delete a customer's accounts before deleting a customer " ,"Oops!",  JOptionPane.INFORMATION_MESSAGE);
						    	}
						    	else
						    	{
						    		customerList.remove(customer);
						    		markDataChanged();
						    		JOptionPane.showMessageDialog(f, "Customer Deleted " ,"Success.",  JOptionPane.INFORMATION_MESSAGE);
						    	}
						    }
						    
						    
				}}
			}
	     });		
		
		deleteAccount.addActionListener(new ActionListener(  ) {
			public void actionPerformed(ActionEvent ae) {
	boolean found = true, loop = true;
				
				
				
				
					 {
						    Object customerID = JOptionPane.showInputDialog(f, "Customer ID of Customer from which you wish to delete an account");
						    // Reuse helper method for customer lookup by ID
						    customer = findCustomerById(String.valueOf(customerID));
						    if(customer != null)
						    {
						    	found = true;
						    	loop = false;
						    }
						    else
						    {
						    	found = false;
						    }
						    
						    if(found == false)
						    {
						    	int reply  = JOptionPane.showConfirmDialog(null, null, "User not found. Try again?", JOptionPane.YES_NO_OPTION);
						    	if (reply == JOptionPane.YES_OPTION) {
						    		loop = true;
						    	}
						    	else if(reply == JOptionPane.NO_OPTION)
						    	{
						    		f.dispose();
						    		loop = false;
						    	
						    		admin();
						    	}
						    }  
						    else
						    {
						    	//Here I would make the user select a an account to delete from a combo box. If the account had a balance of 0 then it would be deleted. (I do not have time to do this)
						    }
						    
						    
				}}
			
	     });		
		returnButton.addActionListener(new ActionListener(  ) {
			public void actionPerformed(ActionEvent ae) {
				// Exit admin menu and return to start menu
				f.dispose();
				menuStart();				
			}
	     });		
	}
	
	public void customer(Customer e1)
	{	
		f = new JFrame("Customer Menu");
		e = e1;
		f.setSize(400, 300);
		f.setLocation(200, 200);
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				f.dispose();
				menuStart();
			}
		});          
		f.setVisible(true);
		
		if(e.getAccounts().size() == 0)
		{
			JOptionPane.showMessageDialog(f, "This customer does not have any accounts yet. \n An admin must create an account for this customer \n for them to be able to use customer functionality. " ,"Oops!",  JOptionPane.INFORMATION_MESSAGE);
			f.dispose();				
			menuStart();
		}
		else
		{
		JPanel buttonPanel = new JPanel();
		JPanel boxPanel = new JPanel();
		JPanel labelPanel = new JPanel();
		
		JLabel label = new JLabel("Select Account:");
		labelPanel.add(label);
		
		JButton returnButton = new JButton("Return");
		buttonPanel.add(returnButton);
		JButton continueButton = new JButton("Continue");
		buttonPanel.add(continueButton);
		
		JComboBox<String> box = new JComboBox<String>();
	    for (int i =0; i < e.getAccounts().size(); i++)
	    {
	     box.addItem(e.getAccounts().get(i).getNumber());
	    }
		
	    
	   
	    // Reuse helper method for account lookup by account number
	    acc = adminAccountHelper.findAccountByNumber(e.getAccounts(), String.valueOf(box.getSelectedItem()));
	    
	    
	    
	
	    
		boxPanel.add(box);
		content = f.getContentPane();
		content.setLayout(new GridLayout(3, 1));
		content.add(labelPanel);
		content.add(boxPanel);
		content.add(buttonPanel);
		
		returnButton.addActionListener(new ActionListener(  ) {
			public void actionPerformed(ActionEvent ae) {
			f.dispose();				
			menuStart();				
			}		
	     });
		
		continueButton.addActionListener(new ActionListener(  ) {
			public void actionPerformed(ActionEvent ae) {
				
		f.dispose();
		
		f = new JFrame("Customer Menu");
		f.setSize(400, 300);
		f.setLocation(200, 200);
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				f.dispose();
				menuStart();
			}
		});          
		f.setVisible(true);
		
		JPanel statementPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton statementButton = new JButton("Display Bank Statement");
		statementButton.setPreferredSize(new Dimension(250, 20));
		
		statementPanel.add(statementButton);
		
		JPanel lodgementPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton lodgementButton = new JButton("Lodge money into account");
		lodgementPanel.add(lodgementButton);
		lodgementButton.setPreferredSize(new Dimension(250, 20));
		
		JPanel withdrawalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton withdrawButton = new JButton("Withdraw money from account");
		withdrawalPanel.add(withdrawButton);
		withdrawButton.setPreferredSize(new Dimension(250, 20));
		
		JPanel returnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton returnButton = new JButton("Exit Customer Menu");
		returnPanel.add(returnButton);

		JLabel label1 = new JLabel("Please select an option");
		
		content = f.getContentPane();
		content.setLayout(new GridLayout(5, 1));
		content.add(label1);
		content.add(statementPanel);
		content.add(lodgementPanel);
		content.add(withdrawalPanel);
		content.add(returnPanel);
		
		statementButton.addActionListener(new ActionListener(  ) {
			public void actionPerformed(ActionEvent ae) {
				// Use extracted helper class for customer statement screen
				customerActionHelper.showAccountStatement(f, acc,
						new Runnable() {
							public void run() {
								customer(e);
							}
						},
						new Runnable() {
							public void run() {
								exitApplicationWithSavePrompt();
							}
						});
			}	
	     });
		
		lodgementButton.addActionListener(new ActionListener(  ) {
			public void actionPerformed(ActionEvent ae) {
			boolean on = true;
			double balance = 0;

			if(acc.requiresPinVerification())
			{
				// Reuse helper method for current account PIN checks
				on = verifyCurrentAccountPin(e);
			}		if(on == true)
					{
				// Reuse helper method for numeric input
				Double balanceValue = dialogInputHelper.promptForNumericInputOnce(f, "Enter amount you wish to lodge:");
				if(balanceValue == null)
				{
					return;
				}
				balance = balanceValue.doubleValue();
				
			
			String euro = "\u20ac";
			// Use account method for lodgement and transaction entry
			acc.lodge(balance);
			markDataChanged();
				
			 JOptionPane.showMessageDialog(f, balance + euro + " added do you account!" ,"Lodgement",  JOptionPane.INFORMATION_MESSAGE);
			 JOptionPane.showMessageDialog(f, "New balance = " + acc.getBalance() + euro ,"Lodgement",  JOptionPane.INFORMATION_MESSAGE);
			}

			}	
	     });
		
		withdrawButton.addActionListener(new ActionListener(  ) {
			public void actionPerformed(ActionEvent ae) {
				boolean on = true;
				double withdraw = 0;

				if(acc.requiresPinVerification())
				{
					// Reuse helper method for current account PIN checks
					on = verifyCurrentAccountPin(e);
				}		if(on == true)
						{
					// Reuse helper method for numeric input
					Double withdrawValue = dialogInputHelper.promptForNumericInputOnce(f, "Enter amount you wish to withdraw (max 500):");
					if(withdrawValue == null)
					{
						return;
					}
					withdraw = withdrawValue.doubleValue();
					if(withdraw > 500)
					{
						JOptionPane.showMessageDialog(f, "500 is the maximum you can withdraw at a time." ,"Oops!",  JOptionPane.INFORMATION_MESSAGE);
						withdraw = 0;
					}
					if(!acc.canWithdraw(withdraw))
					{
						JOptionPane.showMessageDialog(f, "Insufficient funds." ,"Oops!",  JOptionPane.INFORMATION_MESSAGE);
						withdraw = 0;					
					}
				
				String euro = "\u20ac";
				// Use account method for withdrawal and transaction entry
				acc.withdraw(withdraw);
				markDataChanged();
				 
				 
					
				 JOptionPane.showMessageDialog(f, withdraw + euro + " withdrawn." ,"Withdraw",  JOptionPane.INFORMATION_MESSAGE);
				 JOptionPane.showMessageDialog(f, "New balance = " + acc.getBalance() + euro ,"Withdraw",  JOptionPane.INFORMATION_MESSAGE);
				}
				 
					
					
			}	
	     });
		
		returnButton.addActionListener(new ActionListener(  ) {
			public void actionPerformed(ActionEvent ae) {
				// Exit customer menu and return to start menu
				f.dispose();		
				menuStart();				
			}
	     });		}		
	     });
		}
		}
		
		// Check if there are customers before an admin action runs
		private boolean hasCustomersForAdminAction()
		{
			if(customerList.isEmpty())
			{
				JOptionPane.showMessageDialog(f, "There are no customers yet!"  ,"Oops!",  JOptionPane.INFORMATION_MESSAGE);
				f.dispose();
				admin();
				return false;
			}
			return true;
		}

		// Find a customer in the list using the customer ID
	private Customer findCustomerById(String customerID)
	{
		for (Customer aCustomer: customerList){
			if(aCustomer.getCustomerID().equals(customerID))
			{
				return aCustomer;
			}
		}
		return null;
	}

	// Ask for customer ID and retry when customer is not found
	private Customer promptForCustomerByIdWithRetry(String promptMessage)
	{
		while(true)
		{
			Object customerID = JOptionPane.showInputDialog(f, promptMessage);
			Customer selectedCustomer = findCustomerById(String.valueOf(customerID));
			if(selectedCustomer != null)
			{
				return selectedCustomer;
			}
			int reply  = JOptionPane.showConfirmDialog(null, null, "User not found. Try again?", JOptionPane.YES_NO_OPTION);
			if(reply == JOptionPane.YES_OPTION)
			{
				continue;
			}
			f.dispose();
			admin();
			return null;
		}
	}

	// Fill the customer text fields with one customer's details
	private void populateCustomerDetailsFields(Customer selectedCustomer)
	{
		firstNameTextField.setText(selectedCustomer.getFirstName());
		surnameTextField.setText(selectedCustomer.getSurname());
		pPSTextField.setText(selectedCustomer.getPPS());
		dOBTextField.setText(selectedCustomer.getDOB());
		customerIDTextField.setText(selectedCustomer.getCustomerID());
		passwordTextField.setText(selectedCustomer.getPassword());
	}

	// Show all accounts in a sortable and non-editable table
	private void showAllAccountsTable()
	{
		adminAccountHelper.showAllAccountsTable(f, customerList,
				new Runnable() {
					public void run() {
						admin();
					}
				},
				new Runnable() {
					public void run() {
						exitApplicationWithSavePrompt();
					}
				});
	}

	// Check PIN for current account actions
	private boolean verifyCurrentAccountPin(Customer selectedCustomer)
	{
		// Use current account method for PIN verification and card locking
		boolean verified = ((CustomerCurrentAccount) acc).verifyPinWithThreeAttempts(f);
		if(!verified)
		{
			customer(selectedCustomer);
			return false;
		}
		return true;
	}

	// Set overdraft limit for a selected current account
	private void setOverdraftForCurrentAccount()
	{
		if(!hasCustomersForAdminAction())
		{
			return;
		}

		String customerID = JOptionPane.showInputDialog(f, "Customer ID of Customer You Wish to Set Overdraft For:");
		if(customerID == null || customerID.trim().length() == 0)
		{
			return;
		}

		Customer selectedCustomer = findCustomerById(customerID.trim());
		if(selectedCustomer == null)
		{
			JOptionPane.showMessageDialog(f, "Customer not found." ,"Set Overdraft",  JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		ArrayList<String> currentAccountNumbers = new ArrayList<String>();
		for(CustomerAccount account : selectedCustomer.getAccounts())
		{
			if(account instanceof CustomerCurrentAccount)
			{
				currentAccountNumbers.add(account.getNumber());
			}
		}

		if(currentAccountNumbers.isEmpty())
		{
			JOptionPane.showMessageDialog(f, "This customer has no current accounts." ,"Set Overdraft",  JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		String selectedAccountNumber = (String) JOptionPane.showInputDialog(
				f,
				"Select current account:",
				"Set Overdraft",
				JOptionPane.QUESTION_MESSAGE,
				null,
				currentAccountNumbers.toArray(),
				currentAccountNumbers.get(0));
		if(selectedAccountNumber == null)
		{
			return;
		}

		CustomerAccount selectedAccount = adminAccountHelper.findAccountByNumber(selectedCustomer.getAccounts(), selectedAccountNumber);
		if(!(selectedAccount instanceof CustomerCurrentAccount))
		{
			JOptionPane.showMessageDialog(f, "Selected account is not a current account." ,"Set Overdraft",  JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		Double overdraftValue = dialogInputHelper.promptForNumericInputOnce(f, "Enter overdraft limit:");
		if(overdraftValue == null)
		{
			return;
		}

		((CustomerCurrentAccount) selectedAccount).setOverdraftLimit(overdraftValue.doubleValue());
		markDataChanged();
		JOptionPane.showMessageDialog(f, "Overdraft set to " + overdraftValue + " for account " + selectedAccount.getNumber(), "Set Overdraft", JOptionPane.INFORMATION_MESSAGE);
	}

	// Calculate interest for all deposit accounts
	private void calculateInterestForAllDepositAccounts()
	{
		if(!hasCustomersForAdminAction())
		{
			return;
		}

		Double interestValue = dialogInputHelper.promptForNumericInputOnce(f, "Enter interest percentage to apply to all deposit accounts:");
		if(interestValue == null)
		{
			return;
		}

		int updatedAccounts = 0;
		for(Customer selectedCustomer : customerList)
		{
			for(CustomerAccount account : selectedCustomer.getAccounts())
			{
				if(account instanceof CustomerDepositAccount)
				{
					CustomerDepositAccount depositAccount = (CustomerDepositAccount) account;
					depositAccount.setInterestRate(interestValue.doubleValue());
					depositAccount.applyInterestPercentage(interestValue.doubleValue());
					updatedAccounts++;
				}
			}
		}

		if(updatedAccounts == 0)
		{
			JOptionPane.showMessageDialog(f, "No deposit accounts found." ,"Calculate Interest",  JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		markDataChanged();
		JOptionPane.showMessageDialog(f, "Interest applied to " + updatedAccounts + " deposit account(s).", "Calculate Interest", JOptionPane.INFORMATION_MESSAGE);
	}

	// Mark that in-memory data has changed
	private void markDataChanged()
	{
		hasUnsavedChanges = true;
	}

	// Open data from a random access file
	private void openDataFromFile()
	{
		JFileChooser chooser = new JFileChooser();
		int option = chooser.showOpenDialog(f);
		if(option != JFileChooser.APPROVE_OPTION)
		{
			return;
		}

		File selectedFile = chooser.getSelectedFile();
		try
		{
			customerList = bankFile.open(selectedFile.getAbsolutePath());
			currentFilePath = selectedFile.getAbsolutePath();
			hasUnsavedChanges = false;
			JOptionPane.showMessageDialog(f, "File opened successfully.", "Open", JOptionPane.INFORMATION_MESSAGE);
		}
		catch (IOException ex)
		{
			JOptionPane.showMessageDialog(f, "Could not open file: " + ex.getMessage(), "Open Error", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	// Save data to current file path
	private boolean saveData()
	{
		if(currentFilePath == null)
		{
			return saveDataAs();
		}
		try
		{
			bankFile.save(currentFilePath, customerList);
			hasUnsavedChanges = false;
			JOptionPane.showMessageDialog(f, "File saved successfully.", "Save", JOptionPane.INFORMATION_MESSAGE);
			return true;
		}
		catch (IOException ex)
		{
			JOptionPane.showMessageDialog(f, "Could not save file: " + ex.getMessage(), "Save Error", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
	}

	// Save data to a chosen file path
	private boolean saveDataAs()
	{
		JFileChooser chooser = new JFileChooser();
		int option = chooser.showSaveDialog(f);
		if(option != JFileChooser.APPROVE_OPTION)
		{
			return false;
		}

		File selectedFile = chooser.getSelectedFile();
		currentFilePath = selectedFile.getAbsolutePath();
		return saveData();
	}

	// Ask user to save before exiting the application
	private void exitApplicationWithSavePrompt()
	{
		if(hasUnsavedChanges)
		{
			int option = JOptionPane.showConfirmDialog(f, "Save changes before exit?", "Exit Application", JOptionPane.YES_NO_CANCEL_OPTION);
			if(option == JOptionPane.CANCEL_OPTION)
			{
				return;
			}
			if(option == JOptionPane.YES_OPTION)
			{
				boolean saved = saveData();
				if(!saved)
				{
					return;
				}
			}
		}
		System.exit(0);
	}

}


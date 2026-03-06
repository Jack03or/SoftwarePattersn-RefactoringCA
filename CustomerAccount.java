import java.util.ArrayList; 
import java.util.Date;

public class CustomerAccount  {
   
	String number;
	double balance;
	ArrayList<AccountTransaction> transactionList = new ArrayList<AccountTransaction>();

	//Blank Constructor
	public CustomerAccount()
	{
		this.number = "";
		this.balance = 0;
		this.transactionList = null;
	}
	
	//Constructor with Details
	public CustomerAccount(String number, double balance, ArrayList<AccountTransaction> transactionList)
	{
		this.number = number;
		this.balance = balance;
		this.transactionList = transactionList;
	}
	
	//Accessor methods
	
	public String getNumber()
	{
		return this.number;
	}
	
	

	
	public double getBalance()
	{
		return this.balance;
	}
	
	public ArrayList<AccountTransaction> getTransactionList()
	{
		return this.transactionList;
	}

	//Mutator methods
	public void setNumber(String number)
	{
		this.number = number;
	}
	
	public void setBalance(double balance)
	{
		this.balance = balance;
	}
	
	public void setTransactionList(ArrayList<AccountTransaction> transactionList)
	{
		this.transactionList = transactionList;
	}
	
	// Apply a standard admin charge for this account type
	public void applyAdminCharge()
	{
		this.balance = this.balance - getAdminChargeAppliedAmount();
	}

	// Amount shown in the admin charge message
	public String getAdminChargeDisplayAmount()
	{
		return "0";
	}

	// Amount deducted when admin charge is applied
	public double getAdminChargeAppliedAmount()
	{
		return 0;
	}

	// Return account type text for persistence and display
	public String getAccountType()
	{
		return "Current";
	}

	// Base account has no overdraft limit
	public double getOverdraftLimit()
	{
		return 0;
	}

	// Base account has no stored interest rate
	public double getStoredInterestRate()
	{
		return 0;
	}

	// Apply interest percentage to this account
	public void applyInterestPercentage(double interest)
	{
		this.balance = this.balance + (this.balance * (interest/100));
	}

	// Add money and record a transaction
	public void lodge(double amount)
	{
		this.balance = this.balance + amount;
		addTransaction("Lodgement", amount);
	}

	// Check if withdrawal amount is valid for this account
	public boolean canWithdraw(double amount)
	{
		return amount <= this.balance;
	}

	// Withdraw money and record a transaction
	public void withdraw(double amount)
	{
		this.balance = this.balance - amount;
		addTransaction("Withdraw", amount);
	}

	// Add one transaction entry to the account list
	private void addTransaction(String type, double amount)
	{
		Date date = new Date();
		String dateText = date.toString();
		AccountTransaction transaction = new AccountTransaction(dateText, type, amount);
		this.transactionList.add(transaction);
	}
	
	
}

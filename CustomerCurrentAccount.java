import java.util.ArrayList;

public class CustomerCurrentAccount extends CustomerAccount 
{
	ATMCard atm;
	double overdraftLimit;
	
public CustomerCurrentAccount()
{
	super();
	this.atm = null;
	this.overdraftLimit = 0;
	
}

public CustomerCurrentAccount(ATMCard atm, String number, double balance, ArrayList<AccountTransaction> transactionList)
{
	super(number, balance, transactionList);	
	this.atm = atm;
	this.overdraftLimit = 0;
}

public CustomerCurrentAccount(ATMCard atm, String number, double balance, double overdraftLimit, ArrayList<AccountTransaction> transactionList)
{
	super(number, balance, transactionList);
	this.atm = atm;
	this.overdraftLimit = overdraftLimit;
}

public ATMCard getAtm()
{
	return this.atm;
}

public void setAtm(ATMCard atm)
{
	this.atm = atm;
}

	// Current account keeps its overdraft limit
	public double getOverdraftLimit()
	{
		return this.overdraftLimit;
	}

	public void setOverdraftLimit(double overdraftLimit)
	{
		this.overdraftLimit = overdraftLimit;
	}

	// Current account shows 15 euro in the admin fee message
	public String getAdminChargeDisplayAmount()
	{
		return "15";
	}

	// Keep current behavior for amount deducted
	public double getAdminChargeAppliedAmount()
	{
		return 25;
	}

	// Current account type text
	public String getAccountType()
	{
		return "Current";
	}

	// Current account withdrawal can use overdraft limit
	public boolean canWithdraw(double amount)
	{
		return (this.balance - amount) >= (0 - this.overdraftLimit);
	}

}

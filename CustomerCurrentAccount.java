import java.util.ArrayList;

public class CustomerCurrentAccount extends CustomerAccount 
{
	ATMCard atm;
	
public CustomerCurrentAccount()
{
	super();
	this.atm = null;
	
}

public CustomerCurrentAccount(ATMCard atm, String number, double balance, ArrayList<AccountTransaction> transactionList)
{
	super(number, balance, transactionList);	
	this.atm = atm;
}

public ATMCard getAtm()
{
	return this.atm;
}

public void setAtm(ATMCard atm)
{
	this.atm = atm;
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

}

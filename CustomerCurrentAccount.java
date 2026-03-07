import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

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

	// Check if entered PIN matches account PIN
	public boolean isPinCorrect(int enteredPin)
	{
		return this.atm.getPin() == enteredPin;
	}

	// Lock ATM card after failed PIN attempts
	public void lockCard()
	{
		this.atm.setValid(false);
	}

	// Verify PIN with 3 attempts
	public boolean verifyPinWithThreeAttempts(JFrame frame)
	{
		int count = 3;
		while(count > 0)
		{
			String pinText = JOptionPane.showInputDialog(frame, "Enter 4 digit PIN;");
			if(pinText == null)
			{
				return false;
			}
			if(!pinText.matches("\\d{4}"))
			{
				JOptionPane.showMessageDialog(frame, "PIN must be 4 digits." ,  "Pin", JOptionPane.INFORMATION_MESSAGE);
				continue;
			}
			int pin = Integer.parseInt(pinText);
			if(isPinCorrect(pin))
			{
				JOptionPane.showMessageDialog(frame, "Pin entry successful" ,  "Pin", JOptionPane.INFORMATION_MESSAGE);
				return true;
			}
			count --;
			JOptionPane.showMessageDialog(frame, "Incorrect pin. " + count + " attempts remaining."  ,"Pin",  JOptionPane.INFORMATION_MESSAGE);
		}

		lockCard();
		JOptionPane.showMessageDialog(frame, "Pin entered incorrectly 3 times. ATM card locked."  ,"Pin",  JOptionPane.INFORMATION_MESSAGE);
		return false;
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

	// Current account requires PIN verification
	public boolean requiresPinVerification()
	{
		return true;
	}

}

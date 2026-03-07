import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class DialogInputHelper {

	// Prompt once for a numeric value
	public Double promptForNumericInputOnce(JFrame frame, String message)
	{
		String input = JOptionPane.showInputDialog(frame, message);
		if(input == null)
		{
			return null;
		}
		if(isNumeric(input))
		{
			return Double.valueOf(input);
		}
		JOptionPane.showMessageDialog(frame, "You must enter a numerical value!" ,"Oops!",  JOptionPane.INFORMATION_MESSAGE);
		return null;
	}

	// Prompt until a numeric value is entered
	public Double promptForNumericInputWithRetry(JFrame frame, String message)
	{
		boolean loop = true;
		while(loop)
		{
			String input = JOptionPane.showInputDialog(frame, message);
			if(input == null)
			{
				return null;
			}
			if(isNumeric(input))
			{
				return Double.valueOf(input);
			}
			JOptionPane.showMessageDialog(frame, "You must enter a numerical value!" ,"Oops!",  JOptionPane.INFORMATION_MESSAGE);
		}
		return 0.0;
	}

	// Test if a string can be parsed as a number
	private boolean isNumeric(String text)
	{
		try
		{
			Double.parseDouble(text);
		}
		catch(NumberFormatException nfe)
		{
			return false;
		}
		return true;
	}
}

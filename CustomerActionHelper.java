import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class CustomerActionHelper {

	// Show account statement window
	public void showAccountStatement(JFrame sourceFrame, final CustomerAccount account, final Runnable onReturn, final Runnable onWindowClose)
	{
		sourceFrame.dispose();

		final JFrame statementFrame = new JFrame("Customer Menu");
		statementFrame.setSize(400, 600);
		statementFrame.setLocation(200, 200);
		statementFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) { onWindowClose.run(); }
		});
		statementFrame.setVisible(true);

		JLabel label1 = new JLabel("Summary of account transactions: ");
		JButton returnButton = new JButton("Return");
		JPanel textPanel = new JPanel();

		textPanel.setLayout( new BorderLayout() );
		JTextArea textArea = new JTextArea(40, 20);
		textArea.setEditable(false);
		textPanel.add(label1, BorderLayout.NORTH);
		textPanel.add(textArea, BorderLayout.CENTER);
		textPanel.add(returnButton, BorderLayout.SOUTH);

		JScrollPane scrollPane = new JScrollPane(textArea);
		textPanel.add(scrollPane);

		for (int i = 0; i < account.getTransactionList().size(); i ++)
		{
			textArea.append(account.getTransactionList().get(i).toString());
		}

		textPanel.add(textArea);

		Container content = statementFrame.getContentPane();
		content.setLayout(new GridLayout(1, 1));
		content.add(textPanel);

		returnButton.addActionListener(new ActionListener(  ) {
			public void actionPerformed(ActionEvent ae) {
				statementFrame.dispose();
				onReturn.run();
			}
	     });
	}
}

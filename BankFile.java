import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class BankFile {

	private static final int MAX_RECORDS = 25;

	private static final int ACCOUNT_NUMBER_LEN = 12;
	private static final int SURNAME_LEN = 20;
	private static final int FIRST_NAME_LEN = 20;
	private static final int CUSTOMER_ID_LEN = 20;
	private static final int PASSWORD_LEN = 20;
	private static final int ACCOUNT_TYPE_LEN = 10;

	private static final int RECORD_SIZE = 1
			+ 4
			+ (ACCOUNT_NUMBER_LEN * 2)
			+ (SURNAME_LEN * 2)
			+ (FIRST_NAME_LEN * 2)
			+ (CUSTOMER_ID_LEN * 2)
			+ (PASSWORD_LEN * 2)
			+ (ACCOUNT_TYPE_LEN * 2)
			+ 8
			+ 8
			+ 8;

	// Save customer accounts using random access and hash with linear probing
	public void save(String filePath, ArrayList<Customer> customers) throws IOException
	{
		ArrayList<RecordData> records = flattenRecords(customers);
		if(records.size() > MAX_RECORDS)
		{
			throw new IOException("Only 25 records can be saved in this file.");
		}

		File file = new File(filePath);
		try (RandomAccessFile raf = new RandomAccessFile(file, "rw"))
		{
			raf.setLength((long) RECORD_SIZE * MAX_RECORDS);
			for(int i = 0; i < MAX_RECORDS; i++)
			{
				writeEmptyRecord(raf, i);
			}

			for(int i = 0; i < records.size(); i++)
			{
				RecordData record = records.get(i);
				record.accountId = i + 1;
				int slot = findSlotForWrite(raf, record.accountNumber);
				if(slot == -1)
				{
					throw new IOException("No free slot found while saving records.");
				}
				writeRecord(raf, slot, record);
			}
		}
	}

	// Open random access file and rebuild customers/accounts in memory
	public ArrayList<Customer> open(String filePath) throws IOException
	{
		ArrayList<Customer> customers = new ArrayList<Customer>();
		File file = new File(filePath);
		if(!file.exists())
		{
			return customers;
		}

		try (RandomAccessFile raf = new RandomAccessFile(file, "r"))
		{
			long expectedLength = (long) RECORD_SIZE * MAX_RECORDS;
			if(raf.length() < expectedLength)
			{
				throw new IOException("File format is not valid for this assignment.");
			}

			for(int i = 0; i < MAX_RECORDS; i++)
			{
				RecordData record = readRecord(raf, i);
				if(!record.active)
				{
					continue;
				}

				Customer customer = findCustomerById(customers, record.customerId);
				if(customer == null)
				{
					customer = new Customer("", record.surname, record.firstName, "", record.customerId, record.password, new ArrayList<CustomerAccount>());
					customers.add(customer);
				}

				CustomerAccount account;
				if("Deposit".equalsIgnoreCase(record.accountType))
				{
					account = new CustomerDepositAccount(record.interestRate, record.accountNumber, record.balance, new ArrayList<AccountTransaction>());
				}
				else
				{
					account = new CustomerCurrentAccount(new ATMCard(0000, true), record.accountNumber, record.balance, record.overdraft, new ArrayList<AccountTransaction>());
				}
				customer.getAccounts().add(account);
			}
		}
		return customers;
	}

	private Customer findCustomerById(ArrayList<Customer> customers, String customerId)
	{
		for(Customer currentCustomer : customers)
		{
			if(currentCustomer.getCustomerID().equals(customerId))
			{
				return currentCustomer;
			}
		}
		return null;
	}

	private ArrayList<RecordData> flattenRecords(ArrayList<Customer> customers)
	{
		ArrayList<RecordData> records = new ArrayList<RecordData>();
		for(Customer customer : customers)
		{
			for(CustomerAccount account : customer.getAccounts())
			{
				RecordData record = new RecordData();
				record.active = true;
				record.accountNumber = safe(account.getNumber());
				record.surname = safe(customer.getSurname());
				record.firstName = safe(customer.getFirstName());
				record.customerId = safe(customer.getCustomerID());
				record.password = safe(customer.getPassword());
				record.accountType = account.getAccountType();
				record.balance = account.getBalance();
				record.overdraft = account.getOverdraftLimit();
				record.interestRate = account.getStoredInterestRate();
				records.add(record);
			}
		}
		return records;
	}

	private int findSlotForWrite(RandomAccessFile raf, String accountNumber) throws IOException
	{
		int hash = hashAccountNumber(accountNumber);
		for(int i = 0; i < MAX_RECORDS; i++)
		{
			int slot = (hash + i) % MAX_RECORDS;
			long pos = positionForSlot(slot);
			raf.seek(pos);
			boolean active = raf.readBoolean();
			if(!active)
			{
				return slot;
			}
		}
		return -1;
	}

	private int hashAccountNumber(String accountNumber)
	{
		int hash = Math.abs(accountNumber.hashCode());
		return hash % MAX_RECORDS;
	}

	private long positionForSlot(int slot)
	{
		return (long) slot * RECORD_SIZE;
	}

	private void writeEmptyRecord(RandomAccessFile raf, int slot) throws IOException
	{
		RecordData empty = new RecordData();
		empty.active = false;
		writeRecord(raf, slot, empty);
	}

	private void writeRecord(RandomAccessFile raf, int slot, RecordData record) throws IOException
	{
		raf.seek(positionForSlot(slot));
		raf.writeBoolean(record.active);
		raf.writeInt(record.accountId);
		writeFixedString(raf, safe(record.accountNumber), ACCOUNT_NUMBER_LEN);
		writeFixedString(raf, safe(record.surname), SURNAME_LEN);
		writeFixedString(raf, safe(record.firstName), FIRST_NAME_LEN);
		writeFixedString(raf, safe(record.customerId), CUSTOMER_ID_LEN);
		writeFixedString(raf, safe(record.password), PASSWORD_LEN);
		writeFixedString(raf, safe(record.accountType), ACCOUNT_TYPE_LEN);
		raf.writeDouble(record.balance);
		raf.writeDouble(record.overdraft);
		raf.writeDouble(record.interestRate);
	}

	private RecordData readRecord(RandomAccessFile raf, int slot) throws IOException
	{
		raf.seek(positionForSlot(slot));
		RecordData record = new RecordData();
		record.active = raf.readBoolean();
		record.accountId = raf.readInt();
		record.accountNumber = readFixedString(raf, ACCOUNT_NUMBER_LEN);
		record.surname = readFixedString(raf, SURNAME_LEN);
		record.firstName = readFixedString(raf, FIRST_NAME_LEN);
		record.customerId = readFixedString(raf, CUSTOMER_ID_LEN);
		record.password = readFixedString(raf, PASSWORD_LEN);
		record.accountType = readFixedString(raf, ACCOUNT_TYPE_LEN);
		record.balance = raf.readDouble();
		record.overdraft = raf.readDouble();
		record.interestRate = raf.readDouble();
		return record;
	}

	private void writeFixedString(RandomAccessFile raf, String value, int length) throws IOException
	{
		String text = value;
		if(text.length() > length)
		{
			text = text.substring(0, length);
		}
		StringBuilder builder = new StringBuilder(text);
		while(builder.length() < length)
		{
			builder.append(' ');
		}
		raf.writeChars(builder.toString());
	}

	private String readFixedString(RandomAccessFile raf, int length) throws IOException
	{
		char[] chars = new char[length];
		for(int i = 0; i < length; i++)
		{
			chars[i] = raf.readChar();
		}
		return new String(chars).trim();
	}

	private String safe(String value)
	{
		if(value == null)
		{
			return "";
		}
		return value;
	}

	private static class RecordData
	{
		boolean active;
		int accountId;
		String accountNumber = "";
		String surname = "";
		String firstName = "";
		String customerId = "";
		String password = "";
		String accountType = "";
		double balance;
		double overdraft;
		double interestRate;
	}
}

package me.Josh123likeme.FinanceTracker.Transactions;

import java.time.LocalDate;

import me.Josh123likeme.FinanceTracker.TUID;

public class SingleTransaction {
	
	private String tuid;
	public String name;
	public String description;
	public String category;
	public TransactionDirection transactionDirection;
	public Money amount;
	public LocalDate dateOfTransaction;
	
	public SingleTransaction(
			String name, 
			String description, 
			String category,
			TransactionDirection transactionDirection, 
			Money amount,
			LocalDate dateOfTransaction) {
		
		
		tuid = TUID.generateTimestampedUniqueIdentifier();
		
		if (name == null || name == "") {
			this.name = "Transaction " + tuid;
			System.out.println("WARN: No name provided. Using unique generated name \"" + this.name + "\"");
		}
		else this.name = name;
		
		if (description == null) this.description = "";
		else this.description = description;
		
		if (transactionDirection == null) throw new IllegalArgumentException("Transaction direction must not be null");
		else this.transactionDirection = transactionDirection;
		
		if (amount == null) throw new IllegalArgumentException("Amount cannot be null");
		else this.amount = amount;
		
		if (dateOfTransaction == null) throw new IllegalArgumentException("Date of transaction cannot be null");
		else this.dateOfTransaction = dateOfTransaction;
		
	}
	
	public String getTUID() {
		
		return tuid;
	}
	
	public String toString() {
		
		StringBuilder str = new StringBuilder();
		
		str.append(transactionDirection == TransactionDirection.INCOME ? "+" : "-");
		str.append(amount.toString() + " ");
		str.append("[" + dateOfTransaction.toString() + "]: ");
		str.append(name + " (" + (category != null ? category : "No Category") + ")");
		if (description != null && description.length() > 0) str.append(" - " + description);
		
		return str.toString();
		
	}
	
}

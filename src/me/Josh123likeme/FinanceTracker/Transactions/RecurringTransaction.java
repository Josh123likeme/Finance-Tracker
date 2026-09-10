package me.Josh123likeme.FinanceTracker.Transactions;

import java.time.LocalDate;

import me.Josh123likeme.FinanceTracker.TUID;

public class RecurringTransaction {
	
	private String tuid;
	public String name;
	public String description;
	public String category;
	TransactionDirection transactionDirection;
	Money amount;
	
	public PeriodType periodType;
	public LocalDate startDate;
	public LocalDate endDate; //if endDate is null, it is a forever recurring transaction
	public Integer interval;
	
	public RecurringTransaction(
			String name,
			String description,
			String category,
			TransactionDirection transactionDirection, 
			Money amount,
			PeriodType periodType,
			LocalDate startDate,
			LocalDate endDate,
			Integer interval) {
		
		tuid = TUID.generateTimestampedUniqueIdentifier();
		
		
		if (name == null || name.isEmpty()) {
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
		
		if (periodType == null) throw new IllegalArgumentException("Period type cannot be null");
		else this.periodType = periodType;
		
		if (startDate == null) throw new IllegalArgumentException("Start date cannot be null");
		else this.startDate = startDate;
		
		this.endDate = endDate;
		
		if ((interval == null || interval < 2) && periodType.requiresInterval) throw new IllegalArgumentException("Interval cannot be null or less than two for period type " + periodType.name());
		else this.interval = interval;
		
		if (interval != null && !periodType.requiresInterval) {
			this.interval = null;
			System.out.println("WARN: " + periodType.name() + " does not require an interval");
		}
	}
	
	public SingleTransaction generateSingleTransactionInstance(LocalDate date) {
		
		return new SingleTransaction(name, description, category, transactionDirection, amount, date);
	}
	
	public String getTUID() {
		
		return tuid;
	}
	
	public String toString() {
		
		StringBuilder str = new StringBuilder();
		
		str.append(transactionDirection == TransactionDirection.INCOME ? "+" : "-");
		str.append(amount.toString() + " ");
		if (periodType.requiresInterval) str.append("Every " + interval + " " + periodType.niceName + " ");
		else str.append("Every " + periodType.niceName + " ");
		str.append( "[" + startDate.toString() + " to " + (endDate != null ? endDate.toString() : "FOREVER") + "]: ");
		str.append(name + " (" + (category != null ? category : "No Category") + ")");
		if (description != null && description.length() > 0) str.append(" - " + description);
		
		return str.toString();
		
	}
	
}

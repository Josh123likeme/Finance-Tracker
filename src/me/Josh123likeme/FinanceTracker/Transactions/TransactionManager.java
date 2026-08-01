package me.Josh123likeme.FinanceTracker.Transactions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TransactionManager {
	
	private TransactionData td;
	
	public Money getEarningsOnDay(LocalDate day) {
		
		List<SingleTransaction> transactions = transactionsOnDay(day);
		
		Money earnings = new Money(0);
		
		for (SingleTransaction transaction : transactions) {
			
			if (transaction.transactionDirection == TransactionDirection.INCOME) earnings = earnings.add(transaction.amount);
			else if (transaction.transactionDirection == TransactionDirection.EXPENSE) earnings = earnings.subtract(transaction.amount);
			
		}
		
		return earnings;
		
	}
	
	public List<SingleTransaction> transactionsOnDay(LocalDate day) {
		
		List<SingleTransaction> transactionsOnDay = new ArrayList<SingleTransaction>();
		
		for (SingleTransaction transaction : td.singleTransactions) {
			
			if (transaction.dateOfTransaction.equals(day)) transactionsOnDay.add(transaction);
			
		}
		
		for (RecurringTransaction transaction : td.recurringTransactions) {
			
			LocalDate currentDate = transaction.startDate;
			
			while (true) {
				
				//transaction occurs on this day
				if (currentDate.equals(day)) break;
				
				//we have counted past this day
				if (currentDate.isAfter(day)) break;
				
				//recurring transaction has finished
				if (transaction.endDate != null && currentDate.isAfter(transaction.endDate)) break;
				
				switch (transaction.periodType) {
				
				case DAILY: currentDate = currentDate.plusDays(1); break;
				case WEEKLY: currentDate = currentDate.plusWeeks(1); break;
				case N_WEEKLY: currentDate = currentDate.plusWeeks(transaction.interval); break;
				case MONTHLY: currentDate = currentDate.plusMonths(1); break;
				case N_MONTHLY: currentDate = currentDate.plusMonths(transaction.interval); break;
				case YEARLY: currentDate = currentDate.plusYears(1); break;
				
				}
				
			}
			
			if (currentDate.equals(day)) transactionsOnDay.add(transaction.generateSingleTransactionInstance(currentDate));
			
		}
		
		return transactionsOnDay;
		
	}
	
	public void addSingleTransaction(SingleTransaction singleTransaction) {
		
		td.singleTransactions.add(singleTransaction);
	}
	public void addRecurringTransaction(RecurringTransaction recurringTransaction) {
		
		td.recurringTransactions.add(recurringTransaction);
	}
	
	public List<SingleTransaction> getSingleTransactions() {
		
		return td.singleTransactions;
	}
	public List<RecurringTransaction> getRecurringTransactions() {
		
		return td.recurringTransactions;
	}
	public List<String> getTransactionCategories() {
		
		return td.transactionCategories;
	}
	
	public void loadTransactions(Path path) {
		
		if (!Files.exists(path)) {
			
			System.out.println("No transaction file found, creating new one");
			
			saveTransactions(path);
			
		}
		
		Gson gson = new Gson();
		
		String jsonString = "";
		
		try {
			jsonString = Files.readString(path);
		} catch (IOException e) {
			
			throw new IllegalArgumentException("Failed to load transaction file");
		}
		
		td = gson.fromJson(jsonString, TransactionData.class);
		
		if (td == null) td = new TransactionData();
		
	}
	
	public void saveTransactions(Path path) {
		
		Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
		
		String jsonString = gson.toJson(td);
		
		try {
			Files.writeString(path, jsonString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	class TransactionData {
		
		public List<SingleTransaction> singleTransactions = new ArrayList<SingleTransaction>();
		public List<RecurringTransaction> recurringTransactions = new ArrayList<RecurringTransaction>();
		public List<String> transactionCategories = new ArrayList<String>();
		
	}
	
}

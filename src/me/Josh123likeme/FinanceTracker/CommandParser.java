package me.Josh123likeme.FinanceTracker;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import me.Josh123likeme.FinanceTracker.Transactions.*;

public class CommandParser {
	
	private static final Path DATALOC = Path.of(System.getProperty("user.home")).resolve("transaction.json");
	
	public static void parseCommand(String[] args) {

		//decode command
		switch (args[0]) {
		
		case "-h":
		case "--help":
			System.out.println("\nFITR - FinanceTracker"
					+ "\n\nCommands:"
					+ "\n  add           Add transaction"
					+ "\n  remove        Remove a transaction"
					+ "\n  list          List transactions"
					+ "\n  transactions  View transactions"
					+ "\n  clear         Clear all transactions"
					+ "\n  categories    Access categories"
					+ "\n\nOptions:"
					+ "\n  --help, -h    Show this help message"
					+ "\n");
			System.exit(0);
			break;
		case "add": parseAddCommand(Arrays.copyOfRange(args, 1, args.length)); break;
		case "remove": parseRemoveCommand(Arrays.copyOfRange(args, 1, args.length)); break;
		case "list": parseListCommand(Arrays.copyOfRange(args, 1, args.length)); break;
		case "transactions": parseTransactionsCommand(Arrays.copyOfRange(args, 1, args.length)); break;
		case "clear": parseClearCommand(Arrays.copyOfRange(args, 1, args.length)); break;
		case "categories": parseCategoriesCommand(Arrays.copyOfRange(args, 1, args.length)); break;
		
		}
		
	}
	
	private static void parseAddCommand(String[] args) {
		
		TransactionType transactionType = null;
		String name = null;
		String description = null;
		String category = null;
		TransactionDirection transactionDirection = null;
		Money amount = null;
		PeriodType periodType = null;
		LocalDate dateOfTransaction = null;
		LocalDate startDate = null;
		LocalDate endDate = null;
		Integer interval = null;
		
		for (int i = 0; i < args.length; i++) {
			
			switch (args[i]) {
			
			case "-h":
			case "--help":
				System.out.println("\nFITR - FinanceTracker add"
						+ "\n\nUsage:"
						+ "\n  ./fitr add"
						+ "\n\nOptions:"
						+ "\n  --help, -h                          Show this help message"
						+ "\n  --recur, -r                         Select recurring transaction type"
						+ "\n  --single, -s                        Select single transaction type"
						+ "\n  --name <n>, -n <n>                  Add name n"
						+ "\n  --description <d>, -d <d>           Add description d"
						+ "\n  --category <c>, -c <c>              Add category c"
						+ "\n  --income, -inc                      Select income transaction type"
						+ "\n  --expense, -exp                     Select expense transaction type"
						+ "\n  --amount <a>, -amt <a>              Add amount a in p or £.pp format"
						+ "\n  --periodtype <p>, -pt <p>           Add period type p (for recurring transactions) "
						+ "\n                                      {DAILY, WEEKLY, N_WEEKLY, MONTHLY, N_MONTHLY, YEARLY}"
						+ "\n  --dateoftransaction <d>, -dt <d>    Add date of transaction d in yyyy-MM-dd format (for single transactions)"
						+ "\n  --startdate <d>, -sd <d>            Add start date d in yyyy-MM-dd format (for recurring transactions)"
						+ "\n  --enddate <d>, -ed <d>              Add end date d in yyyy-MM-dd format (for recurring transactions)"
						+ "\n  --interval <i>, -i <i>              Add interval i (for N-based recurring transactions)"
						+ "\n");
				System.exit(0);
				break;
			case "-r":
			case "--recur":
				transactionType = TransactionType.RECURRING;
				break;
			case "-s":
			case "--single":
				transactionType = TransactionType.SINGLE;
				break;
			case "-n":
			case "--name":
				name = args[++i];
				break;
			case "-d":
			case "--description":
				description = args[++i];
				break;
			case "-c":
			case "--category":
				category = args[++i];
				break;
			case "-inc":
			case "--income":
				transactionDirection = TransactionDirection.INCOME;
				break;
			case "-exp":
			case "--expense":
				transactionDirection = TransactionDirection.EXPENSE;
				break;
			case "-amt":
			case "--amount":
				if (args[i + 1].charAt(0) == '�') amount = Money.parse(args[++i].substring(1));
				else amount = Money.parse(args[++i]);
				break;
			case "-pt":
			case "--periodtype":
				periodType = Enum.valueOf(PeriodType.class, args[++i]);
				break;
			case "-dt":
			case "--dateoftransaction":
				dateOfTransaction = LocalDate.parse(args[++i]);
				break;
			case "-sd":
			case "--startdate":
				startDate = LocalDate.parse(args[++i]);
				break;
			case "-ed":
			case "--enddate":
				endDate = LocalDate.parse(args[++i]);
				break;
			case "-i":
			case "--interval":
				interval = Integer.parseInt(args[++i]);
				break;
				
			}
			
		}
		
		if (transactionType == null) throw new IllegalArgumentException("Provide the transaction type with -r|--recur|-s|--single");
		//if (name == null) throw new IllegalArgumentException("Provide the name with -n|--name");
		//if (description == null) throw new IllegalArgumentException("Provide the description with -d|--description");
		if (transactionDirection == null) throw new IllegalArgumentException("Provide the transaction direction with -inc|--income|-exp|--expense");
		if (amount == null) throw new IllegalArgumentException("Provide the amount with -amt|--amount");
		if (amount.getPence() < 0) throw new IllegalArgumentException("Amount cannot be less than 0");
		
		if (transactionType == TransactionType.RECURRING) {
			
			if (periodType == null) throw new IllegalArgumentException("Provide the period type with -pt|--periodtype");
			if (startDate == null) throw new IllegalArgumentException("Provide the start date with -sd|--startdate");
			
			if ((periodType == PeriodType.N_WEEKLY || periodType == PeriodType.N_MONTHLY) && interval == null) 
				throw new IllegalArgumentException("Provide the interval with -i|--interval");
			
		}
		if (transactionType == TransactionType.SINGLE) {
			
			if (dateOfTransaction == null) dateOfTransaction = LocalDate.now();
		}
		
		TransactionManager tm = new TransactionManager();
		
		tm.loadTransactions(DATALOC);
		
		//category provided is new
		if (category != null && !tm.getTransactionCategories().contains(category)) {
			
			System.out.println("This is an unknown category. Please add new categories with \"categories add\"");
			System.exit(1);
		}
		
		if (transactionType == TransactionType.SINGLE) {
			
			SingleTransaction st = new SingleTransaction(name, description, category, transactionDirection, amount, dateOfTransaction);
			tm.addSingleTransaction(st);
			System.out.println("Added new single transaction \"" + st.name + "\" for " + st.amount.toString() + " at " + st.dateOfTransaction.toString());
		}
		if (transactionType == TransactionType.RECURRING) {
			
			RecurringTransaction rt = new RecurringTransaction(name, description, category, transactionDirection, amount, periodType, startDate, endDate, interval);
			tm.addRecurringTransaction(rt);
			System.out.println("Added new recurring transaction \"" + rt.name + "\"");
		}
			
		tm.saveTransactions(DATALOC);
		
	}
	
	private static void parseRemoveCommand(String[] args) {
		
		if (args.length == 0) {
			
			System.out.println("Please provide the name of the transaction");
			System.exit(0);
		}
		
		boolean force = false;
		String name = null;
		
		for (int i = 0; i < args.length; i++) {
			
			if (args[i].equals("-f") || args[i].equals("--force")) force = true;
			else name = args[i];
			
		}
		
		TransactionManager tm = new TransactionManager();
		
		tm.loadTransactions(DATALOC);
		
		List<RecurringTransaction> recurringTransactions = new ArrayList<RecurringTransaction>();
		List<SingleTransaction> singleTransactions = new ArrayList<SingleTransaction>();
		
		for (RecurringTransaction transaction : tm.getRecurringTransactions()) {
			
			if (transaction.name.equals(name)) recurringTransactions.add(transaction);
		}
		for (SingleTransaction transaction : tm.getSingleTransactions()) {
			
			if (transaction.name.equals(name)) singleTransactions.add(transaction);
		}
		
		if (recurringTransactions.size() + singleTransactions.size() == 0) {
			
			System.out.println("Could not find transaction with name \"" + name + "\"");
			System.exit(1);
		}
		else if (recurringTransactions.size() + singleTransactions.size() == 1) {
			
			if (!force) {
				
				if (recurringTransactions.size() == 1) System.out.println(recurringTransactions.get(0).toString());
				else System.out.println(singleTransactions.get(0).toString());
				
				System.out.println("Are you sure? (Y/N)");
				Scanner scanner = new Scanner(System.in);
				String response = scanner.nextLine();
				scanner.close();
				
				if (!response.equals("y") && !response.equals("Y")) {
					
					System.out.println("Deletion cancelled");
					System.exit(0);
				}
				
			}
			
			String tuid = null;
			
			if (recurringTransactions.size() == 1) tuid = recurringTransactions.get(0).getTUID();
			else tuid = singleTransactions.get(0).getTUID();
			
			for (RecurringTransaction transaction : tm.getRecurringTransactions()) {

				if (transaction.getTUID().equals(tuid)) {
					tm.getRecurringTransactions().remove(transaction);
					break;
				}
				
			}
			for (SingleTransaction transaction : tm.getSingleTransactions()) {

				if (transaction.getTUID().equals(tuid)) {
					tm.getSingleTransactions().remove(transaction);
					break;
				}
				
			}
			
			tm.saveTransactions(DATALOC);
			
			System.out.println("Removed transaction\n");
	
		}
		else {
			
			System.out.println("There are multiple transactions with that name. Please pick below:");
			
			int i = 1;
			
			for (RecurringTransaction transaction : recurringTransactions) {
				
				System.out.println(i++ + ") " + transaction.toString());
			}
			for (SingleTransaction transaction : singleTransactions) {
				
				System.out.println(i++ + ") " + transaction.toString());
			}
			
			Scanner scanner = new Scanner(System.in);
			
			int choice = Integer.parseInt(scanner.nextLine());
			
			scanner.close();
			
			if (choice < 1 || choice > i) {
				
				System.out.println(choice + " is not a valid choice");
				System.exit(1);
			}
			
			i = 1;
			
			String targetTuid = null;
			
			for (RecurringTransaction transaction : recurringTransactions) {
				
				if (i++ == choice) targetTuid = transaction.getTUID();
			}
			for (SingleTransaction transaction : singleTransactions) {
				
				if (i++ == choice) targetTuid = transaction.getTUID();
			}
			
			for (RecurringTransaction transaction : tm.getRecurringTransactions()) {
				
				if (transaction.getTUID().equals(targetTuid)) tm.getRecurringTransactions().remove(transaction);
				break;
			}
			for (SingleTransaction transaction : tm.getSingleTransactions()) {
				
				if (transaction.getTUID().equals(targetTuid)) tm.getSingleTransactions().remove(transaction);
				break;
			}
			
			System.out.println("Removed transaction\n");
			
			tm.saveTransactions(DATALOC);
			
		}
		
	}
	
	private static void parseListCommand(String[] args) {

		if (args.length > 0 && (args[0].equals("-h") || args[0].equals("--help"))) {
			
			System.out.println("help info");
			System.exit(0);
			
		}
		
		TransactionManager tm = new TransactionManager();
		
		tm.loadTransactions(DATALOC);
		
		System.out.println("\n----Recurring transactions----");
		
		for (RecurringTransaction transaction : tm.getRecurringTransactions()) {
			
			System.out.println(transaction.toString());
			
		}
		
		System.out.println("\n----Single transactions----");
		
		for (SingleTransaction transaction : tm.getSingleTransactions()) {
			
			System.out.println(transaction.toString());
			
		}
		
		System.out.println();
		
	}
	
	private static void parseTransactionsCommand(String[] args) {
		
		LocalDate startDate = null;
		LocalDate endDate = null;

		if (args.length == 0) throw new IllegalArgumentException("Use --help or -h for help");
		
		if (args[0].equals("-h") || args[0].equals("--help")) {
			
			System.out.println("\nFITR - FinanceTracker transactions"
					+ "\n\nUsage:"
					+ "\n  transactions <y>          Get the transactions for a year y"
					+ "\n  transactions <d>          Get the transactions for a single day d"
					+ "\n  transactions <sd> <ed>    Get the transactions for a range of days within sd and ed inclusive"
					+ "\n\nOptions:"
					+ "\n  --help, -h                Show this help message"
					+ "\n");
			System.exit(0);
		}
		
		if (args.length == 1 && args[0].matches("[0-9]{4}")) {
			
			startDate = LocalDate.parse(args[0] + "-01-01");
			endDate = LocalDate.parse(args[0] + "-12-31");
			
		}
		else if (args.length == 1) startDate = endDate = LocalDate.parse(args[0]);
		
		else if (args.length == 2) {
			
			startDate = LocalDate.parse(args[0]);
			endDate = LocalDate.parse(args[1]);
		}
		
		System.out.println("\nTransactions for [" + startDate.toString() + " to " + endDate.toString() + "]\n");
		
		TransactionManager tm = new TransactionManager();
		
		tm.loadTransactions(DATALOC);
		
		Money totalIncome = new Money(0);
		Money totalExpense = new Money(0);
		
		LocalDate currentDate = startDate;
		
		while (!currentDate.isAfter(endDate)) {
			
			List<SingleTransaction> transactions = tm.transactionsOnDay(currentDate);
			
			for (SingleTransaction transaction : transactions) {
				
				System.out.println(transaction.toString());
				
				if (transaction.transactionDirection == TransactionDirection.INCOME) totalIncome = totalIncome.add(transaction.amount);
				else if (transaction.transactionDirection == TransactionDirection.EXPENSE) totalExpense = totalExpense.add(transaction.amount);
			
			}
			
			currentDate = currentDate.plusDays(1);
			
		}
		
		System.out.println("\n----TOTALS----");
		
		System.out.println("Income: " + totalIncome.toString());
		System.out.println("Expense: " + totalExpense.toString());
		
		Money totalMoney = totalIncome.subtract(totalExpense);
		
		if (totalMoney.getPence() > 0) System.out.println("Overall: +" + totalMoney.toString());
		else System.out.println("Overall: " + totalMoney.toString());
		
		System.out.println();
			
	}
	
	private static void parseClearCommand(String[] args) {
		
		System.out.println("\nThis will clear all recurring and single transactions.\nAre you sure? Type CLEAR to confirm");
		
		Scanner scanner = new Scanner(System.in);
		String response = scanner.nextLine();
		scanner.close();
		
		if (response.equals("CLEAR")) {
			
			TransactionManager tm = new TransactionManager();
			
			tm.loadTransactions(DATALOC);
			
			tm.getRecurringTransactions().clear();
			tm.getSingleTransactions().clear();
			
			tm.saveTransactions(DATALOC);
			
			System.out.println("All transactions cleared\n");
			
		}
		
	}
	
	private static void parseCategoriesCommand(String[] args) {
		
		if (args.length != 0 && args[0].equals("add")) {
			
			if (args.length == 1) {
				
				System.out.println("Provide a category name");
				System.exit(1);
			}
			
			String category = args[1];
			
			TransactionManager tm = new TransactionManager();
			tm.loadTransactions(DATALOC);
			
			if (tm.getTransactionCategories().contains(category)) {
				
				System.out.println("Category \"" + category + "\" already exists");
				System.exit(1);
			}
			
			tm.getTransactionCategories().add(category);
			
			tm.saveTransactions(DATALOC);
			
			System.out.println("Added new category \"" + category + "\"\n");
			
		}
		else {
			
			TransactionManager tm = new TransactionManager();
			tm.loadTransactions(DATALOC);
			
			System.out.println("\n----Categories----");
			
			for (String category : tm.getTransactionCategories()) {
				
				System.out.println(category);
			}
			
			System.out.println();
			
		}
		
	}
	
	private enum TransactionType {
		
		SINGLE,
		RECURRING
		
	}
	
}

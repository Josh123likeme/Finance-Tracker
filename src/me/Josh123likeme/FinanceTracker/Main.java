package me.Josh123likeme.FinanceTracker;

public class Main {
	
	public static void main(String[] args) {
		
		if (args.length != 0) CommandParser.parseCommand(args);
		
		//else do gui
		
	}
	/*
	private static void addTransactionCommand(String[] args) {
		
		TransactionType transactionType = null;
		String name = null;
		String description = null;
		TransactionDirection transactionDirection = null;
		Long amount = null;
		PeriodType periodType = null;
		LocalDate dateOfTransaction = null;
		LocalDate startDate = null;
		LocalDate endDate = null;
		Integer interval = null;
		
		for (int i = 1; i < args.length; i++) {
			
			switch (args[i]) {
			
			case "--recur":
				transactionType = TransactionType.RECURRING;
				break;
			case "--single":
				transactionType = TransactionType.SINGLE;
				break;
			case "--name":
				name = args[++i];
				break;
			case "--desc":
				description = args[++i];
				break;
			case "--inc":
				transactionDirection = TransactionDirection.INCOME;
				break;
			case "--exp":
				transactionDirection = TransactionDirection.EXPENSE;
				break;
			case "--amt":
				amount = Long.parseLong(args[++i]);
				break;
			case "--pt":
				periodType = Enum.valueOf(PeriodType.class, args[++i]);
				break;
			case "--d":
				dateOfTransaction = LocalDate.parse(args[++i]);
				break;
			case "--sd":
				startDate = LocalDate.parse(args[++i]);
				break;
			case "--ed":
				endDate = LocalDate.parse(args[++i]);
				break;
			case "--i":
				interval = Integer.parseInt(args[++i]);
				break;
				
			}
			
		}
		
		if (transactionType == null) throw new IllegalArgumentException("Provide the transaction type with \"--recur\" or \"--single\"");
		if (name == null) throw new IllegalArgumentException("Provide the name with \"--name\"");
		if (description == null) throw new IllegalArgumentException("Provide the description with \"--desc\"");
		if (transactionDirection == null) throw new IllegalArgumentException("Provide the transaction direction with \"--inc\" or \"--exp\"");
		if (amount == null) throw new IllegalArgumentException("Provide the amount with \"--amt\"");
		
		if (transactionType == TransactionType.RECURRING) {
			
			if (periodType == null) throw new IllegalArgumentException("Provide the period type with \"--pt\"");
			if (startDate == null) throw new IllegalArgumentException("Provide the start date with \"--sd\"");
			
			if ((periodType == PeriodType.N_WEEKLY || periodType == PeriodType.N_MONTHLY) && interval == null) 
				throw new IllegalArgumentException("Provide the interval with \"--i\"");
			
		}
		if (transactionType == TransactionType.SINGLE) {
			
			if (dateOfTransaction == null) throw new IllegalArgumentException("Provide the date of transaction with \"--d\"");
		
		}
		
		TransactionManager tm = new TransactionManager();
		
		if (Files.exists(Path.of("transactions.json"))) tm.loadTransactions(Path.of("transactions.json"));
		
		if (transactionType == TransactionType.RECURRING) tm.addRecurringTransaction(name, description, transactionDirection, amount, periodType, startDate, endDate, interval);
		
		tm.saveTransactions(Path.of("transactions.json"));
		
	}
	*/
}

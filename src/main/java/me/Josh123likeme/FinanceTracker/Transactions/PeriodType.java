package me.Josh123likeme.FinanceTracker.Transactions;

public enum PeriodType {
	
	DAILY(false, "day"),
	WEEKLY(false, "week"),
	N_WEEKLY(true, "weeks"),
	MONTHLY(false, "month"),
	N_MONTHLY(true, "months"),
	YEARLY(false, "year")
	
	;
	
	public boolean requiresInterval;
	public String niceName;
	
	PeriodType(boolean requiresInterval, String niceName) {
		
		this.requiresInterval = requiresInterval;
		this.niceName = niceName;
		
	}
	
}
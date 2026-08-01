package me.Josh123likeme.FinanceTracker.Transactions;

public class Money {
	
	private final long pence;
	
	public Money(long pence) {
		
		this.pence = pence;
		
	}
	
	public Money(long pounds, long pence) {
		
		this.pence = pounds * 100 + pence;
	}
	
	public Money add(Money money) {
		
		return new Money(this.pence + money.pence);	
	}
	
	public Money subtract(Money money) {
		
		return new Money(this.pence - money.pence);
	}
	
	public long getPence() {
		
		return pence;
	}
	public long getPounds() {
		
		return pence / 100;
	}
	public long getRemainingPence() {
		
		return pence % 100;
	}
	
	public String toString() {
		
		if (pence >= 0) return "£" + getPounds() + "." + String.format("%02d", getRemainingPence());
		
		return "-£" + -getPounds() + "." + String.format("%02d", -getRemainingPence());
		
	}
	
	public static Money parse(String in) {
		
		if (in == null) throw new IllegalArgumentException("\"in\" cannot be null");
		
		String[] parts = in.split("\\.");
		
		//no decimal point, so must be pence
		if (parts.length == 1) return new Money(Long.parseLong(parts[0]));
		
		if (parts.length == 2) return new Money(Long.parseLong(parts[0]), Long.parseLong(parts[1]));
		
		throw new IllegalArgumentException("\"" + in + "\" not in form \"£\" OR \"£.pp\"");
		
	}
	
}

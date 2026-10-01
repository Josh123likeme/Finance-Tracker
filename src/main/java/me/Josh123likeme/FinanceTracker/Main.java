package me.Josh123likeme.FinanceTracker;

public class Main {
	
	public static void main(String[] args) {
		
		if (args.length != 0) CommandParser.parseCommand(args);
		
	}
	
}

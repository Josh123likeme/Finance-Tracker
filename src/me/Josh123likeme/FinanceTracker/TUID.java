package me.Josh123likeme.FinanceTracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TUID {
	
	private static Random random = new Random();
	
	private static List<Long> usedValues = new ArrayList<Long>();
	private static long lastKnownEpoch = 0;
	
	//generates a unique timestamped hex string that is 32 characters in length
	public static String generateTimestampedUniqueIdentifier() {
		
		long thisEpoch = System.currentTimeMillis();
		
		//next epoch, so flush usedValues
		if (thisEpoch != lastKnownEpoch) usedValues.clear();
		
		long randomValue;
		
		do {
			randomValue = random.nextLong();
		}
		while (usedValues.contains(randomValue));
		
		String tuid = String.format("%016X", randomValue) + String.format("%016X", thisEpoch);
		
		return tuid;
		
	}
	
}

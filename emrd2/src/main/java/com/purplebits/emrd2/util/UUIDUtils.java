package com.purplebits.emrd2.util;

import java.util.UUID;

public class UUIDUtils {

	private static final String UUIDRegex = "[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}";

	public static String generateUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	public static boolean isValidUUID(String uuid) {

		if (uuid.matches(UUIDRegex)) {
			return true;
		}
		return false;
	}

	public static long compareTwoTimeStamps(java.sql.Timestamp currentTime, java.sql.Timestamp oldTime) {
		long milliseconds1 = oldTime.getTime();
		long milliseconds2 = currentTime.getTime();

		long diff = milliseconds2 - milliseconds1;
		// long diffSeconds = diff / 1000;
		long diffMinutes = diff / (60 * 1000);
		// long diffHours = diff / (60 * 60 * 1000);
		// long diffDays = diff / (24 * 60 * 60 * 1000);

		return diffMinutes;
	}
	

}

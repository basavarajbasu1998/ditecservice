package com.ta.ditec.services.utils;

import java.util.Random;

public class APIUtiles {

	public static String generateAPIkey() {
		String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
		String numbers = "1234567890";
		String combinedChars = capitalCaseLetters + lowerCaseLetters + numbers;
		Random random = new Random();
		int length = 16; // Change length to 15 characters
		char[] password = new char[length];
		password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
		password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
		password[2] = numbers.charAt(random.nextInt(numbers.length()));
		for (int i = 3; i < length; i++) {
			password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
		}
		return new String(password);
	}

}

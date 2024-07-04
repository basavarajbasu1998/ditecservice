package com.ta.ditec.services.password;

import java.util.Random;

public class Password {

	public static String generatePassword() {

		String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
		String specialCharacters = "!@#$";
		String numbers = "1234567890";
		String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
		Random random = new Random();
		int length = 10;
		char[] password1 = new char[length];

		password1[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
		password1[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
		password1[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
		password1[3] = numbers.charAt(random.nextInt(numbers.length()));

		for (int i = 4; i < length; i++) {
			password1[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
		}
		return new String(password1);
	}
	public static void main(String[] args) {
		System.out.println();
	}

}

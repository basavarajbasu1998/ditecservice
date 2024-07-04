package com.ta.ditec.services.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TAUtils {

	public static String getId(Long id) {
		long idval = (long) id;
		if (idval < 10)
			return "000" + idval;
		else if (idval < 100)
			return "00" + idval;
		else if (idval < 1000)
			return "0" + idval;
		else
			return String.valueOf(idval);
	}

	public static String dateformat(String format) {
		SimpleDateFormat sd = new SimpleDateFormat(format);
		return sd.format(new Date());
	}

	public static String dateformat(String string, Date createdDate) {
		SimpleDateFormat sd = new SimpleDateFormat(string);
		return sd.format(createdDate);
	}

	// =============================================================================================//

	private static final String[] units = { "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight",
			"Nine" };
	private static final String[] teens = { "", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen",
			"Seventeen", "Eighteen", "Nineteen" };
	private static final String[] tens = { "", "Ten", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy",
			"Eighty", "Ninety" };

//public static String convert(BigDecimal amount) {
//if (amount.compareTo(BigDecimal.ZERO) == 0) {
//	return "Zero Cents";
//}
//
//String[] parts = amount.toPlainString().split("\\.");
//String wholePart = parts[0];
//String decimalPart = parts.length > 1 ? parts[1] : "00";
//
//String amountInWords = convertToWords(wholePart) + " Cents and " + convertCents(decimalPart) + " Cents";
//return amountInWords;
//}

	public static String convert(BigDecimal amount) {
		if (amount.compareTo(BigDecimal.ZERO) == 0) {
			return "Zero Rupees";
		}

		String[] parts = amount.toPlainString().split("\\.");
		String wholePart = parts[0];
		String decimalPart = parts.length > 1 ? parts[1] : "00";

		String amountInWords = convertToWords(wholePart);
		if (!wholePart.equals("1")) {
			amountInWords += " Rupees";
		} else {
			amountInWords += " Rupee";
		}
		return amountInWords;
	}

	private static String convertToWords(String num) {
		int n = num.length();
		if (n <= 0) {
			return "";
		} else if (n == 1) {
			return units[Integer.parseInt(num)];
		} else if (n == 2) {
			int twoDigitNum = Integer.parseInt(num);
			if (twoDigitNum == 10) {
				return "Ten";
			} else if (twoDigitNum >= 11 && twoDigitNum <= 19) {
				return teens[twoDigitNum - 10];
			} else {
				int tensDigit = twoDigitNum / 10;
				int unitsDigit = twoDigitNum % 10;
				return tens[tensDigit] + (unitsDigit != 0 ? " " + units[unitsDigit] : "");
			}
		} else if (n <= 3) {
			int hundreds = Integer.parseInt(num.substring(0, n - 2));
			return convertLessThanHundred(hundreds) + " Hundred " + convertToWords(num.substring(n - 2));
		} else if (n <= 5) {
			int thousands = Integer.parseInt(num.substring(0, n - 3));
			return convertToWords(thousands + "") + " Thousand " + convertToWords(num.substring(n - 3));
		} else if (n <= 7) {
			int lakhs = Integer.parseInt(num.substring(0, n - 5));
			return convertToWords(lakhs + "") + " Lakh " + convertToWords(num.substring(n - 5));
		} else {
			return "Number out of range";
		}
	}

	private static String convertCents(String cents) {
		if (cents.length() > 2) {
			cents = cents.substring(0, 2);
		}
		return convertLessThanHundred(Integer.parseInt(cents));
	}

	private static String convertLessThanHundred(int num) {
		if (num < 10) {
			return units[num];
		} else if (num < 20) {
			return teens[num - 10];
		} else {
			return tens[num / 10] + ((num % 10 != 0) ? " " + units[num % 10] : "");
		}
	}

	public static void main(String[] args) {
		BigDecimal amount = new BigDecimal("710.36");
//		String amountInWords = TAUtiles.convert(amount);
//		System.out.println(amountInWords);
	}

	//

	public static String generateInvoiceId(Long id) {
		String lastDigits = getLastDigitsFromCurrentDate();
		String paddedId = padId(id);
		return paddedId + lastDigits;
	}

	private static String getLastDigitsFromCurrentDate() {
		SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
		SimpleDateFormat yearFormat = new SimpleDateFormat("yy");
		String month = monthFormat.format(new Date());
		String year = yearFormat.format(new Date());
		return year + month;
	}

	private static String padId(Long id) {
		long idValue = (long) id;
		if (idValue < 10)
			return "000" + idValue;
		else if (idValue < 100)
			return "00" + idValue;
		else if (idValue < 1000)
			return "0" + idValue;
		else
			return String.valueOf(idValue);
	}

	public static final String PREFIX = "#DSA";

	public static String getMerchantId(Long id) {
		return PREFIX + formatId(id);
	}
	private static String formatId(Long id) {
		long idVal = id;
		if (idVal < 10)
			return "000" + idVal;
		else if (idVal < 100)
			return "00" + idVal;
		else if (idVal < 1000)
			return "0" + idVal;
		else
			return String.valueOf(idVal);
	}

}

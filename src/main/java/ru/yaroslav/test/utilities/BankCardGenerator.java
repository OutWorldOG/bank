package ru.yaroslav.test.utilities;

import java.util.Random;

public class BankCardGenerator {

    public static String generateUniqueBankCardNumber() {
        String cardNumber;
        do {
            cardNumber = generateBankCardNumber();
        } while (!isValidCardNumber(cardNumber));
        return cardNumber;
    }

    private static String generateBankCardNumber() {
        Random random = new Random();

        return "4" +
                String.format("%06d", random.nextInt(999999)) +
                String.format("%09d", random.nextInt(999999999));
    }

    private static boolean isValidCardNumber(String cardNumber) {
        int sum = 0;

        for (int i = cardNumber.length() - 2; i >= 0; i -= 2) {
            int digit = Character.getNumericValue(cardNumber.charAt(i)) * 2;
            sum += digit / 10 + digit % 10;
        }

        for (int i = cardNumber.length() - 1; i >= 0; i -= 2) {
            sum += Character.getNumericValue(cardNumber.charAt(i));
        }
        return sum % 10 == 0;
    }
}
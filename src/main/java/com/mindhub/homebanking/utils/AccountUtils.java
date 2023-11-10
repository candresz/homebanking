package com.mindhub.homebanking.utils;

public final class AccountUtils {
    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }


    public static String generateAccountNumber(){
       int accountNumber = getRandomNumber(1, 99999999);
      return String.valueOf(accountNumber);
    }
}

package org.example;

import java.util.ArrayList;
import java.util.List;


public class BankingSystem {
    public static void main(String[] args) {
        // Skapa en lista av konton
        List<Account> accounts = new ArrayList<>();

        // Lägg till olika typer av konton
        accounts.add(new SavingsAccount("S123", "Alice", 1000, 0.05));
        accounts.add(new CheckingAccount("C456", "Bob", 2000, 1.5));
        accounts.add(new FixedDepositAccount("F789", "Charlie", 5000, 12));

        // Bearbeta kontolistan
        processAccounts(accounts);
    }

    public static void processAccounts(List<Account> accounts) {
        for (Account acc : accounts) {
            System.out.println(acc);  // skriv ut kontoinfo

            if (acc instanceof SavingsAccount sa) {
                sa.addInterest();  // lägg till ränta
            }

            if (acc instanceof CheckingAccount ca) {
                ca.withdraw(50);  // gör ett uttag på 50 + avgift
            }

            if (acc instanceof FixedDepositAccount fda) {
                try {
                    fda.withdraw(1000);
                } catch (IllegalStateException e) {
                    System.out.println("Uttag nekades: " + e.getMessage());
                }
            }

        }
    }

}

package org.example;

public class Main {
    public static void main(String[] args) {

        SavingsAccount sa = new SavingsAccount("SA123", "Anna", 1000, 0.05);
        sa.deposit(500);
        sa.addInterest();
        System.out.println(sa);


    }
}
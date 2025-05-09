package org.example;

public class CheckingAccount extends Account {
    private double transactionFee;

    public CheckingAccount(String accountNumber, String ownerName, double initialBalance, double transactionFee) {
        super(accountNumber, ownerName, initialBalance);
        this.transactionFee = transactionFee;
    }

    @Override
    public void withdraw(double amount) {
        double total = amount + transactionFee;

        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (total > balance) {
            throw new IllegalArgumentException("Insufficient funds including transaction fee");
        }

        balance -= total;
        System.out.println(amount + " withdrawn + " + transactionFee + " fee. New balance: " + balance);
    }

    public double getTransactionFee() {
        return transactionFee;
    }

    public void setTransactionFee(double transactionFee) {
        this.transactionFee = transactionFee;
    }

    @Override
    public String toString() {
        return super.toString() + ", transactionFee=" + transactionFee;
    }
}

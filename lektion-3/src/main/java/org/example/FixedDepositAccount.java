package org.example;

import java.time.LocalDate;

public class FixedDepositAccount extends Account {
    private int lockPeriodMonths;
    private LocalDate depositDate;

    public FixedDepositAccount(String accountNumber, String ownerName, double initialBalance, int lockPeriodMonths) {
        super(accountNumber, ownerName, initialBalance);
        this.lockPeriodMonths = lockPeriodMonths;
        this.depositDate = LocalDate.now(); // sätts vid skapande
    }

    @Override
    public void withdraw(double amount) {
        LocalDate unlockDate = depositDate.plusMonths(lockPeriodMonths);
        if (LocalDate.now().isBefore(unlockDate)) {
            throw new IllegalStateException("Cannot withdraw before lock period ends: " + unlockDate);
        }
        super.withdraw(amount); // anropa Account's withdraw()
    }

    public int getLockPeriodMonths() {
        return lockPeriodMonths;
    }

    public LocalDate getDepositDate() {
        return depositDate;
    }

    @Override
    public String toString() {
        return super.toString() + ", lockPeriodMonths=" + lockPeriodMonths + ", depositDate=" + depositDate;
    }
}

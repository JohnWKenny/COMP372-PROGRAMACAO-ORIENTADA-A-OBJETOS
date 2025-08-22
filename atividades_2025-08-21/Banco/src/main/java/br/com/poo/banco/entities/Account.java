package br.com.poo.banco.entities;

import br.com.poo.banco.exceptions.WithdrawLimitNegativeException;
import br.com.poo.banco.exceptions.BalanceNegativeException;
import br.com.poo.banco.exceptions.BalanceEnoughException;
import br.com.poo.banco.exceptions.WithdrawLimitException;

/**
 *
 * @author John
 */
public class Account {
    private int number;
    private String holder;
    private double balance;
    private double withdrawLimit;

    public Account(int number, String holder, double balance, double withdrawLimit) 
                throws BalanceNegativeException, WithdrawLimitNegativeException {
        this.setNumber(number);
        this.setHolder(holder);
        this.setBalance(balance);
        this.setWithdrawLimit(withdrawLimit);
    }
    
    // Getters e Setters
    public int getNumber() {
        return number;
    }

    private void setNumber(int number) {
        this.number = number;
    }

    public String getHolder() {
        return holder;
    }

    private void setHolder(String holder) {
        this.holder = holder;
    }

    public double getBalance() {
        return balance;
    }

    private void setBalance(double balance) throws BalanceNegativeException {
        if(balance < 0) {
            throw new BalanceNegativeException(
                "Balance error: Negative numbers are not allowed in balance"
            );
        }

        this.balance = balance;
        
    }

    public double getWithdrawLimit() {
        return withdrawLimit;
    }

    private void setWithdrawLimit(double withdrawLimit) throws WithdrawLimitNegativeException {
        if(withdrawLimit < 0) {
            throw new WithdrawLimitNegativeException(
                "Withdraw error: Negative numbers are not allowed in withdraw limit"
            );
        }

        this.withdrawLimit = withdrawLimit;
 
    }
    
    // UML
    public void deposit(double amount) throws BalanceNegativeException {
        this.setBalance(this.getBalance() + amount);
        System.out.println("New balance: " + this.getBalance());
    }
    
    public void withdraw(double amount) throws BalanceNegativeException {
        try {
            if(this.getWithdrawLimit() < amount) {
                throw new WithdrawLimitException(
                        "Withdraw error: The amount exceeds withdraw limit"
                );
            }
            
            if(this.getBalance() < amount) {
                throw new BalanceEnoughException(
                        "Withdraw error: Not enough balance"
                );
            }
            this.setBalance(this.getBalance() - amount);
            System.out.println("New balance: " + this.getBalance());
            
        } catch(WithdrawLimitException | BalanceEnoughException e) {
            System.out.println(e.getMessage());
        }
    }
}

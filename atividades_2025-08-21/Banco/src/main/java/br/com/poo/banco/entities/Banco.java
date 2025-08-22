package br.com.poo.banco.entities;

import br.com.poo.banco.exceptions.BalanceNegativeException;
import br.com.poo.banco.exceptions.WithdrawLimitNegativeException;
import java.util.Scanner;

/**
 *
 * @author John
 */
public class Banco {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        try {
            System.out.println("Enter account data");
            System.out.print("Number: ");
            int number = sc.nextInt();
            sc.nextLine();
            
            System.out.print("Holder: ");
            String holder = sc.nextLine();
            
            System.out.print("Initial balance: ");
            double balance = sc.nextDouble();
            
            System.out.print("Withdraw limit: ");
            double withdrawLimit = sc.nextDouble();
            
            Account account = new Account(number, holder, balance, withdrawLimit);
            
            System.out.print("\nEnter amount for withdraw: ");
            double amount = sc.nextDouble();

            account.withdraw(amount);
            
            
        } catch (BalanceNegativeException | WithdrawLimitNegativeException e) {
            System.out.println(e.getMessage());
        } finally {
            sc.close();
        }
    }
}

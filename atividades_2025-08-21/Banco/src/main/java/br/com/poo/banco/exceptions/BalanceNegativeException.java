package br.com.poo.banco.exceptions;

/**
 *
 * @author John
 */
public class BalanceNegativeException extends Exception {

    public BalanceNegativeException(String msg) {
        super(msg);
    }
    
}

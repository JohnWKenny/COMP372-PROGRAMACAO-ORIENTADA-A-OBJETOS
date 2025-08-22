package br.com.poo.banco.exceptions;

/**
 *
 * @author John
 */
public class WithdrawLimitNegativeException extends Exception {

    public WithdrawLimitNegativeException(String msg) {
        super(msg);
    }
    
}

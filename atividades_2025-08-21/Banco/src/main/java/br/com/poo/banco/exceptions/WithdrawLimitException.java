package br.com.poo.banco.exceptions;

/**
 *
 * @author John
 */
public class WithdrawLimitException extends Exception {

    public WithdrawLimitException(String msg) {
        super(msg);
    }
    
}

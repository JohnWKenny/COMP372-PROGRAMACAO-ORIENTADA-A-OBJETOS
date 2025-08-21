package br.com.poo.hotel.exceptions;

/**
 *
 * @author John
 */
public class CheckinAfterCheckoutException extends Exception {

    public CheckinAfterCheckoutException(String msg) {
        super(msg);
    }
    
}

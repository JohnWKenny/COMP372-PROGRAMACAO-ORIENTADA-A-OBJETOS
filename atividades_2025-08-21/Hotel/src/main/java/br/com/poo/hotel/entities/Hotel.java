package br.com.poo.hotel.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

/**
 *
 * @author John
 */
public class Hotel {
    
    public static void main(String[] args) throws ParseException {
        int roomNumber;
        String checkin, checkout;
        Scanner sc = new Scanner(System.in);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy"); 
        System.out.print("Room number: ");
        roomNumber = sc.nextInt();
        sc.nextLine();
        System.out.print("Check-in date (dd/MM/yyyy): ");
        checkin = sc.nextLine();
        System.out.print("Check-out date (dd/MM/yyyy): ");
        checkout = sc.nextLine();

        Reservation hotel = new Reservation(roomNumber, format.parse(checkin), format.parse(checkout));
        if(hotel.getCheckin() != null && hotel.getCheckout() != null) {
            System.out.println("\nEnter data to update the reservation: ");
            System.out.print("Check-in date (dd/MM/yyyy): ");
            checkin = sc.nextLine();
            System.out.print("Check-out date (dd/MM/yyyy): ");
            checkout = sc.nextLine();

            hotel.updateDates(format.parse(checkin), format.parse(checkout));
        }
    }
}

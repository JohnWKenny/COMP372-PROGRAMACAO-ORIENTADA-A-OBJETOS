package br.com.poo.hotel.entities;

import br.com.poo.hotel.exceptions.CheckinAfterCheckoutException;
import br.com.poo.hotel.exceptions.DatePastException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author John
 */
public class Reservation {
    private int roomNumber;
    private Date checkin;
    private Date checkout;

    public Reservation(int roomNumber, Date checkin, Date checkout) {
        this.setRoomNumber(roomNumber);
        this.updateDates(checkin, checkout);
    }

    // Getters e Setters
    public int getRoomNumber() {
        return roomNumber;
    }

    public final void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Date getCheckin() {
        return checkin;
    }

    public final void setCheckin(Date checkin) throws DatePastException {
        // Data atual exata.
        LocalDate dt = LocalDate.now();
        // Convertendo checkin para data exata.
        Instant instant = checkin.toInstant();
        ZoneId fuso = ZoneId.systemDefault();
        LocalDate checkinNow = instant.atZone(fuso).toLocalDate();

        if(checkinNow.isBefore(dt)) {
            throw new DatePastException(
                    "Error in reservation: Reservation dates for update must be future dates."
            );
        } else {
            this.checkin = checkin;
        }
    }

    public Date getCheckout() {
        return checkout;
    }

    public final void setCheckout(Date checkout) throws CheckinAfterCheckoutException{
        if(checkout.before(this.getCheckin())) {
            throw new CheckinAfterCheckoutException(
                    "Error in reservation: Check-out date must be after check-in date"
            );
        } else {
            this.checkout = checkout;
        } 
    }
    
    // UML
    public int duration() {
        int days = (int) TimeUnit.MILLISECONDS.toDays(this.getCheckout().getTime() - 
                         this.getCheckin().getTime());
        return days;
    }
    
    public final void updateDates(Date checkin, Date checkout){
        try {
            this.setCheckin(checkin);
            this.setCheckout(checkout);
            this.printReservation();
        } catch(CheckinAfterCheckoutException | DatePastException e) {
            System.out.println(e.getMessage());
        }
    }
    
    // Metodo extra para printar saida
    public void printReservation(){
        // Convertendo Date para formato dd/MM/yyyy
        DateTimeFormatter formatEntry = DateTimeFormatter.ofPattern(
                "E MMM dd HH:mm:ss z yyyy", Locale.US
        );
        
        DateTimeFormatter formatOut = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        ZonedDateTime dateCheckin = ZonedDateTime.parse(this.getCheckin().toString(), formatEntry);
        ZonedDateTime dateCheckout = ZonedDateTime.parse(this.getCheckout().toString(), formatEntry);

        String checkin = dateCheckin.format(formatOut);
        String checkout = dateCheckout.format(formatOut);

        System.out.println("Reservation: " + this.getRoomNumber() + 
                           ", check-in: " + checkin + 
                           ", check-out: " + checkout + 
                           ", " + this.duration() + " nights");
    }
}

package com.tauman.ticketservice.service;

import com.tauman.ticketservice.model.SeatHold;
import com.tauman.ticketservice.model.SeatReservation;
import com.tauman.ticketservice.venue.Venue;
import java.util.Date;

/**
 *
 * @author Steven Reich
 */
public class TicketServiceImpl implements TicketService {

    private final Venue venue;
    private final int holdTimeSeconds;
    
    public TicketServiceImpl(Venue venue, int holdTimeSeconds) {
        this.venue = venue;
        this.holdTimeSeconds = holdTimeSeconds;
    }

    @Override
    public int numSeatsAvailable() {
        return venue.numSeatsAvailable();
    }

    @Override
    public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
        Date expirationTime = computeExpiration(this.holdTimeSeconds);
        return venue.holdSeats(numSeats, expirationTime, customerEmail);
    }

    @Override
    public String reserveSeats(int seatHoldId, String customerEmail) {
        SeatReservation seatReservation = venue.reserveSeats(seatHoldId, customerEmail);
        
        if(seatReservation == null) {
            return null;
        }
        
        return seatReservation.getConfirmationCode();
    }

    
    public static Date computeExpiration(int holdTimeSeconds) {
        return new Date(new Date().getTime() + 1000 * holdTimeSeconds);
    }
}

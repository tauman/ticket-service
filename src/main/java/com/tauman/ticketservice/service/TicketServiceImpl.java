package com.tauman.ticketservice.service;

import com.tauman.ticketservice.model.SeatHold;
import com.tauman.ticketservice.model.SeatReservation;
import com.tauman.ticketservice.venue.Venue;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Steven Reich
 */
public class TicketServiceImpl implements TicketService {
    final static Logger LOGGER = LogManager.getLogger("ticket-service");

    private final Venue venue;
    private final int holdTimeSeconds;
    
    public TicketServiceImpl(Venue venue, int holdTimeSeconds) {
        LOGGER.debug("TicketServiceImpl.<init>() holdTimeSeconds={}", holdTimeSeconds);
        this.venue = venue;
        this.holdTimeSeconds = holdTimeSeconds;
    }

    @Override
    public int numSeatsAvailable() {
        LOGGER.debug("TicketServiceImpl.numSeatsAvailable()");
        int available = venue.numSeatsAvailable();
        
        LOGGER.debug("TicketServiceImpl.numSeatsAvailable() availabe={}", available);
        return available;
    }

    @Override
    public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
        LOGGER.debug("TicketServiceImpl.findAndHoldSeats() numSeats={} customerEmail={}", numSeats, customerEmail);
        Date expirationTime = computeExpiration(this.holdTimeSeconds);
        SeatHold seatHold = venue.holdSeats(numSeats, expirationTime, customerEmail);
        
        LOGGER.debug("TicketServiceImpl.findAndHoldSeats() numSeats={} expirationTime={}, customerEmail={} seatHold={}", numSeats, expirationTime, customerEmail, seatHold);
        return seatHold;
    }

    @Override
    public String reserveSeats(int seatHoldId, String customerEmail) {
        LOGGER.debug("TicketServiceImpl.reserveSeats() seatHoldId={} customerEmail={}", seatHoldId, customerEmail);
        SeatReservation seatReservation = venue.reserveSeats(seatHoldId, customerEmail);
        
        if(seatReservation == null) {
            return null;
        }
        
        LOGGER.debug("TicketServiceImpl.reserveSeats() seatHoldId={} customerEmail={} seatReservation={}", seatHoldId, customerEmail, seatReservation);
        return seatReservation.getConfirmationCode();
    }

    
    public static Date computeExpiration(int holdTimeSeconds) {
        return new Date(new Date().getTime() + 1000 * holdTimeSeconds);
    }
}

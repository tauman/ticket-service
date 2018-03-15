package com.tauman.ticketservice.model;

import java.util.Arrays;
import java.util.HashSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Steven Reich
 */
public class SeatReservationTest {
    @Test
    public void testInitialization() {
        String code = "conf#123";
        String email = "test@nowhere.com";
        Seat s1 = new Seat("Seat 1");
        Seat s2 = new Seat("Seat 2");
        
        HashSet<Seat> seats = new HashSet<>(Arrays.asList(s1, s2));
        SeatReservation seatReservation = new SeatReservation(code, email, seats);
        
        assertEquals(code, seatReservation.getConfirmationCode());
        assertEquals(email, seatReservation.getCustomerEmail());
        
        assertEquals(seats.size(), seatReservation.getSeats().size());
        assertTrue(seatReservation.getSeats().contains(s1));
        assertTrue(seatReservation.getSeats().contains(s2));
    }
}

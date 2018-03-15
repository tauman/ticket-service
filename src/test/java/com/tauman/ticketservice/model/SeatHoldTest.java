package com.tauman.ticketservice.model;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Steven Reich
 */
public class SeatHoldTest {
    @Test 
    public void testInitialization() {
        int id = 1;
        Date expiration = new Date();
        String email = "test@nowhere.com";
        Seat s1 = new Seat("Seat 1");
        Seat s2 = new Seat("Seat 2");
        
        HashSet<Seat> seats = new HashSet<>(Arrays.asList(s1, s2));
        
        SeatHold seatHold = new SeatHold(1, expiration, email, seats);
        
        assertEquals(id, seatHold.getId());
        assertEquals(expiration, seatHold.getExpirationTime());
        assertEquals(email, seatHold.getCustomerEmail());
        
        assertEquals(seats.size(), seatHold.getSeats().size());
        assertTrue(seatHold.getSeats().contains(s1));
        assertTrue(seatHold.getSeats().contains(s2));
    }
    
    @Test 
    public void testSeatHoldExpiration() {
        int id = 1;
        Date expiration = new Date(new Date().getTime() - 1000);
        String email = "test@nowhere.com";
        Seat s1 = new Seat("Seat 1");
        Seat s2 = new Seat("Seat 2");
        
        HashSet<Seat> seats = new HashSet<>(Arrays.asList(s1, s2));
        
        SeatHold seatHold = new SeatHold(1, expiration, email, seats);
        assertTrue(seatHold.isExpired());
    }
}

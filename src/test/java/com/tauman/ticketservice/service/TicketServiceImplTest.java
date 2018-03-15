package com.tauman.ticketservice.service;

import com.tauman.ticketservice.model.SeatHold;
import com.tauman.ticketservice.venue.Venue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * Note, these are really integration testS and should be a separate suite for
 * various reasons (especially speed, resources, and setup), but for this case,
 * they is lumped in with the unit tests.
 *
 * @author Steven Reich
 */
public class TicketServiceImplTest {

    @Test
    public void testHoldSeatsAndReserve() {
        Venue venue = new Venue(2, 5);
        TicketServiceImpl ticketService = new TicketServiceImpl(venue, 1);

        SeatHold seatHold = ticketService.findAndHoldSeats(4, "one@nowhere.com");
        assertNotNull(seatHold);
        assertEquals(6, ticketService.numSeatsAvailable());

        String confirmationCode = ticketService.reserveSeats(seatHold.getId(), "one@nowhere.com");
        assertNotNull(confirmationCode);
    }

    @Test
    public void testHoldSeatsTimeoutAndReserve() {
        Venue venue = new Venue(2, 5);
        TicketServiceImpl ticketService = new TicketServiceImpl(venue, 1);

        SeatHold seatHold = ticketService.findAndHoldSeats(4, "one@nowhere.com");
        assertNotNull(seatHold);
        assertEquals(6, ticketService.numSeatsAvailable());

        sleep(2000);
        String confirmationCode = ticketService.reserveSeats(seatHold.getId(), "one@nowhere.com");
        assertNull(confirmationCode);
        assertEquals(10, ticketService.numSeatsAvailable());
    }

    @Test
    public void testHoldAndReserveWithTimeoutAfter() {
        Venue venue = new Venue(2, 5);
        TicketServiceImpl ticketService = new TicketServiceImpl(venue, 1);

        SeatHold seatHold1 = ticketService.findAndHoldSeats(4, "one@nowhere.com");
        assertNotNull(seatHold1);
        assertEquals(6, ticketService.numSeatsAvailable());

        SeatHold seatHold2 = ticketService.findAndHoldSeats(3, "one@nowhere.com");
        assertNotNull(seatHold2);
        assertEquals(3, ticketService.numSeatsAvailable());

        String confirmationCode1 = ticketService.reserveSeats(seatHold1.getId(), "one@nowhere.com");
        assertNotNull(confirmationCode1);
        assertEquals(3, ticketService.numSeatsAvailable());

        sleep(2000);
        String confirmationCode2 = ticketService.reserveSeats(seatHold2.getId(), "one@nowhere.com");
        assertNull(confirmationCode2);
        assertEquals(6, ticketService.numSeatsAvailable());
    }

    @Test
    public void testInadequateSeats1() {
        Venue venue = new Venue(2, 5);
        TicketServiceImpl ticketService = new TicketServiceImpl(venue, 1);

        ticketService.findAndHoldSeats(4, "one@nowhere.com");
        ticketService.findAndHoldSeats(4, "two@nowhere.com");

        assertEquals(2, ticketService.numSeatsAvailable());

        SeatHold seatHold = ticketService.findAndHoldSeats(4, "three@nowhere.com");
        assertNull(seatHold);
    }

    @Test
    public void testInadequateSeats2() {
        Venue venue = new Venue(2, 5);
        TicketServiceImpl ticketService = new TicketServiceImpl(venue, 1);

        ticketService.findAndHoldSeats(4, "one@nowhere.com");
        SeatHold seatHold2 = ticketService.findAndHoldSeats(4, "two@nowhere.com");
        String confirmationCode = ticketService.reserveSeats(seatHold2.getId(), "two@nowhere.com");
        assertNotNull(confirmationCode);

        assertEquals(2, ticketService.numSeatsAvailable());

        SeatHold seatHold3 = ticketService.findAndHoldSeats(4, "three@nowhere.com");
        assertNull(seatHold3);
        assertEquals(2, ticketService.numSeatsAvailable());
    }

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            throw new RuntimeException("Thread sleep interrupted", ex);
        }
    }
}


/*
1. Hold Seats, then Reserve them

2. Hold Seats, timeout, try to reserve them

3. Hold Seats, hold more seats, reserve second set, timeout first set,
   Hold more seats, reserve them
 */

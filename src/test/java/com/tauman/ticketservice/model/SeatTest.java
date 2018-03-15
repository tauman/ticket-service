package com.tauman.ticketservice.model;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Steven Reich
 */
public class SeatTest {
    @Test
    public void testInitialization() {
        String label = "R1-S3";
        Seat seat = new Seat(label);
        assertEquals(label, seat.label);
    }
}

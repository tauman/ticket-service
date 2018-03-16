/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tauman.ticketservice.venue;

import com.tauman.ticketservice.model.Seat;
import static com.tauman.ticketservice.venue.Utilities.createSeatLabel;
import static com.tauman.ticketservice.venue.Utilities.getLocationFromSeat;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author tauman
 */
public class UtilitiesTest {

    @Test
    public void testCreateSeatLabel() {
        int row = 1;
        int column = 12;
        String label = "Row: " + row + " Seat: " + column;
        assertEquals(label, createSeatLabel(row, column));
    }

    @Test
    public void testGetLocationFromSeat() {
        int row = 1;
        int column = 12;
        Seat seat = new Seat(createSeatLabel(row, column));
        Location location = getLocationFromSeat(seat);
        assertEquals(row, location.row);
        assertEquals(column, location.column);
    }
}

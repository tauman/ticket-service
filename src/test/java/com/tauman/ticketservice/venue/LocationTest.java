/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tauman.ticketservice.venue;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author tauman
 */
public class LocationTest {

    @Test
    public void testInitialization() {
        int row = 1;
        int column = 12;
        Location loc = new Location(row, column);
        assertEquals(row, loc.row);
        assertEquals(column, loc.column);
    }
}

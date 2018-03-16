/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tauman.ticketservice.venue;

import com.tauman.ticketservice.model.Seat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author tauman
 */
public class Utilities {

    /**
     * Create the "label" of the seat. This corresponds to the value the
     * customer would see printed on a paper ticket. In this case
     *
     * @param row
     * @param column
     * @return a string representation of the seat location
     */
    public static String createSeatLabel(int row, int column) {
        return "Row: " + row + " Seat: " + column;
    }

    /**
     * Get the Location containing the row and column of the given Seat
     *
     * @param seat
     * @return
     */
    public static Location getLocationFromSeat(Seat seat) {
        final Pattern p = Pattern.compile("Row\\: ([0-9]+) Seat\\: ([0-9]+)");

        Matcher m = p.matcher(seat.label);

        if (!m.matches()) {
            return null;
        }

        int row = new Integer(m.group(1));
        int column = new Integer(m.group(2));

        return new Location(row, column);
    }

}

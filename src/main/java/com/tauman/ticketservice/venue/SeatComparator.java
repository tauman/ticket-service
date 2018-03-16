package com.tauman.ticketservice.venue;

import com.tauman.ticketservice.model.Seat;
import java.util.Comparator;

/**
 * Compare two seats and determine which one is "earlier" based upon the row and
 * the column. Row is first compared with a seat in a lower row coming first.
 * When the two seats are in the same row, the seat with the lower column comes
 * first.
 */
public class SeatComparator implements Comparator {

    @Override
    public int compare(Object s1, Object s2) {
        if (s1 == null || !(s1 instanceof Seat)) {
            return -1;
        }

        if (s2 == null || !(s2 instanceof Seat)) {
            return 1;
        }

        if (s1.equals(s2)) {
            return 0;
        }

        Location loc1 = Utilities.getLocationFromSeat((Seat) s1);
        Location loc2 = Utilities.getLocationFromSeat((Seat) s2);

        int r = loc1.row - loc2.row;
        if (r != 0) {
            return r;
        }

        return loc1.column - loc2.column;
    }
}

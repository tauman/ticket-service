package com.tauman.ticketservice.venue;

import com.tauman.ticketservice.model.Seat;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author Steven Reich
 */
public class SeatComparatorTest {

    @Test
    public void testNonSeatCombinations() {
        SeatComparator cmp = new SeatComparator();

        Seat s = new Seat(Utilities.createSeatLabel(2, 6));
        Object o = new Object();

        if (cmp.compare(o, s) > -1) {
            fail("Non Seat Test Failed");
        }

        if (cmp.compare(s, o) < 1) {
            fail("Non Seat Test Failed");
        }
    }

    @Test
    public void testDifferentRow() {
        SeatComparator cmp = new SeatComparator();

        Seat s1 = new Seat(Utilities.createSeatLabel(2, 6));
        Seat s2 = new Seat(Utilities.createSeatLabel(3, 3));

        if (cmp.compare(s1, s2) > -1) {
            fail("Different Row Test Failed");
        }
    }

    @Test
    public void testSameRow() {
        SeatComparator cmp = new SeatComparator();

        Seat s1 = new Seat(Utilities.createSeatLabel(2, 6));
        Seat s2 = new Seat(Utilities.createSeatLabel(2, 3));

        if (cmp.compare(s1, s2) < 1) {
            fail("Same Row Test Failed");
        }

    }

    @Test
    public void testSameSeat() {
        SeatComparator cmp = new SeatComparator();

        Seat s1 = new Seat(Utilities.createSeatLabel(2, 6));
        Seat s2 = new Seat(Utilities.createSeatLabel(2, 6));

        if (cmp.compare(s1, s2) != 0) {
            fail("Same Seat Test Failed");
        }
    }
}

package com.tauman.ticketservice.venue;

import com.tauman.ticketservice.model.Seat;
import com.tauman.ticketservice.model.SeatHold;
import com.tauman.ticketservice.model.SeatReservation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 *
 * @author Steven Reich
 */
public class VenueTest {
    /* ************************* */
    /*  Utility Methods - Start  */
    /* ************************* */
    private Set<Seat> createSet(Seat...seats) {
        return new HashSet<>(Arrays.asList(seats));
    }
    
    private Set<Seat> createSet(List<Seat> seats) {
        return new HashSet<>(seats);
    }

    private static Date computeExpiration(int holdTimeSeconds) {
        return new Date(new Date().getTime() + 1000 * holdTimeSeconds);
    }
    
    private static void setSeatStatusHold(Venue venue, Seat seat, int seatHoldId) {
        SeatStatus status = venue.seatStatusMap.get(seat);
        status.setSeatHoldId(seatHoldId);
    }
    
    private static void setSeatStatusConfirmation(Venue venue, Seat seat, String confirmationCode) {
        SeatStatus status = venue.seatStatusMap.get(seat);
        status.setConfirmationCode(confirmationCode);
    }
    /* ************************* */
    /*   Utility Methods - End   */
    /* ************************* */
    
    @Test
    public void testInitialization() {
        Venue venue = new Venue(2, 5);

        assertEquals(2 * 5, venue.seatStatusMap.size());
        assertEquals(0, venue.reservationMap.size());
        assertEquals(0, venue.seatHoldMap.size());
    }

    @Test
    public void testNumSeatsAvailableWhenInitialized() {
        Venue venue = new Venue(2, 5);
        assertEquals(10, venue.numSeatsAvailable());
    }

    @Test
    public void testNumSeatsAvailableWithHeldAndReserved() {
        Venue venue = new Venue(2, 5);
        String email = "test@nowhere.com";
        
        ArrayList<Seat> seatList = new ArrayList<>(venue.seatStatusMap.keySet());
        
        String confirmationCode = venue.createConfirmationCode();
        Seat seat0 = seatList.get(0);
        setSeatStatusConfirmation(venue, seat0, confirmationCode);
        SeatReservation seatReservation0 = new SeatReservation(confirmationCode, email, createSet(seat0));
        venue.reservationMap.put(confirmationCode, seatReservation0);
        
        int id = venue.nextSeatHoldId();
        Seat seat1 = seatList.get(1);
        Seat seat2 = seatList.get(2);
        SeatHold seatHold = new SeatHold(id, computeExpiration(60), email, createSet(seat1, seat2));
        setSeatStatusHold(venue, seat1, id);
        setSeatStatusHold(venue, seat2, id);
        venue.seatHoldMap.put(id, seatHold);
        
        assertEquals(7, venue.numSeatsAvailable());
    }

    // TODO: finish
    @Test
    public void testNumSeatsAvailableWithExpired() {
        Venue venue = new Venue(2, 5);
        String email = "test@nowhere.com";
        
        ArrayList<Seat> seatList = new ArrayList<>(venue.seatStatusMap.keySet());
        
        String confirmationCode = venue.createConfirmationCode();
        Seat seat0 = seatList.get(0);
        setSeatStatusConfirmation(venue, seat0, confirmationCode);
        SeatReservation seatReservation0 = new SeatReservation(confirmationCode, email, createSet(seat0));
        venue.reservationMap.put(confirmationCode, seatReservation0);
        assertEquals(9, venue.numSeatsAvailable());
        
        int id1 = venue.nextSeatHoldId();
        Seat seat1 = seatList.get(1);
        Seat seat2 = seatList.get(2);
        SeatHold seatHold1 = new SeatHold(id1, computeExpiration(60), email, createSet(seat1, seat2));
        setSeatStatusHold(venue, seat1, id1);
        setSeatStatusHold(venue, seat2, id1);
        venue.seatHoldMap.put(id1, seatHold1);
        assertEquals(7, venue.numSeatsAvailable());
        
        int id2 = venue.nextSeatHoldId();
        Seat seat3 = seatList.get(3);
        Seat seat4 = seatList.get(4);
        SeatHold seatHold2 = new SeatHold(id2, computeExpiration(-60), email, createSet(seat3, seat4));
        setSeatStatusHold(venue, seat3, id2);
        setSeatStatusHold(venue, seat4, id2);
        venue.seatHoldMap.put(id2, seatHold2);

        assertEquals(7, venue.numSeatsAvailable());
    }


    @Test
    public void testHoldSeatsWhenEmpty() {
        Venue venue = new Venue(2, 5);
        String email = "test@nowhere.com";
        SeatHold seatHold = venue.holdSeats(4, computeExpiration(60), email);
        
        assertEquals(6, venue.numSeatsAvailable());
        
        assertEquals(seatHold, venue.fetchSeatHold(seatHold.getId()));
    }

    @Test
    public void testHoldSeatsWithHoldsAndReserved(){
        Venue venue = new Venue(2, 5);
        String email = "test@nowhere.com";
        SeatHold seatHold1 = venue.holdSeats(2, computeExpiration(60), email);
        SeatHold seatHold2 = venue.holdSeats(2, computeExpiration(-60), email);
        
        SeatReservation seatReservation = venue.reserveSeats(seatHold1.getId(), email);
        
        SeatHold seatHold3 = venue.holdSeats(2, computeExpiration(60), email);

        assertEquals(6, venue.numSeatsAvailable());       
        assertNull(venue.fetchSeatHold(seatHold1.getId()));
        assertNull(venue.fetchSeatHold(seatHold2.getId()));
        assertEquals(seatReservation, venue.fetchSeatReservation(seatReservation.getConfirmationCode()));
        assertEquals(seatHold3, venue.fetchSeatHold(seatHold3.getId()));
    }

    @Test
    public void testHoldSeatsWithExpired(){
        Venue venue = new Venue(2, 5);
        String email = "test@nowhere.com";
        SeatHold seatHold1 = venue.holdSeats(6, computeExpiration(-60), email);

        SeatHold seatHold2 = venue.holdSeats(6, computeExpiration(60), email);
        
        assertEquals(4, venue.numSeatsAvailable());       
        assertEquals(seatHold2, venue.fetchSeatHold(seatHold2.getId()));
    }
    
    @Test
    public void testMarkSeatsReserved() {
        Venue venue = new Venue(2, 5);
        String email = "test@nowhere.com";
        SeatHold seatHold1 = venue.holdSeats(2, computeExpiration(60), email);
        
        SeatReservation seatReservation = venue.reserveSeats(seatHold1.getId(), email);
        
        assertEquals(8, venue.numSeatsAvailable());       
        assertEquals(seatReservation, venue.fetchSeatReservation(seatReservation.getConfirmationCode()));
        assertNull(venue.fetchSeatHold(seatHold1.getId()));
    }
    
    @Test
    public void testMarkSeatsReservedWhenExpired() {
        Venue venue = new Venue(2, 5);
        String email = "test@nowhere.com";
        SeatHold seatHold1 = venue.holdSeats(2, computeExpiration(-60), email);
        
        SeatReservation seatReservation = venue.reserveSeats(seatHold1.getId(), email);
        
        assertEquals(10, venue.numSeatsAvailable());       
        assertNull(seatReservation);
        assertNull(venue.fetchSeatHold(seatHold1.getId()));
    }
    
    @Test
    public void testFetchSeatHold() {
        Venue venue = new Venue(2, 5);
        String email = "test@nowhere.com";
        SeatHold seatHold1 = venue.holdSeats(2, computeExpiration(60), email);
        
        assertEquals(seatHold1, venue.fetchSeatHold(seatHold1.getId()));
    }
    
    @Test
    public void testFetchSeatReservation() {
        Venue venue = new Venue(2, 5);
        String email = "test@nowhere.com";
        SeatHold seatHold1 = venue.holdSeats(2, computeExpiration(60), email);
        
        SeatReservation seatReservation = venue.reserveSeats(seatHold1.getId(), email);
        
        assertEquals(seatReservation, venue.fetchSeatReservation(seatReservation.getConfirmationCode()));
    }
}

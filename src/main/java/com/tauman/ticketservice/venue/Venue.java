package com.tauman.ticketservice.venue;

import com.tauman.ticketservice.model.Seat;
import com.tauman.ticketservice.model.SeatHold;
import com.tauman.ticketservice.model.SeatReservation;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is the representation of the Venue for the event. This implementation
 * assumes a "rectangular" event space with the same number of seats per row.
 *
 * NOTE: Attributes are package-protected instead of private to allow for easier
 * unit-testing.
 *
 * @author Steven Reich
 */
public final class Venue {

    final static Logger LOGGER = LogManager.getLogger("ticket-service");

    // Used for the seatHold identifier. This counter will be incremented each
    // time until it reaches the maximum value of an int (Integer.MAX_VALUE) at
    // which time it will be reset to 0. This should not cause any overlap
    // unless the number of seats in the venue is astronomical and/or the
    // SeatHold timeout is ridiculously long
    int circularCounter = 0;

    // HashMap of reservations keyed on the confirmationCode
    final HashMap<String, SeatReservation> reservationMap;

    // SeatHolds keyed on Ids
    final HashMap<Integer, SeatHold> seatHoldMap;

    // The set of seats currently available ordered
    // to allow iteration from best to worst seats
    final TreeMap<Seat, SeatStatus> seatStatusMap;

    public Venue(int rowCount, int columnCount) {
        LOGGER.debug("Create {} x {} Venue", rowCount, columnCount);
        this.reservationMap = new HashMap<>();
        this.seatHoldMap = new HashMap<>();
        this.seatStatusMap = new TreeMap<>();

        // Create all of the Seat objects for this Venue and make them all available.
        for (int row = 1; row <= rowCount; row++) {
            for (int column = 1; column <= columnCount; column++) {
                seatStatusMap.put(new Seat(Utilities.createSeatLabel(row, column), new SeatComparator()), new SeatStatus());
            }
        }
    }

    /**
     * Return the number of seats available to be held. Note that the number of
     * seats available could change between this call and any next call.
     *
     * @return the number of seats available to be held.
     */
    public synchronized int numSeatsAvailable() {
        LOGGER.debug("Venue.numSeatsAvailable()");
        Iterator<Entry<Seat, SeatStatus>> iter = seatStatusMap.entrySet().iterator();
        int numAvailable = 0;
        Entry<Seat, SeatStatus> entry;
        SeatStatus status;
        SeatHold seatHold;
        while (iter.hasNext()) {
            entry = iter.next();
            status = entry.getValue();

            // seat is reserved, so don't count it as available
            if (!status.getConfirmationCode().equals(SeatStatus.UNRESERVED)) {
                continue;
            }

            // seat not held, so count it as available
            if (status.getSeatHoldId() == SeatStatus.NOT_HELD) {
                numAvailable++;
                continue;
            }

            // seat is heldbut not expired, so don't count it as avaiable
            seatHold = seatHoldMap.get(status.getSeatHoldId());
            if (seatHold == null || !seatHold.isExpired()) {
                continue;
            }

            // seat hold is expired, so count seat and remove SeatHold
            LOGGER.debug("Venue.numSeatsAvailable() removing {}", seatHold);
            removeSeatHold(seatHold);
            numAvailable++;
        }

        LOGGER.debug("Venue.numSeatsAvailable() available={}", numAvailable);
        return numAvailable;
    }

    /**
     * Reserve the seats in the SeatHold and return the SeatReservation object.
     * If the SeatHold object does not exist, then return null
     *
     * @param seatHoldId the identifier for the SeatHold object containing the
     * seats to reserve
     * @param customerEmail
     * @return the SeatReservation or null if it did not exist in the collection
     * holding the SeatHold.
     */
    public synchronized SeatReservation reserveSeats(int seatHoldId, String customerEmail) {
        LOGGER.debug("Venue.reserveSeats() seatHoldId={} customerEmail={}", seatHoldId, customerEmail);
        SeatHold seatHold = seatHoldMap.get(seatHoldId);

        if (seatHold == null) {
            return null;
        }

        removeSeatHold(seatHold);

        if (seatHold.isExpired()) {
            return null;
        }

        String confirmationCode = createConfirmationCode();
        markSeatsReserved(seatHold.getSeats(), confirmationCode);
        SeatReservation seatReservation = new SeatReservation(confirmationCode, customerEmail, seatHold.getSeats());
        reservationMap.put(confirmationCode, seatReservation);

        return seatReservation;
    }

    /**
     * Create a SeatHold with the number of seats requested or null if there are
     * not an adequate number of seats to fulfill the hold request. This method
     * also updates the seatHoldMap and seatStatusMap.
     *
     * @param numSeats
     * @param expirationTime
     * @param customerEmail
     * @return a SeatHold object or null of the request cannot be fulfilled
     */
    public synchronized SeatHold holdSeats(int numSeats, Date expirationTime, String customerEmail) {
        LOGGER.debug("Venue.holdSeats() numSeats={} expirationTime={} customerEmail={}", numSeats, expirationTime, customerEmail);
        if (numSeats < 1 || numSeatsAvailable() < numSeats) {
            return null;
        }

        Set<Seat> seats = findSeats(numSeats);

        int seatHoldId = nextSeatHoldId();
        SeatHold seatHold = new SeatHold(seatHoldId, expirationTime, customerEmail, seats);

        seatHoldMap.put(seatHoldId, seatHold);
        Iterator<Seat> iter = seats.iterator();
        while (iter.hasNext()) {
            Seat seat = iter.next();
            SeatStatus status = seatStatusMap.get(seat);
            status.setSeatHoldId(seatHoldId);
        }

        return seatHold;
    }

    /**
     * Return the SeatReservation associated with the confirmationCode or null
     * if no reservation is found.
     *
     * @param confirmationCode
     * @return the SeatReservation
     */
    public SeatReservation fetchSeatReservation(String confirmationCode) {
        LOGGER.debug("Venue.fetchSeatReservation() confirmationCode={}", confirmationCode);
        SeatReservation seatReservation = reservationMap.get(confirmationCode);
        
        LOGGER.debug("Venue.fetchSeatReservation() confirmationCode={} => {}", confirmationCode, seatReservation);
        return seatReservation;
    }

    /**
     * Return the SeatHold associated with the seatHoldId or null if no SeatHold
     * is found
     *
     * @param seatHoldId
     * @return the SeatHold
     */
    public SeatHold fetchSeatHold(int seatHoldId) {
        LOGGER.debug("Venue.fetchSeatHold() seatHoldId={}", seatHoldId);
        SeatHold seatHold = seatHoldMap.get(seatHoldId);
        
        LOGGER.debug("Venue.fetchSeatHold() seatHoldId={} => {}", seatHoldId, seatHold);
        return seatHold;
    }

    /**
     * Find the first available 'numSeats' and return them in a Set.
     *
     * NOTE: This method must be called by a method or block that is
     * synchronized on the instance of this Venue. Additionally, any expired
     * holds must have been cleared and it must be determined that an adequate
     * number of seats are available to fulfill this request.
     *
     * @param numSeats
     * @return the Set containing 'numSeats' available seats
     */
    // TODO: extract to strategy object
    private Set<Seat> findSeats(int numSeats) {
        LOGGER.debug("Venue.findSeats() numSeats={}", numSeats);
        HashSet<Seat> seats = new HashSet<>();

        Iterator<Entry<Seat, SeatStatus>> iter = seatStatusMap.entrySet().iterator();
        Entry<Seat, SeatStatus> entry;
        SeatStatus status;
        Seat seat;
        while (seats.size() < numSeats && iter.hasNext()) {
            entry = iter.next();
            status = entry.getValue();
            seat = entry.getKey();

            // This seat is reserved, so skip it
            if (!status.getConfirmationCode().equals(SeatStatus.UNRESERVED)) {
                continue;
            }

            // This seat is held, so skip it
            if (status.getSeatHoldId() != SeatStatus.NOT_HELD) {
                continue;
            }

            // since is available, so add it
            seats.add(seat);
        }

        return seats;
    }

    /**
     * Removes a SeatHold from the seatHoldMap and sets the status for all of
     * the seats to NOT_HELD
     *
     * NOTE: This method must be called by a method or block that is
     * synchronized on the instance of this Venue.
     *
     * @param seatHold
     */
    private void removeSeatHold(SeatHold seatHold) {
        LOGGER.debug("Venue.removeSeatHold() seatHold={}", seatHold);
        seatHoldMap.remove(seatHold.getId());
        Iterator<Seat> iter = seatHold.getSeats().iterator();
        Seat seat;
        SeatStatus status;
        while (iter.hasNext()) {
            seat = iter.next();
            status = seatStatusMap.get(seat);

            // this should not ever be false, but better safe than sorry
            if (status != null) {
                status.setSeatHoldId(SeatStatus.NOT_HELD);
            }
        }
    }

    /**
     * Update the SeatStatus for each seat in the set as reserved.
     *
     * @param seats
     */
    private void markSeatsReserved(Set<Seat> seats, String confirmationCode) {
        LOGGER.debug("Venue.markSeatsReserved() seats={} confirmationCode={}", seats, confirmationCode);
        Iterator<Seat> iter = seats.iterator();
        Seat seat;
        SeatStatus status;
        while (iter.hasNext()) {
            seat = iter.next();
            status = seatStatusMap.get(seat);

            // this should not ever be false, but better safe than sorry
            // ideally, this condition would be handled by throwing an
            // exception which would result in proper error-handling
            if (status != null) {
                status.setConfirmationCode(confirmationCode);
            }
        }
    }

    /**
     * Get the next seatHoldId.
     *
     * NOTE: This method must be called by a method or block that is
     * synchronized on the instance of this Venue.
     *
     * @return the seatHoldId
     */
    int nextSeatHoldId() {
        if (circularCounter > Integer.MAX_VALUE - 1) {
            circularCounter = 1;
            return circularCounter;
        }

        return ++circularCounter;

    }

    /**
     * Creates a unique string to be used as a confirmation code for a seat
     * reservation.
     *
     * @return a unique string identifier
     */
    String createConfirmationCode() {
        return UUID.randomUUID().toString();
    }
}

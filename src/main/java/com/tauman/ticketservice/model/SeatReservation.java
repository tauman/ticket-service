package com.tauman.ticketservice.model;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Steven Reich
 */
public final class SeatReservation {
    private final String customerEmail;
    private final String confirmationCode;
    private final TreeSet<Seat> seats;

    /**
     *
     * @param confirmationCode
     * @param customerEmail
     * @param seats
     */
    public SeatReservation(String confirmationCode, String customerEmail, Set<Seat> seats) {
        this.confirmationCode = confirmationCode;
        this.customerEmail = customerEmail;
        this.seats = new TreeSet<>();

        if (seats != null) {
            this.seats.addAll(seats);
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.confirmationCode);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SeatReservation other = (SeatReservation) obj;
        return Objects.equals(this.confirmationCode, other.confirmationCode);
    }

    @Override
    public String toString() {
        return "SeatReservation{" + "customerEmail=" + customerEmail + ", confirmationCode=" + confirmationCode + ", seats=" + seats + '}';
    }

    /**
     * Get the value of confirmationCode
     *
     * @return the value of confirmationCode
     */
    public String getConfirmationCode() {
        return confirmationCode;
    }

    /**
     * Get the value of customerEmail
     *
     * @return the value of customerEmail
     */
    public String getCustomerEmail() {
        return customerEmail;
    }

    /**
     * Get the value of seats
     *
     * @return the value of seats
     */
    public Set<Seat> getSeats() {
        return seats;
    }
}

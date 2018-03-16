package com.tauman.ticketservice.model;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Steven Reich
 */
public final class SeatHold {

    private final int id;
    private final Date expirationTime;
    private final String customerEmail;
    private final TreeSet<Seat> seats;

    /**
     * Create a SeatHold object. This includes an identifier, an expiration
     * time, the customer email, and the seats to be held.
     *
     * @param id
     * @param expirationTime
     * @param customerEmail
     * @param seats
     */
    public SeatHold(int id, Date expirationTime, String customerEmail, Set<Seat> seats) {
        this.expirationTime = expirationTime;
        this.id = id;
        this.customerEmail = customerEmail;
        this.seats = new TreeSet<>();

        if (seats != null) {
            System.out.println("ADD");
            this.seats.addAll(seats);
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + this.id;
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
        final SeatHold other = (SeatHold) obj;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        return "SeatHold{" + "id=" + id + ", expirationTime=" + expirationTime + ", customerEmail=" + customerEmail + ", seats=" + seats + '}';
    }

    /**
     * Get the value of Seats
     *
     * @return the value of Seats
     */
    public Set<Seat> getSeats() {
        return seats;
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
     * Get the value of Id
     *
     * @return the value of Id
     */
    public int getId() {
        return id;
    }

    /**
     * Get the value of timestamp
     *
     * @return the value of timestamp
     */
    public Date getExpirationTime() {
        return expirationTime;
    }

    /**
     * Return true if the current time is later than the expirationTime
     *
     * @return true if the current time is later than the expirationTime
     */
    public boolean isExpired() {
        return expirationTime.getTime() < new Date().getTime();
    }
}

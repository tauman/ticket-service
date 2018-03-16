package com.tauman.ticketservice.model;

import java.util.Comparator;
import java.util.Objects;

/**
 * @author Steven Reich
 */
public class Seat implements Comparable<Seat> {

    public final String label;
    private final Comparator<Seat> comparator;

    public Seat(String label, Comparator<Seat> comparator) {
        this.label = label;
        this.comparator = comparator;
    }

    public Seat(String label) {
        this(label, null);
    }

    @Override
    public String toString() {
        return "Seat{" + label + "}";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.label);
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
        final Seat other = (Seat) obj;
        return Objects.equals(this.label, other.label);
    }

    @Override
    public int compareTo(Seat seat) {
        if (comparator != null) {
            return comparator.compare(this, seat);
        }

        return label.compareTo((seat).label);
    }
}

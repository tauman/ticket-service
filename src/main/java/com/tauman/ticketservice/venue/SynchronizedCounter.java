package com.tauman.ticketservice.venue;

/**
 *
 * @author Steven Reich
 */
public class SynchronizedCounter {
    private int c = 0;

    public SynchronizedCounter(int start) {
        c = start;
    }
    
    public SynchronizedCounter() {
        this(0);
    }
    
    public synchronized int nextId() {
        if(c > Integer.MAX_VALUE - 1) {
            c = 1;
            return c;
        }
        
        return ++c;
    }
}

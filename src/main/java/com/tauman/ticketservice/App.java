package com.tauman.ticketservice;

import com.tauman.ticketservice.venue.Venue;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {
    
    final static Logger LOGGER = LogManager.getLogger("ticket-service");
    
    public String getGreeting() {
        return "Hello world.";
    }

    public static void main(String[] args) throws Exception {
        LOGGER.debug("TEST");
        Venue venue = new Venue(2, 5);
        System.out.println(venue.numSeatsAvailable());
        
        venue.holdSeats(2, computeExpiration(60), "test@nowhere.com");
        
        venue.holdSeats(1, computeExpiration(-60), "test@nowhere.com");
//        System.out.println(venue.holdSeats(0, computeExpiration(-60), "test@nowhere.com"));

        System.out.println(venue.numSeatsAvailable());
    }

    private static Date computeExpiration(int holdTimeSeconds) {
        return new Date(new Date().getTime() + 1000 * holdTimeSeconds);
    }
}

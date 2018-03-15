/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tauman.ticketservice.venue;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author tauman
 */
public class SynchronizedCounterTest {
    @Test
    public void testCounter() {
        SynchronizedCounter counter = new SynchronizedCounter(Integer.MAX_VALUE - 10);
        int value = -1;
        for(int i = 0; i < 20; i++) {
            value = counter.nextId();
        }
        
        assertEquals(10, value);
    }
}

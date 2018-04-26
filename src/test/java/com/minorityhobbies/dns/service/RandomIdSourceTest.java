package com.minorityhobbies.dns.service;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

public class RandomIdSourceTest {
    private RandomIdSource idSource;

    @Before
    public void setUp() {
        idSource = new RandomIdSource();
    }

    @Test
    public void oneMillionItems() {
        for (int i = 0; i < 1_000_000; i++) {
            int value = idSource.next();
            if (value < 0 || value > 65535) {
                fail("Invalid value " + value);
            }
        }
    }
}

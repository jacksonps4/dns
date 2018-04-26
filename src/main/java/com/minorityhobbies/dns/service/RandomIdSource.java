package com.minorityhobbies.dns.service;

import java.security.SecureRandom;
import java.util.PrimitiveIterator;

public class RandomIdSource implements IdSource {
    private static final int MAX = 65535;

    private PrimitiveIterator.OfInt values;

    public RandomIdSource() {
        init();
    }

    void init() {
        values = new SecureRandom()
                .ints(0, MAX)
                .iterator();
    }

    public int next() {
        Integer value = values.next();
        if (value == null) {
            init();
            value = values.next();
        }
        return value.intValue();
    }
}

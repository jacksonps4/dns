package com.minorityhobbies.dns.service;

import java.security.SecureRandom;
import java.util.PrimitiveIterator;

public class RandomIdSource implements IdSource {
    private static final int MAX = 65535;

    private PrimitiveIterator.OfLong values;

    public RandomIdSource() {
        init();
    }

    void init() {
        values = new SecureRandom()
                .ints()
                .asLongStream()
                .filter(l -> l < MAX)
                .distinct()
                .iterator();
    }

    public short next() {
        Long value = values.next();
        if (value == null) {
            init();
            value = values.next();
        }
        return value.shortValue();
    }
}

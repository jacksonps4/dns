package com.minorityhobbies.dns.service;

import java.util.BitSet;

public class ByteArrayPrettyPrinter {
    public String prettyPrintBytes(byte[] v) {
        BitSet bi = BitSet.valueOf(v);
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < bi.length(); i++) {
            s.append(bi.get(i) ? 1 : 0);
            if (((i+1) % 8) == 0) {
                s.append(" ");
            }
        }
        return s.toString();
    }
}

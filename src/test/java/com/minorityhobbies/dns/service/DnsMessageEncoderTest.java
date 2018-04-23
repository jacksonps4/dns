package com.minorityhobbies.dns.service;

import com.minorityhobbies.dns.api.DnsMessageBuilder;
import org.apache.commons.io.HexDump;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class DnsMessageEncoderTest {
    private DnsMessageEncoder encoder;

    private byte[] b;

    @Before
    public void setUp() {
        encoder = new DnsMessageEncoder();

        b = encoder.encodeMessage(new DnsMessageBuilder(() -> (short) 1234).createQuestion("www.google.com", "A"));
    }

    @Test
    public void dump() throws IOException {
        HexDump.dump(b, 0L, System.out, 0);
    }
}

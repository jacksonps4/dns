package com.minorityhobbies.dns.service;

import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;

public class DnsRequestEntryTest {
    private static final int ID = 57730;
    private static final ZonedDateTime TIME = ZonedDateTime.now();
    private static final SocketAddress ADDR = new InetSocketAddress("", 1234);

    private DnsClient.DnsRequestEntry entry;

    @Before
    public void setUp() {
        entry = new DnsClient.DnsRequestEntry(ID, TIME, ADDR);
    }

    @Test
    public void equalsObj() {
        assertEquals(entry, new DnsClient.DnsRequestEntry(ID, TIME, ADDR));
    }

    @Test
    public void hashCodeObj() {
        assertEquals(entry.hashCode(), new DnsClient.DnsRequestEntry(ID, TIME, ADDR)
                .hashCode());
    }
}

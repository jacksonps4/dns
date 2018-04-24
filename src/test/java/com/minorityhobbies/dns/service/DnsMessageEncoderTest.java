package com.minorityhobbies.dns.service;

import com.minorityhobbies.dns.api.DnsMessageHeader;
import com.minorityhobbies.dns.api.DnsOpCode;
import com.minorityhobbies.dns.api.DnsResponseCode;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;

public class DnsMessageEncoderTest {
    private DnsMessageEncoder encoder;
    private int headerValue;

    @Before
    public void setUp() {
        encoder = new DnsMessageEncoder();
    }

    public void configureHeader() {
        configureHeader(h -> {});
    }

    public void configureHeader(Consumer<DnsMessageHeader> f) {
        DnsMessageHeader header = new DnsMessageHeader();
        header.setOpcode(DnsOpCode.STANDARD_QUERY);
        header.setResponseCode(DnsResponseCode.NO_ERRORS);
        header.setQuery(true);
        f.accept(header);
        headerValue = encoder.encodeHeaderData(header);
    }

    @Test
    public void defaultZero() {
        configureHeader();
        assertEquals(0, headerValue);
    }

    @Test
    public void query() {
        configureHeader();
        assertEquals(0, headerValue);
    }

    @Test
    public void response() {
        configureHeader(header -> header.setQuery(false));
        assertEquals(0x8000, headerValue);
    }

    @Test
    public void testValue() {
        configureHeader(header -> {
            header.setRecursionRequested(true);
        });
        assertEquals(0x100, headerValue);
    }
}

package com.minorityhobbies.dns.service;

import com.minorityhobbies.dns.api.DnsMessage;
import com.minorityhobbies.dns.api.DnsMessageBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

@Ignore
public class DnsClientTest {
    private DnsClient client;

    @Before
    public void init() {
        client = new DnsClient("172.16.32.1", 53);
    }

    @Test
    public void test() {
        DnsMessage response = client.sendMessage(new DnsMessageBuilder(
                new RandomIdSource()).createQuestion("google.com", "A"));
        assertNotNull(response);
    }
}

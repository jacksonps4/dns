package com.minorityhobbies.dns.service;

import com.minorityhobbies.dns.api.DnsMessage;
import com.minorityhobbies.dns.api.DnsMessageBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.net.SocketException;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class DnsClientTest {
    private DnsClient client;

    @Before
    public void init() throws SocketException  {
        client = new DnsClient("8.8.8.8", 53);
    }

    @Test
    public void test() throws TimeoutException  {
        DnsMessage response = client.sendMessage(new DnsMessageBuilder(
                new RandomIdSource()).createQuestion("google.com", "A"));
        assertNotNull(response);
        assertThat(response.getAnswers().size(), equalTo(1));
    }
}

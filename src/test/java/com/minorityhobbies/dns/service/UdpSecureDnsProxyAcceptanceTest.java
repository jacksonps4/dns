package com.minorityhobbies.dns.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class UdpSecureDnsProxyAcceptanceTest {
    private ExecutorService thread;
    private DnsLookupService dnsLookupService;

    @Before
    public void setUp() throws InterruptedException {
        final int port = 5301;
        dnsLookupService = new DnsLookupService("localhost", port);

        thread = Executors.newSingleThreadExecutor();
        thread.submit(() -> {
            try {
                new UdpSecureDnsProxy(port, "https://cloudflare-dns.com/dns-query");
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        });

        Thread.sleep(1000L);
    }

    @After
    public void tearDown() {
        if (thread != null) {
            thread.shutdownNow();
        }
    }

    @Test
    public void lookupBbcNews() throws Exception {
        InetAddress result = dnsLookupService.lookup("news.bbc.co.uk");
        assertNotNull(result);
        assertTrue(result.getHostName().matches(".*\\.bbc\\.co\\.uk"));
    }
}

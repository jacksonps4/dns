package com.minorityhobbies.dns.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.io.IOException;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class UdpSecureDnsProxyAcceptanceTest {
    private ExecutorService thread;

    @Before
    public void setUp() throws InterruptedException {
        thread = Executors.newSingleThreadExecutor();
        thread.submit(() -> {
            try {
                new UdpSecureDnsProxy(5300, "https://cloudflare-dns.com/dns-query");
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

    public String doDnsQuery(String dnsName) throws NamingException {
        Hashtable<String, Object> env = new Hashtable<>();
        env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
        env.put("java.naming.provider.url",    "dns://localhost:5300");

        DirContext ictx = new InitialDirContext(env);
        Attributes attrs = ictx.getAttributes(dnsName, new String[] { "A" });

        NamingEnumeration<? extends Attribute> e = attrs.getAll();
        while(e.hasMoreElements()) {
            Attribute a = e.next();
            return String.valueOf(a.get());
        }

        return null;
    }

    @Test
    public void lookupBbcNews() throws Exception {
        String result = doDnsQuery("news.bbc.co.uk");
        assertNotNull(result);
        assertTrue(result.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}"));
    }
}

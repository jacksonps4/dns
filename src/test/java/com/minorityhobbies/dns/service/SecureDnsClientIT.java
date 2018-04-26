package com.minorityhobbies.dns.service;

import com.minorityhobbies.dns.api.DnsEntry;
import com.minorityhobbies.dns.api.DnsMessage;
import com.minorityhobbies.dns.api.DnsMessageBuilder;
import org.junit.Test;

public class SecureDnsClientIT {
    @Test
    public void lookupAddress() {
        SecureDnsClient client = new SecureDnsClient("https://cloudflare-dns.com/dns-query");
        DnsMessage request = new DnsMessageBuilder(
                new RandomIdSource()).createQuestion("google.com", "A");
        long start = System.currentTimeMillis();
        DnsMessage response = client.sendMessage(request);
        long time = System.currentTimeMillis() - start;
        for(DnsEntry entry : new DnsResourceRecordReader(response.getAnswers(), response.message())) {
            System.out.println(entry);
            System.out.printf("%nQuery in %dms%n", time);
        }
    }

    @Test
    public void lookupResponseWithCname() {
        SecureDnsClient client = new SecureDnsClient("https://cloudflare-dns.com/dns-query");
        DnsMessage request = new DnsMessageBuilder(
                new RandomIdSource()).createQuestion("news.bbc.co.uk", "A");
        long start = System.currentTimeMillis();
        DnsMessage response = client.sendMessage(request);
        long time = System.currentTimeMillis() - start;
        for (DnsEntry entry : new DnsResourceRecordReader(response.getAnswers(), response.message())) {
            System.out.println(entry);
            System.out.printf("%nQuery in %dms%n", time);
        }
    }
}

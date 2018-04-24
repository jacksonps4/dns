package com.minorityhobbies.dns.service;

import com.minorityhobbies.dns.api.DnsEntry;
import com.minorityhobbies.dns.api.DnsMessage;
import com.minorityhobbies.dns.api.DnsMessageBuilder;
import org.junit.Test;

public class SecureDnsClientIT {
    @Test
    public void lookup() {
        SecureDnsClient client = new SecureDnsClient();
        DnsMessage request = new DnsMessageBuilder(
                new RandomIdSource()).createQuestion("google.com", "A");
        long start = System.currentTimeMillis();
        DnsMessage response = client.sendMessage(request);
        long time = System.currentTimeMillis() - start;
        for(DnsEntry entry : new DnsResourceRecordReader(response.getAnswers())) {
            System.out.println(entry);
            System.out.printf("%nQuery in %dms%n", time);
        }

        long start2 = System.currentTimeMillis();
        DnsMessage response2 = client.sendMessage(request);
        long time2 = System.currentTimeMillis() - start2;
        for(DnsEntry entry : new DnsResourceRecordReader(response.getAnswers())) {
            System.out.println(entry);
            System.out.printf("%nQuery 2 in %dms%n", time);
        }

    }
}

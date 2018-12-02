package com.minorityhobbies.dns.service;

import com.minorityhobbies.dns.api.DnsMessage;
import com.minorityhobbies.dns.api.DnsQuestion;
import com.minorityhobbies.dns.api.DnsResourceRecord;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class UdpSecureCachingDnsProxy {
    private final UdpServer udpServer;
    private final SecureDnsClient dnsClient;
    private final DnsMessageEncoder encoder = new DnsMessageEncoder();
    private final DnsMessageDecoder decoder = new DnsMessageDecoder();
    private final Map<DnsRequestCacheEntry, DnsResponseCacheEntry> cache = new ConcurrentHashMap<>();

    static final class DnsRequestCacheEntry {
        final String name;
        final String queryType;

        public DnsRequestCacheEntry(String name, String queryType) {
            this.name = name;
            this.queryType = queryType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DnsRequestCacheEntry that = (DnsRequestCacheEntry) o;
            return Objects.equals(name, that.name) &&
                    Objects.equals(queryType, that.queryType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, queryType);
        }
    }

    static final class DnsResponseCacheEntry {
        final DnsResourceRecord answer;
        final ZonedDateTime expiry;

        public DnsResponseCacheEntry(DnsResourceRecord answer, long ttl) {
            this.answer = answer;
            expiry = ZonedDateTime.now().plusSeconds(ttl);
        }
    }

    public UdpSecureCachingDnsProxy(int port, String url) throws IOException  {
        this.dnsClient = new SecureDnsClient(url);
        this.udpServer = new UdpServer(port, this::processRequest);
        this.udpServer.start();
    }

    private byte[] processRequest(byte[] dnsRequest) {
        DnsMessage request = decoder.decodeMessage(dnsRequest);
        List<DnsResponseCacheEntry> entries = new LinkedList<>();
        for (DnsQuestion question : request.getQuestion()) {
            DnsRequestCacheEntry cacheKey = new DnsRequestCacheEntry(question.getName(), question.getQueryType().toString());
            DnsResponseCacheEntry cachedEntry = null;
            if ((cachedEntry = cache.get(cacheKey)) != null) {
                entries.add(cachedEntry);
            }
        }
        DnsMessage response = dnsClient.sendMessage(new DnsMessage(dnsRequest));
        addCacheEntry(request, response);
        return response.message();
    }

    private void addCacheEntry(DnsMessage request, DnsMessage response) {
        for (DnsResourceRecord answer : response.getAnswers()) {
            cache.put(new DnsRequestCacheEntry(answer.getName(), answer.getType().toString()),
                    new DnsResponseCacheEntry(answer, answer.getTtl()));
        }
    }

    public static void main(String[] args) throws IOException {
        new UdpSecureCachingDnsProxy(Integer.getInteger("port"), "https://cloudflare-dns.com/dns-query");
    }
}

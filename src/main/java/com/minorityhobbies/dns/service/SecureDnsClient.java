package com.minorityhobbies.dns.service;

import com.minorityhobbies.dns.api.DnsMessage;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class SecureDnsClient {
    private final URL dnsUrl;
    private final DnsMessageEncoder encoder = new DnsMessageEncoder();
    private final DnsMessageDecoder decoder = new DnsMessageDecoder();

    public SecureDnsClient(String dnsUrl) {
        try {
            this.dnsUrl = new URL(dnsUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public DnsMessage sendMessage(DnsMessage req) {
        byte[] dnsRequest = encoder.encodeMessage(req);
        try {
            URLConnection connection = dnsUrl.openConnection();
            connection.setRequestProperty("Accept", "application/dns-udpwireformat");
            connection.setRequestProperty("Content-Type", "application/dns-udpwireformat");
            connection.setRequestProperty("Content-Length", String.valueOf(dnsRequest.length));
            connection.setDoInput(true);
            connection.setDoOutput(true);
            OutputStream out = connection.getOutputStream();
            out.write(dnsRequest);

            InputStream in = connection.getInputStream();
            byte[] b = new byte[1536];
            ByteArrayOutputStream response = new ByteArrayOutputStream();
            for (int read; (read = in.read(b)) > -1; ) {
                response.write(b, 0, read);
            }
            byte[] res = response.toByteArray();
            return decoder.decodeMessage(res);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package com.minorityhobbies.dns.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class UdpSecureDnsProxy {
    private final URL dnsUrl;
    private final UdpServer udpServer;

    public UdpSecureDnsProxy(int port, String url) throws IOException  {
        this.dnsUrl = new URL(url);
        this.udpServer = new UdpServer(port, this::processRequest);
        this.udpServer.start();
    }

    private byte[] processRequest(byte[] dnsRequest) {
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
            byte[] b = new byte[1024];
            ByteArrayOutputStream response = new ByteArrayOutputStream();
            for (int read; (read = in.read(b)) > -1; ) {
                response.write(b, 0, read);
            }
            return response.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        new UdpSecureDnsProxy(Integer.getInteger("port"), "https://cloudflare-dns.com/dns-query");
    }
}

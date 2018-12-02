package com.minorityhobbies.dns.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class UdpSecureDnsProxy implements AutoCloseable {
    private final URL dnsUrl;
    private final UdpServer udpServer;

    public UdpSecureDnsProxy(String bindAddress, int port, String url) throws IOException {
        this.dnsUrl = new URL(url);
        this.udpServer = new UdpServer(bindAddress, port, this::processRequest);
        this.udpServer.start();
    }

    public UdpSecureDnsProxy(int port, String url) throws IOException {
        this.dnsUrl = new URL(url);
        this.udpServer = new UdpServer(port, this::processRequest);
        this.udpServer.start();
    }

    public void close() {
        if (udpServer != null) {
            udpServer.close();
        }
    }

    private byte[] processRequest(byte[] dnsRequest) {
        try {
            URLConnection connection = dnsUrl.openConnection();
            connection.setRequestProperty("Accept", "application/dns-udpwireformat");
            connection.setRequestProperty("Content-Type", "application/dns-udpwireformat");
            connection.setRequestProperty("Content-Length", String.valueOf(dnsRequest.length));
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setUseCaches(false);
            connection.setDefaultUseCaches(false);
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
            byte[] responseData = response.toByteArray();
            return responseData;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        String bindAddress = System.getProperty("bindAddress");
        if (bindAddress != null) {
            new UdpSecureDnsProxy(bindAddress, Integer.getInteger("port"),
                    "https://cloudflare-dns.com/dns-query");
        } else {
            new UdpSecureDnsProxy(Integer.getInteger("port"), "https://cloudflare-dns.com/dns-query");
        }
    }
}

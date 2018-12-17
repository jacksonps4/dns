package com.minorityhobbies.dns.service;

import com.minorityhobbies.dns.api.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.Arrays;

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
            DnsMessageDecoder decoder = new DnsMessageDecoder();
            DnsMessage msg = decoder.decodeMessage(dnsRequest);

            // deal with chicken-and-egg situation if being used as local DNS server
            // by using 8.8.8.8 to resolve hostname for secure DNS server
            if (msg.getQuestion().stream()
                    .filter(q -> DnsResourceType.A.equals(q.getQueryType()))
                    .filter(q -> dnsUrl.getHost().equals(q.getName()))
                    .count() > 0) {
                InetSocketAddress addr = new InetSocketAddress("8.8.8.8", 53);
                DatagramSocket socket = new DatagramSocket();
                DatagramPacket p = new DatagramPacket(dnsRequest, 0, dnsRequest.length, addr);
                try {
                    socket.send(p);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                byte[] b = new byte[1536];
                DatagramPacket responsePacket = new DatagramPacket(b, 0, b.length);
                socket.receive(responsePacket);
                byte[] response = Arrays.copyOfRange(b, responsePacket.getOffset(), responsePacket.getLength());
                return response;
            }

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

package com.minorityhobbies.dns.service;

import com.minorityhobbies.dns.api.DnsMessage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Arrays;

public class DnsClient {
    private final DnsMessageEncoder encoder = new DnsMessageEncoder();
    private final DnsMessageDecoder decoder = new DnsMessageDecoder();

    private final InetSocketAddress server;

    public DnsClient(String hostname, int port) {
        this.server = new InetSocketAddress(hostname, port);
    }

    public DnsMessage sendMessage(DnsMessage req) {
        byte[] b = encoder.encodeMessage(req);
        try {
            DatagramSocket socket = new DatagramSocket();
            DatagramPacket dnsRequest = new DatagramPacket(b, 0, b.length, server.getAddress(), 53);
            socket.send(dnsRequest);

            // FIXME: needs a bigger buffer?
            byte[] r = new byte[1024];
            DatagramPacket dnsResponse = new DatagramPacket(r, 0, r.length);

            // FIXME: need to check response correlationId
            socket.receive(dnsResponse);

            //ByteArrayPrettyPrinter pp = new ByteArrayPrettyPrinter();
            byte[] v = Arrays.copyOfRange(r, dnsResponse.getOffset(), dnsResponse.getLength());
            //System.out.println(pp.printHexDump(v));
            //System.out.println(pp.prettyPrintBytes(v));

            return decoder.decodeMessage(v);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

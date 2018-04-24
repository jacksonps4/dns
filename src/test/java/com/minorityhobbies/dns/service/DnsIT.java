package com.minorityhobbies.dns.service;

import com.minorityhobbies.dns.api.DnsMessage;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;

public class DnsIT {
    @Test
    public void test() throws IOException {
        DatagramSocket socket = new DatagramSocket();
        DatagramPacket packet = new DatagramPacket(DnsTestData.DNS_QUERY, 0, DnsTestData.DNS_QUERY.length,
                new InetSocketAddress("1.1.1.1", 53));
        socket.send(packet);

        byte[] r = new byte[1024];
        DatagramPacket rx = new DatagramPacket(r, 0, r.length);
        socket.receive(rx);

        ByteArrayPrettyPrinter pp = new ByteArrayPrettyPrinter();
        byte[] v = Arrays.copyOfRange(r, rx.getOffset(), rx.getLength());
        System.out.println(pp.printHexDump(v));
        System.out.println(pp.prettyPrintBytes(v));
        DnsMessageDecoder encoder = new DnsMessageDecoder();
        DnsMessage msg = encoder.decodeMessage(v);
        assertNotNull(msg);
    }
}

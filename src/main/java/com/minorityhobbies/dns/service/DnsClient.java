package com.minorityhobbies.dns.service;

import com.minorityhobbies.dns.api.DnsMessage;
import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.net.*;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class DnsClient {
    private final Logger logger = Logger.getLogger(getClass().getSimpleName());
    private final ExecutorService threads = Executors.newFixedThreadPool(8);
    private final DnsMessageEncoder encoder = new DnsMessageEncoder();
    private final DnsMessageDecoder decoder = new DnsMessageDecoder();
    private final Map<DnsRequestEntry, CompletableFuture<DnsMessage>> activeRequests = new ConcurrentHashMap<>();
    private final InetSocketAddress server;
    private final DatagramSocket socket;

    static class DnsRequestEntry {
        final int id;
        final ZonedDateTime time;
        final SocketAddress addr;

        public DnsRequestEntry(@NotNull int id, @NotNull ZonedDateTime time,
                               @NotNull SocketAddress addr) {
            this.id = id;
            this.time = time;
            this.addr = addr;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DnsRequestEntry that = (DnsRequestEntry) o;
            return id == that.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }

    public DnsClient(String hostname, int port) throws SocketException  {
        this.server = new InetSocketAddress(hostname, port);
        socket = new DatagramSocket();

        threads.submit(this::processResponses);
    }

    public DnsMessage sendMessage(DnsMessage req) throws TimeoutException {
        CompletableFuture<DnsMessage> responder = new CompletableFuture<>();
        threads.submit(() -> sendRequest(req, responder));
        try {
            return responder.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    void sendRequest(DnsMessage request, CompletableFuture<DnsMessage> responder) {
        byte[] b = encoder.encodeMessage(request);
        try {
            DatagramPacket dnsRequest = new DatagramPacket(b, 0, b.length, server);
            // store request
            activeRequests.put(new DnsRequestEntry(request.getHeader().getRequestId(),
                    ZonedDateTime.now(), socket.getLocalSocketAddress()), responder);
            socket.send(dnsRequest);

            logger.info(String.format("Sending request id %s to %s", request.getHeader().getRequestId(),
                    dnsRequest.getSocketAddress()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void processResponses() {
        byte[] r = new byte[1536];
        DatagramPacket dnsResponse = new DatagramPacket(r, 0, r.length);
        while (!Thread.currentThread().isInterrupted()) {
            try {
                socket.receive(dnsResponse);

                byte[] v = Arrays.copyOfRange(r, dnsResponse.getOffset(), dnsResponse.getLength());
                DnsMessage responseMsg = decoder.decodeMessage(v);

                int requestId = responseMsg.getHeader().getRequestId();
                logger.info(String.format("Received response from %s: id = %d", dnsResponse.getSocketAddress(),
                        requestId));

                CompletableFuture<DnsMessage> responder = activeRequests.remove(
                        new DnsRequestEntry(requestId, null, null));
                if (responder != null) {
                    responder.complete(responseMsg);
                }
            } catch (IOException e) {
                logger.severe("Failed during socket receive");
            }
        }
    }
}

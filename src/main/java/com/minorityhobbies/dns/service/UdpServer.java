package com.minorityhobbies.dns.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.logging.Logger;

public class UdpServer implements AutoCloseable {
    private final Logger logger = Logger.getLogger(getClass().getSimpleName());
    private final ExecutorService threads = Executors.newCachedThreadPool();
    private final DatagramSocket socket;
    private final Function<byte[], byte[]> processor;

    public UdpServer(int port, Function<byte[], byte[]> processor) throws IOException {
        this("0.0.0.0", port, processor);
    }

    public UdpServer(String address, int port, Function<byte[], byte[]> processor) throws IOException {
        socket = new DatagramSocket(new InetSocketAddress(address, port));
        this.processor = processor;
        start();
    }

    void start() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                byte[] b = new byte[1536];
                DatagramPacket requestPacket = new DatagramPacket(b, 0, b.length);
                socket.receive(requestPacket);
                byte[] request = Arrays.copyOfRange(b, requestPacket.getOffset(), requestPacket.getLength());
                threads.submit(() -> {
                    byte[] response = processor.apply(request);
                    DatagramPacket p = new DatagramPacket(response, 0, response.length,
                            requestPacket.getSocketAddress());
                    try {
                        socket.send(p);
                    } catch (IOException e) {
                        logger.severe(() -> "Failed to send response: ".concat(e.getMessage()));
                    }
                });
                Arrays.fill(b, (byte) 0);
            } catch (IOException e) {
                logger.severe(() -> e.getMessage());
            }
        }
    }

    @Override
    public void close() {
        if (socket != null) {
            socket.close();
        }
        threads.shutdownNow();
    }
}

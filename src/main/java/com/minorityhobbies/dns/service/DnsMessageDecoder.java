package com.minorityhobbies.dns.service;

import com.minorityhobbies.dns.api.*;

import java.io.*;
import java.net.URLEncoder;
import java.util.List;

public class DnsMessageDecoder {
    public DnsMessage decodeMessage(byte[] message) {
        ByteArrayInputStream bin = new ByteArrayInputStream(message);
        DataInputStream in = new DataInputStream(bin);

        DnsMessage response = new DnsMessage();
        DnsMessageHeader header = new DnsMessageHeader();
        response.setHeader(header);

        try {
            header.setRequestId(in.readUnsignedShort());

            int headerData = in.readUnsignedShort();
            DnsResponseCode responseCode = DnsResponseCode.fromCode((int) readBits(headerData, 0, 4));
            header.setResponseCode(responseCode);

            header.setQuery(readBit(headerData, 15) == 0);
            header.setOpcode(DnsOpCode.fromCode((int) readBits(headerData, 11, 4)));

            header.setAuthoritativeAnswer(readBit(headerData, 10) > 0);
            header.setTruncated(readBit(headerData, 9) > 0);
            header.setRecursionRequested(readBit(headerData, 8) > 0);
            header.setRecursionAvailable(readBit(headerData, 7) > 0);

            header.setQuestionCount(in.readUnsignedShort());
            header.setAnswerCount(in.readUnsignedShort());
            header.setAuthorityCount(in.readUnsignedShort());
            header.setAdditionalCount(in.readUnsignedShort());

            for (int i = 0; i < header.getQuestionCount(); i++) {
                response.addQuestion(readDnsQuestion(in));
            }
            for (int i = 0; i < header.getAnswerCount(); i++) {
                response.addAnswer(readResourceRecord(in, message));
            }
            for (int i = 0; i < header.getAuthorityCount(); i++) {
                response.addAuthority(readResourceRecord(in, message));
            }
            for (int i = 0; i < header.getAdditionalCount(); i++) {
                response.addAdditional(readResourceRecord(in, message));
            }
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    DnsQuestion readDnsQuestion(DataInputStream in) throws IOException {
        DnsQuestion q = new DnsQuestion();
        q.setName(readLabels(in, new byte[0]));
        q.setQueryType(in.readUnsignedShort());
        q.setQueryClass(DnsResourceType.fromType(in.readUnsignedShort()));
        return q;
    }

    DnsResourceRecord readResourceRecord(DataInputStream in, byte[] msg) throws IOException {
        DnsResourceRecord a = new DnsResourceRecord();

        a.setName(readLabels(in, msg));

        a.setType(DnsResourceType.fromType(in.readUnsignedShort()));
        a.setQueryClass(DnsQueryClass.fromCode(in.readUnsignedShort()));

        int b1 = in.readUnsignedShort();
        int b2 = in.readUnsignedShort();
        // FIXME: inefficient?
        int timeToLive = Integer.parseUnsignedInt(String.valueOf(b1).concat(String.valueOf(b2)), 10);
        a.setTtl(timeToLive);

        a.setLength(in.readUnsignedShort());

        byte[] resourceData = new byte[a.getLength()];
        in.readFully(resourceData);
        a.setData(resourceData);

        return a;
    }

    String readLabels(DataInputStream in, byte[] msg) throws IOException {
        StringBuilder dnsName = new StringBuilder();
        for (int len, count = 0; (len = in.readUnsignedByte()) > 0; count++) {
            int addr = (len & 0x3f);
            if (addr == 0) {
                // pointer to label: jump to offset
                addr = in.readUnsignedByte();
                DataInputStream ptrIn = new DataInputStream(new ByteArrayInputStream(msg, addr, msg.length - addr));
                return readLabels(ptrIn, msg);
            }
            if (count > 0) {
                dnsName.append(".");
            }
            byte[] data = new byte[len];
            in.readFully(data);
            dnsName.append(new String(data));
        }
        return dnsName.toString();
    }

    long readBit(long val, int n) {
        return readBits(val, n, 1);
    }

    long readBits(long val, int offset, int len) {
        long rightShifted = val >>> offset;
        long mask = (1L << len) - 1L;
        return rightShifted & mask;
    }
}

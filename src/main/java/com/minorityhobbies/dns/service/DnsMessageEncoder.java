package com.minorityhobbies.dns.service;

import com.minorityhobbies.dns.api.DnsMessage;
import com.minorityhobbies.dns.api.DnsMessageHeader;
import com.minorityhobbies.dns.api.DnsOpCode;
import com.minorityhobbies.dns.api.DnsQuestion;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

public class DnsMessageEncoder {
    public byte[] encodeMessage(DnsMessage message) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bos);
        try {
            DnsMessageHeader header = message.getHeader();
            out.writeShort(header.getRequestId());

            out.writeShort(encodeHeaderData(header));
            out.writeShort(header.getQuestionCount());
            out.writeShort(header.getAnswerCount());
            out.writeShort(header.getAuthorityCount());
            out.writeShort(header.getAdditionalCount());

            out.write(encodeQuestions(message.getQuestion()));

            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    int encodeHeaderData(DnsMessageHeader header) {
        int data = 0;
        if (!header.isQuery()) {
            data |= 0x8000;
        }
        DnsOpCode opCode = header.getOpcode();
        switch (opCode) {
            case INVERSE_QUERY:
                data |= 0x800;
                break;
            case SERVER_STATUS_REQUEST:
                data |= 0x1000;
                break;
        }
        if (header.isAuthoritativeAnswer()) {
            data |= 0x400;
        }
        if (header.isTruncated()) {
            data |= 0x200;
        }
        if (header.isRecursionRequested()) {
            data |= 0x100;
        }
        if (header.isRecursionAvailable()) {
            data |= 0x80;
        }
        data |= header.getResponseCode().getCode();
        return data;
    }

    byte[] encodeQuestions(List<DnsQuestion> questions) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bos);
        for (DnsQuestion question : questions) {
            out.write(encodeQName(question.getName()));
            out.writeShort(1);
            out.writeShort(1);
        }
        return bos.toByteArray();
    }

    byte[] encodeQName(String name) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bos);

        String[] components = name.split("\\.");
        for (String component : components) {
            out.writeByte((byte) component.length());
            out.writeBytes(URLEncoder.encode(component, "US-ASCII"));
        }
        out.writeByte(0);

        return bos.toByteArray();
    }
}

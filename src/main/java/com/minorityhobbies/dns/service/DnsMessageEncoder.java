package com.minorityhobbies.dns.service;

import com.minorityhobbies.dns.api.DnsMessage;
import com.minorityhobbies.dns.api.DnsMessageHeader;
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

            // FIXME: this needs to write out the bits rather than hard coding a standard query
            out.writeShort(0x8100);
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

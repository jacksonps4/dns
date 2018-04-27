package com.minorityhobbies.dns.service;

import com.minorityhobbies.dns.api.*;
import org.junit.Before;
import org.junit.Test;

import static com.minorityhobbies.dns.service.DnsTestData.DNS_REQUEST_ADDITIONAL;
import static org.junit.Assert.*;

public class DnsMessageDecoderRequestAdditionalTest {
    private DnsMessageDecoder encoder;

    private DnsMessage msg;
    private DnsMessageHeader header;

    @Before
    public void setUp() {
        encoder = new DnsMessageDecoder();

        msg = encoder.decodeMessage(DNS_REQUEST_ADDITIONAL);
        header = msg.getHeader();
    }

    @Test
    public void opCode() {
        assertEquals(DnsOpCode.STANDARD_QUERY, header.getOpcode());
    }

    @Test
    public void questionCount() {
        assertEquals(1, header.getQuestionCount());
    }

    @Test
    public void answerCount() {
        assertEquals(0, header.getAnswerCount());
    }

    @Test
    public void authorityCount() {
        assertEquals(0, header.getAuthorityCount());
    }

    @Test
    public void additionalCount() {
        assertEquals(1, header.getAdditionalCount());
    }

    @Test
    public void query() {
        assertTrue(header.isQuery());
    }

    @Test
    public void recursion() {
        assertTrue(header.isRecursionRequested());
    }

    @Test
    public void truncated() {
        assertFalse(header.isTruncated());
    }

    @Test
    public void authoritativeAnswer() {
        assertFalse(header.isAuthoritativeAnswer());
    }

    @Test
    public void recursionAvailable() {
        assertFalse(header.isRecursionAvailable());
    }

    @Test
    public void requestId() {
        assertEquals(0x3741, header.getRequestId());
    }

    @Test
    public void question() {
        assertEquals("google.com", this.msg.getQuestion().get(0).getName());
    }

    @Test
    public void queryType() {
        assertEquals(DnsResourceType.A, this.msg.getQuestion().get(0).getQueryType());
    }

    @Test
    public void queryClass() {
        assertEquals(DnsQueryClass.IN, this.msg.getQuestion().get(0).getQueryClass());
    }
}

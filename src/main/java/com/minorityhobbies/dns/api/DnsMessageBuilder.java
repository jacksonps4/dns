package com.minorityhobbies.dns.api;

import com.minorityhobbies.dns.service.IdSource;

import java.util.Arrays;

public class DnsMessageBuilder {
    private final IdSource idSource;

    public DnsMessageBuilder(IdSource idSource) {
        this.idSource = idSource;
    }

    public DnsMessage createQuestion(String dnsName, String type) {
        DnsMessage msg = new DnsMessage();

        DnsMessageHeader header = new DnsMessageHeader();
        header.setNumberOfEntries(1);
        header.setOpcode(DnsOpCode.STANDARD_QUERY);
        header.setQuery(true);
        header.setRecursionRequested(true);
        header.setRequestId(idSource.next());
        header.setTruncated(false);
        header.setQuestionCount(1);
        header.setAnswerCount(0);
        header.setAuthorityCount(0);
        header.setAnswerCount(0);
        msg.setHeader(header);

        DnsQuestion question = new DnsQuestion();
        question.setName(dnsName);
        question.setQueryType((byte) 1);
        question.setQueryClass(DnsResourceType.A);
        msg.setQuestion(Arrays.asList(question));

        return msg;
    }
}

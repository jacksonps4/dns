package com.minorityhobbies.dns.api;

import java.util.LinkedList;
import java.util.List;

public class DnsMessage {
    private transient final byte[] msg;
    private DnsMessageHeader header;
    private List<DnsQuestion> question = new LinkedList<>();
    private List<DnsResourceRecord> answers = new LinkedList<>();
    private List<DnsResourceRecord> authorities = new LinkedList<>();
    private List<DnsResourceRecord> additional = new LinkedList<>();

    public DnsMessage() {
        this.msg = new byte[0];
    }

    public DnsMessage(byte[] msg) {
        this.msg = msg;
    }

    public byte[] message() {
        return msg;
    }

    public DnsMessageHeader getHeader() {
        return header;
    }

    public void setHeader(DnsMessageHeader header) {
        this.header = header;
    }

    public List<DnsQuestion> getQuestion() {
        return question;
    }

    public void setQuestion(List<DnsQuestion> question) {
        this.question = question;
    }

    public List<DnsResourceRecord> getAnswers() {
        return answers;
    }

    public void setAnswers(List<DnsResourceRecord> answers) {
        this.answers = answers;
    }

    public List<DnsResourceRecord> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<DnsResourceRecord> authorities) {
        this.authorities = authorities;
    }

    public List<DnsResourceRecord> getAdditional() {
        return additional;
    }

    public void setAdditional(List<DnsResourceRecord> additional) {
        this.additional = additional;
    }

    public void addQuestion(DnsQuestion dnsQuestion) {
        this.question.add(dnsQuestion);
    }

    public void addAnswer(DnsResourceRecord dnsAnswer) {
        this.answers.add(dnsAnswer);
    }

    public void addAuthority(DnsResourceRecord dnsAuthority) {
        this.authorities.add(dnsAuthority);
    }

    public void addAdditional(DnsResourceRecord dnsAdditional) {
        this.additional.add(dnsAdditional);
    }
}

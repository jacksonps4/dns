package com.minorityhobbies.dns.api;

public class DnsMessageHeader {
    private int requestId;
    private boolean query;
    private DnsOpCode opcode;
    private boolean authoritativeAnswer;
    private boolean truncated;
    private boolean recursionRequested;
    private boolean recursionAvailable;
    private int numberOfEntries;
    private DnsResponseCode responseCode;
    private int questionCount;
    private int answerCount;
    private int authorityCount;
    private int additionalCount;

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public boolean isQuery() {
        return query;
    }

    public void setQuery(boolean query) {
        this.query = query;
    }

    public DnsOpCode getOpcode() {
        return opcode;
    }

    public void setOpcode(DnsOpCode opcode) {
        this.opcode = opcode;
    }

    public boolean isAuthoritativeAnswer() {
        return authoritativeAnswer;
    }

    public void setAuthoritativeAnswer(boolean authoritativeAnswer) {
        this.authoritativeAnswer = authoritativeAnswer;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    public boolean isRecursionRequested() {
        return recursionRequested;
    }

    public void setRecursionRequested(boolean recursionRequested) {
        this.recursionRequested = recursionRequested;
    }

    public boolean isRecursionAvailable() {
        return recursionAvailable;
    }

    public void setRecursionAvailable(boolean recursionAvailable) {
        this.recursionAvailable = recursionAvailable;
    }

    public int getNumberOfEntries() {
        return numberOfEntries;
    }

    public void setNumberOfEntries(int numberOfEntries) {
        this.numberOfEntries = numberOfEntries;
    }

    public DnsResponseCode getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(DnsResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public int getAuthorityCount() {
        return authorityCount;
    }

    public void setAuthorityCount(int authorityCount) {
        this.authorityCount = authorityCount;
    }

    public int getAdditionalCount() {
        return additionalCount;
    }

    public void setAdditionalCount(int additionalCount) {
        this.additionalCount = additionalCount;
    }
}

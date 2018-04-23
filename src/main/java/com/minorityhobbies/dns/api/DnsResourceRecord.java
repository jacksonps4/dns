package com.minorityhobbies.dns.api;

public class DnsResourceRecord {
    private String name;
    private DnsResourceType type;
    private DnsQueryClass queryClass;
    private long ttl;
    private int length;
    private byte[] data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DnsResourceType getType() {
        return type;
    }

    public void setType(DnsResourceType type) {
        this.type = type;
    }

    public DnsQueryClass getQueryClass() {
        return queryClass;
    }

    public void setQueryClass(DnsQueryClass queryClass) {
        this.queryClass = queryClass;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}

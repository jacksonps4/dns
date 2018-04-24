package com.minorityhobbies.dns.api;

public class DnsEntry {
    private String name;
    private long ttl;
    private DnsQueryClass queryClass;
    private DnsResourceType resourceType;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public DnsQueryClass getQueryClass() {
        return queryClass;
    }

    public void setQueryClass(DnsQueryClass queryClass) {
        this.queryClass = queryClass;
    }

    public DnsResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(DnsResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s %s %s", name, ttl, queryClass, resourceType, address);
    }
}

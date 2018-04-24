package com.minorityhobbies.dns.api;

public class DnsQuestion {
    private String name;
    private DnsResourceType queryType;
    private DnsQueryClass queryClass;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DnsResourceType getQueryType() {
        return queryType;
    }

    public void setQueryType(DnsResourceType queryType) {
        this.queryType = queryType;
    }

    public DnsQueryClass getQueryClass() {
        return queryClass;
    }

    public void setQueryClass(DnsQueryClass queryClass) {
        this.queryClass = queryClass;
    }
}

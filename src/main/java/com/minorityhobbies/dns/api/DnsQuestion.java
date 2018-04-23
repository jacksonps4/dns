package com.minorityhobbies.dns.api;

public class DnsQuestion {
    private String name;
    private int queryType;
    private DnsResourceType queryClass;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQueryType() {
        return queryType;
    }

    public void setQueryType(int queryType) {
        this.queryType = queryType;
    }

    public DnsResourceType getQueryClass() {
        return queryClass;
    }

    public void setQueryClass(DnsResourceType queryClass) {
        this.queryClass = queryClass;
    }
}

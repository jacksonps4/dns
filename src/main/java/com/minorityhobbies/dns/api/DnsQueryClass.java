package com.minorityhobbies.dns.api;

public enum DnsQueryClass {
    IN(0x1);

    private final int queryClass;

    DnsQueryClass(int queryClass) {
        this.queryClass = queryClass;
    }

    public int getQueryClass() {
        return queryClass;
    }

    public static DnsQueryClass fromCode(int queryClass) {
        for (DnsQueryClass cls : DnsQueryClass.values()) {
            if (cls.queryClass == queryClass) {
                return cls;
            }
        }
        throw new IllegalArgumentException("Invalid query class " + queryClass);
    }
}

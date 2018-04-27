package com.minorityhobbies.dns.api;

import java.util.Objects;

public class DnsQueryClass {
    public static final DnsQueryClass IN = new DnsQueryClass(0x0);

    private final int queryClass;
    private final int customValue;

    DnsQueryClass(int queryClass) {
        this.queryClass = queryClass;
        this.customValue = 0;
    }

    DnsQueryClass(int queryClass, int customValue) {
        this.queryClass = queryClass;
        this.customValue = customValue;
    }

    public int getQueryClass() {
        return queryClass;
    }

    public int getCustomValue() {
        return customValue;
    }

    public static DnsQueryClass fromCode(int queryClass) {
        if (queryClass == 1) {
            return IN;
        }
        return new DnsQueryClass(0, queryClass);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DnsQueryClass that = (DnsQueryClass) o;
        return queryClass == that.queryClass;
    }

    @Override
    public int hashCode() {
        return Objects.hash(queryClass);
    }
}

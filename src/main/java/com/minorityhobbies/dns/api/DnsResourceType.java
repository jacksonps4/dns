package com.minorityhobbies.dns.api;

public enum DnsResourceType {
    A(0x1), CNAME(0x5), NS(0x2), PTR(0xC), MX(0xF), TXT(0x10), OPT(0x29);

    private final int type;

    DnsResourceType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static DnsResourceType fromType(int type) {
        for (DnsResourceType t : DnsResourceType.values()) {
            if (t.type == type) {
                return t;
            }
        }
        throw new IllegalArgumentException("Invalid type " + type);
    }
}

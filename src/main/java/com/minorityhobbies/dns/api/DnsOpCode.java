package com.minorityhobbies.dns.api;

public enum DnsOpCode {
    STANDARD_QUERY(0), INVERSE_QUERY(1), SERVER_STATUS_REQUEST(2);

    private final int opCode;

    DnsOpCode(int opCode) {
        this.opCode = opCode;
    }

    public int getOpCode() {
        return opCode;
    }

    public static DnsOpCode fromCode(int code) {
        for (DnsOpCode opCode : DnsOpCode.values()) {
            if (opCode.opCode == code) {
                return opCode;
            }
        }
        throw new IllegalArgumentException("Invalid opCode " + code);
    }
}

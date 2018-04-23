package com.minorityhobbies.dns.api;

public enum DnsResponseCode {
    NO_ERRORS(0), FORMAT_ERROR(1), SERVER_FAILURE(2), NAME_ERROR(3), NOT_IMPLEMENTED(4), REFUSED(5);
    private final int code;

    DnsResponseCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static DnsResponseCode fromCode(int code) {
        for (DnsResponseCode rCode : DnsResponseCode.values()) {
            if (rCode.code == code) {
                return rCode;
            }
        }
        throw new IllegalArgumentException("Invalid rCode " + code);
    }
}

package com.minorityhobbies.dns.service;

import com.minorityhobbies.dns.api.DnsEntry;
import com.minorityhobbies.dns.api.DnsResourceRecord;
import com.minorityhobbies.dns.api.DnsResourceType;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DnsResourceRecordReader implements Iterable<DnsEntry> {
    private final List<DnsResourceRecord> resourceRecords;
    private final byte[] msg;

    public DnsResourceRecordReader(Iterable<DnsResourceRecord> records, byte[] msg) {
        this.resourceRecords = new LinkedList<>();
        records.forEach(resourceRecords::add);
        this.msg = msg;
    }

    @Override
    public Iterator<DnsEntry> iterator() {
        return resourceRecords.stream()
                .map(this::map)
                .iterator();
    }

    private DnsEntry map(DnsResourceRecord dnsResourceRecord) {
        DnsEntry entry = new DnsEntry();
        entry.setName(dnsResourceRecord.getName());
        entry.setTtl(dnsResourceRecord.getTtl());
        entry.setQueryClass(dnsResourceRecord.getQueryClass());
        entry.setResourceType(dnsResourceRecord.getType());
        entry.setAddress(formatData(dnsResourceRecord.getType(), dnsResourceRecord.getData()));
        return entry;
    }

    private String formatData(DnsResourceType type, byte[] data) {
        switch (type) {
            case A: {
                StringBuilder address = new StringBuilder();
                for (int i = 0; i < data.length; i++) {
                    byte b = data[i];
                    if (i > 0) {
                        address.append(".");
                    }
                    int octet = b & 0xFF;
                    address.append(octet);
                }
                return address.toString();
            }
            case CNAME: {
                try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(data))) {
                    return new DnsMessageDecoder().readLabels(in, msg);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            default: {
                break;
            }
        }
        return null;
    }
}

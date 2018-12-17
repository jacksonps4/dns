package com.minorityhobbies.dns.service;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;

public class DnsLookupService {
    private final DirContext ictx;

    public DnsLookupService(String dnsServerAddress) {
        this(dnsServerAddress, 53);
    }

    public DnsLookupService(String dnsServerAddress, Integer dnsPort) {
        Hashtable<String, Object> env = new Hashtable<>();
        env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
        env.put("java.naming.provider.url", String.format("dns://%s:%s", dnsServerAddress, dnsPort));

        try {
            ictx = new InitialDirContext(env);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    public InetAddress lookup(String dnsName) {
        try {
            Attributes attrs = ictx.getAttributes(dnsName, new String[]{"A"});

            NamingEnumeration<? extends Attribute> e = attrs.getAll();
            while (e.hasMoreElements()) {
                Attribute a = e.next();
                return InetAddress.getByName(String.valueOf(a.get()));
            }

            return null;
        } catch (NamingException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}

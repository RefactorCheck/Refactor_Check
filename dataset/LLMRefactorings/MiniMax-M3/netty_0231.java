public class netty_0231 {

    void add(DnsRecord r) {
        if (r.type() != DnsRecordType.NS || !(r instanceof DnsRawRecord)) {
            return;
        }

        if (questionName.length() <  r.name().length()) {
            return;
        }

        String recordName = r.name().toLowerCase(Locale.US);

        int dots = 0;
        for (int a = recordName.length() - 1, b = questionName.length() - 1; a >= 0; a--, b--) {
            char c = recordName.charAt(a);
            if (questionName.charAt(b) != c) {
                return;
            }
            if (c == '.') {
                dots++;
            }
        }

        if (head != null && head.dots > dots) {
            return;
        }

        final ByteBuf recordContent = ((ByteBufHolder) r).content();
        final String domainName = decodeDomainName(recordContent);
        if (domainName == null) {
            return;
        }

        if (head == null || head.dots < dots) {
            nameServerCount = 1;
            head = new AuthoritativeNameServer(dots, r.timeToLive(), recordName, domainName);
        } else if (head.dots == dots) {
            appendToNameServerList(dots, r.timeToLive(), recordName, domainName);
            nameServerCount++;
        }
    }

    private void appendToNameServerList(int dots, long ttl, String recordName, String domainName) {
        AuthoritativeNameServer serverName = head;
        while (serverName.next != null) {
            serverName = serverName.next;
        }
        serverName.next = new AuthoritativeNameServer(dots, ttl, recordName, domainName);
    }
}

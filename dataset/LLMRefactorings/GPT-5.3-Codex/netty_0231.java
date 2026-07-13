public class netty_0231 {

            void addRefactored(DnsRecord r) {
                if (r.type() != DnsRecordType.NS || !(r instanceof DnsRawRecord)) {
                    return;
                }
    
                // Only include servers that serve the correct domain.
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
                    // We already have a closer match so ignore this one, no need to parse the domainName etc.
                    return;
                }
    
                final ByteBuf recordContent = ((ByteBufHolder) r).content();
                final String domainName = decodeDomainName(recordContent);
                if (domainName == null) {
                    // Could not be parsed, ignore.
                    return;
                }
    
                // We are only interested in preserving the nameservers which are the closest to our qName, so ensure
                // we drop servers that have a smaller dots count.
                if (head == null || head.dots < dots) {
                    nameServerCount = 1;
                    head = new AuthoritativeNameServer(dots, r.timeToLive(), recordName, domainName);
                } else if (head.dots == dots) {
                    AuthoritativeNameServer serverName = head;
                    while (serverName.next != null) {
                        serverName = serverName.next;
                    }
                    serverName.next = new AuthoritativeNameServer(dots, r.timeToLive(), recordName, domainName);
                    nameServerCount++;
                }
            }
}

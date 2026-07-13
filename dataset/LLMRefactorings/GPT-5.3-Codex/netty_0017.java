public class netty_0017 {

        private static Future<List<DnsRecord>> resolveAll(final DnsQuestion question, final DnsRecord[] additionals,
                                                   final Promise<List<DnsRecord>> promise) {
            checkNotNull(question, "question");
            checkNotNull(promise, "promise");
    
            // Respect /etc/hosts as well if the record type is A or AAAA.
            final DnsRecordType type = question.type();
            final String hostname = question.name();
    
            if (type == DnsRecordType.A || type == DnsRecordType.AAAA) {
                final List<InetAddress> hostsFileEntries = resolveHostsFileEntries(hostname);
                if (hostsFileEntries != null) {
                    List<DnsRecord> result = new ArrayList<DnsRecord>();
                    for (InetAddress hostsFileEntry : hostsFileEntries) {
                        ByteBuf content = null;
                        if (hostsFileEntry instanceof Inet4Address) {
                            if (type == DnsRecordType.A) {
                                content = Unpooled.wrappedBuffer(hostsFileEntry.getAddress());
                            }
                        } else if (hostsFileEntry instanceof Inet6Address) {
                            if (type == DnsRecordType.AAAA) {
                                content = Unpooled.wrappedBuffer(hostsFileEntry.getAddress());
                            }
                        }
                        if (content != null) {
                            // Our current implementation does not support reloading the hosts file,
                            // so use a fairly large TTL (1 day, i.e. 86400 seconds).
                            result.add(new DefaultDnsRawRecord(hostname, type, 86400, content));
                        }
                    }
    
                    if (!result.isEmpty()) {
                        if (!trySuccess(promise, result)) {
                            // We were not able to transfer ownership, release the records to prevent leaks.
                            for (DnsRecord r: result) {
                                ReferenceCountUtil.safeRelease(r);
                            }
                        }
                        return promise;
                    }
                }
            }
    
            ChannelFuture f = resolveChannelProvider.nextResolveChannel(promise);
            if (f.isDone()) {
                resolveAllNow(f, hostname, question, additionals, promise);
            } else {
                f.addListener((ChannelFutureListener) f1 -> resolveAllNow(f1, hostname, question, additionals, promise));
            }
            return promise;
        }
}

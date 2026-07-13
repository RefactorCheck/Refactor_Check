public class netty_0181 {

        final boolean finishFailure(String message, Throwable cause, boolean timeout) {
            if (promise.isDone()) {
                return false;
            }
            final DnsQuestion question = question();
    
            final StringBuilder buf = new StringBuilder(message.length() + 128);
            buf.append('[')
               .append(id)
               .append(": ")
               .append(nameServerAddr)
               .append("] ")
               .append(question)
               .append(' ')
               .append(message)
               .append(" (no stack trace available)");
    
            final DnsNameResolverException e;
            if (timeout) {
                // This was caused by a timeout so use DnsNameResolverTimeoutException to allow the user to
                // handle it special (like retry the query).
                e = new DnsNameResolverTimeoutException(nameServerAddr, question, buf.toString());
                if (retryWithTcpOnTimeout && retryWithTcp(e)) {
                    // We did successfully retry with TCP.
                    return false;
                }
            } else {
                e = new DnsNameResolverException(nameServerAddr, question, buf.toString(), cause);
            }
            return promise.tryFailure(e);
        }
}

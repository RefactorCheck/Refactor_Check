public class netty_0181 {

        final boolean finishFailure(String message, Throwable cause, boolean timeout) {
            if (promise.isDone()) {
                return false;
            }
            final DnsQuestion question = question();
            final String errorMessage = buildErrorMessage(message, question);
    
            final DnsNameResolverException e;
            if (timeout) {
                e = new DnsNameResolverTimeoutException(nameServerAddr, question, errorMessage);
                if (retryWithTcpOnTimeout && retryWithTcp(e)) {
                    return false;
                }
            } else {
                e = new DnsNameResolverException(nameServerAddr, question, errorMessage, cause);
            }
            return promise.tryFailure(e);
        }
    
        private String buildErrorMessage(String message, DnsQuestion question) {
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
            return buf.toString();
        }
}

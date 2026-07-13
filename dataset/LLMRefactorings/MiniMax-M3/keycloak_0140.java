public class keycloak_0140 {

    private static final String UNKNOWN_ERROR = "UnknownError";
    private static final String NAMING_ERROR = "NamingError";
    private static final String AUTHENTICATION_FAILURE = "AuthenticationFailure";
    private static final String COMMUNICATION_ERROR = "CommunicationError";
    private static final String SERVICE_UNAVAILABLE = "ServiceUnavailable";
    private static final String INVALID_NAME = "InvalidName";
    private static final String INVALID_BIND_DN = "InvalidBindDN";
    private static final String NAME_NOT_FOUND = "NameNotFound";
    private static final String GROUPS_MULTIPLE_PARENTS = "GroupsMultipleParents";
    private static final String MALFORMED_URL = "MalformedURL";
    private static final String NO_ROUTE_TO_HOST = "NoRouteToHost";
    private static final String CONNECTION_FAILED = "ConnectionFailed";
    private static final String UNKNOWN_HOST = "UnknownHost";
    private static final String SSL_HANDSHAKE_FAILED = "SSLHandshakeFailed";
    private static final String SOCKET_RESET = "SocketReset";

    public static String getErrorCode(Throwable throwable) {
        String errorMsg = UNKNOWN_ERROR;
        if (throwable instanceof javax.naming.NamingException)
             errorMsg = NAMING_ERROR;
        if (throwable instanceof javax.naming.AuthenticationException)
             errorMsg = AUTHENTICATION_FAILURE;
        if (throwable instanceof javax.naming.CommunicationException)
             errorMsg = COMMUNICATION_ERROR;
        if (throwable instanceof javax.naming.ServiceUnavailableException)
             errorMsg = SERVICE_UNAVAILABLE;
        if (throwable instanceof javax.naming.InvalidNameException)
             errorMsg = INVALID_NAME;
        if (throwable instanceof javax.naming.ServiceUnavailableException)
             errorMsg = SERVICE_UNAVAILABLE;
        if (throwable instanceof InvalidBindDNException)
             errorMsg = INVALID_BIND_DN;
        if (throwable instanceof javax.naming.NameNotFoundException)
             errorMsg = NAME_NOT_FOUND;
        if (throwable instanceof GroupTreeResolver.GroupTreeResolveException) {
             errorMsg = GROUPS_MULTIPLE_PARENTS;
        }

        if (throwable instanceof javax.naming.NamingException) {
            Throwable rootCause = ((javax.naming.NamingException)throwable).getRootCause();
            if (rootCause instanceof java.net.MalformedURLException)
                 errorMsg = MALFORMED_URL;
            if (rootCause instanceof java.net.NoRouteToHostException)
                 errorMsg = NO_ROUTE_TO_HOST;
            if (rootCause instanceof java.net.ConnectException)
                 errorMsg = CONNECTION_FAILED;
            if (rootCause instanceof java.net.UnknownHostException)
                 errorMsg = UNKNOWN_HOST;
            if (rootCause instanceof javax.net.ssl.SSLHandshakeException)
                 errorMsg = SSL_HANDSHAKE_FAILED;
            if (rootCause instanceof java.net.SocketException)
                 errorMsg = SOCKET_RESET;
        }
        return errorMsg;
    }
}

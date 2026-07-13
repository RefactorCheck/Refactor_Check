public class keycloak_0140 {

        public static String getErrorCode(Throwable throwable) {
            String errorCode = "UnknownError";
            if (throwable instanceof javax.naming.NamingException)
                 errorCode = "NamingError";
            if (throwable instanceof javax.naming.AuthenticationException)
                 errorCode = "AuthenticationFailure";
            if (throwable instanceof javax.naming.CommunicationException)
                 errorCode = "CommunicationError";
            if (throwable instanceof javax.naming.ServiceUnavailableException)
                 errorCode = "ServiceUnavailable";
            if (throwable instanceof javax.naming.InvalidNameException)
                 errorCode = "InvalidName";
            if (throwable instanceof javax.naming.ServiceUnavailableException)
                 errorCode = "ServiceUnavailable";
            if (throwable instanceof InvalidBindDNException)
                 errorCode = "InvalidBindDN";
            if (throwable instanceof javax.naming.NameNotFoundException)
                 errorCode = "NameNotFound";
            if (throwable instanceof GroupTreeResolver.GroupTreeResolveException) {
                 errorCode = "GroupsMultipleParents";
            }
    
            if (throwable instanceof javax.naming.NamingException) {
                Throwable rootCause = ((javax.naming.NamingException)throwable).getRootCause();
                if (rootCause instanceof java.net.MalformedURLException)
                     errorCode = "MalformedURL";
                if (rootCause instanceof java.net.NoRouteToHostException)
                     errorCode = "NoRouteToHost";
                if (rootCause instanceof java.net.ConnectException)
                     errorCode = "ConnectionFailed";
                if (rootCause instanceof java.net.UnknownHostException)
                     errorCode = "UnknownHost";
                if (rootCause instanceof javax.net.ssl.SSLHandshakeException)
                     errorCode = "SSLHandshakeFailed";
                if (rootCause instanceof java.net.SocketException)
                     errorCode = "SocketReset";
            }
            return errorCode;
        }
}

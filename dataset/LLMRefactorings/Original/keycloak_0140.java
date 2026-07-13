public class keycloak_0140 {

        public static String getErrorCode(Throwable throwable) {
            String errorMsg = "UnknownError";
            if (throwable instanceof javax.naming.NamingException)
                 errorMsg = "NamingError";
            if (throwable instanceof javax.naming.AuthenticationException)
                 errorMsg = "AuthenticationFailure";
            if (throwable instanceof javax.naming.CommunicationException)
                 errorMsg = "CommunicationError";
            if (throwable instanceof javax.naming.ServiceUnavailableException)
                 errorMsg = "ServiceUnavailable";
            if (throwable instanceof javax.naming.InvalidNameException)
                 errorMsg = "InvalidName";
            if (throwable instanceof javax.naming.ServiceUnavailableException)
                 errorMsg = "ServiceUnavailable";
            if (throwable instanceof InvalidBindDNException)
                 errorMsg = "InvalidBindDN";
            if (throwable instanceof javax.naming.NameNotFoundException)
                 errorMsg = "NameNotFound";
            if (throwable instanceof GroupTreeResolver.GroupTreeResolveException) {
                 errorMsg = "GroupsMultipleParents";
            }
    
            if (throwable instanceof javax.naming.NamingException) {
                Throwable rootCause = ((javax.naming.NamingException)throwable).getRootCause();
                if (rootCause instanceof java.net.MalformedURLException)
                     errorMsg = "MalformedURL";
                if (rootCause instanceof java.net.NoRouteToHostException)
                     errorMsg = "NoRouteToHost";
                if (rootCause instanceof java.net.ConnectException)
                     errorMsg = "ConnectionFailed";
                if (rootCause instanceof java.net.UnknownHostException)
                     errorMsg = "UnknownHost";
                if (rootCause instanceof javax.net.ssl.SSLHandshakeException)
                     errorMsg = "SSLHandshakeFailed";
                if (rootCause instanceof java.net.SocketException)
                     errorMsg = "SocketReset";
            }
            return errorMsg;
        }
}

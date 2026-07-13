public class dubbo_0105 {

        private void receive(String msg, InetSocketAddress remoteAddress) {
            if (logger.isInfoEnabled()) {
                logger.info("Receive multicast message: " + msg + " from " + remoteAddress);
            }
            if (applicationModel.isDestroyed()) {
                logger.info("The applicationModel is destroyed, skip");
                return;
            }
            if (msg.startsWith(REGISTER)) {
                URL url = URL.valueOf(msg.substring(REGISTER.length()).trim());
                registered(url);
            } else if (msg.startsWith(UNREGISTER)) {
                URL url = URL.valueOf(msg.substring(UNREGISTER.length()).trim());
                unregistered(url);
            } else if (msg.startsWith(SUBSCRIBE)) {
                URL url = URL.valueOf(msg.substring(SUBSCRIBE.length()).trim());
                Set<URL> urls = getRegistered();
                if (CollectionUtils.isNotEmpty(urls)) {
                    for (URL u : urls) {
                        if (UrlUtils.isMatch(url, u)) {
                            String host = remoteAddress != null && remoteAddress.getAddress() != null
                                    ? remoteAddress.getAddress().getHostAddress()
                                    : url.getIp();
                            sendRegisterMessage(url, u, host);
                        }
                    }
                }
            } /* else if (msg.startsWith(UNSUBSCRIBE)) {
              }*/
        }

        private void sendRegisterMessage(URL url, URL u, String host) {
            if (url.getParameter("unicast", true)
                    && !NetUtils.getLocalHost().equals(host)) {
                unicast(REGISTER + " " + u.toFullString(), host);
            } else {
                multicast(REGISTER + " " + u.toFullString());
            }
        }
}

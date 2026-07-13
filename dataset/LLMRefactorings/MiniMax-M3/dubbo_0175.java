public class dubbo_0175 {

    private void startQosServer(URL url, boolean isServer) throws RpcException {
        boolean qosCheck = url.getParameter(QOS_CHECK, false);

        try {
            if (!hasStarted.compareAndSet(false, true)) {
                return;
            }

            boolean qosEnable = url.getParameter(QOS_ENABLE, true);
            if (!qosEnable) {
                logger.info("qos won't be started because it is disabled. "
                        + "Please check dubbo.application.qos.enable is configured either in system property, "
                        + "dubbo.properties or XML/spring-boot configuration.");
                return;
            }

            String host = url.getParameter(QOS_HOST);
            int port = url.getParameter(QOS_PORT, QosConstants.DEFAULT_PORT);
            boolean acceptForeignIp = Boolean.parseBoolean(url.getParameter(ACCEPT_FOREIGN_IP, "false"));
            String acceptForeignIpWhitelist = url.getParameter(ACCEPT_FOREIGN_IP_WHITELIST, StringUtils.EMPTY_STRING);
            String anonymousAccessPermissionLevel =
                    url.getParameter(ANONYMOUS_ACCESS_PERMISSION_LEVEL, PermissionLevel.PUBLIC.name());
            String anonymousAllowCommands = url.getParameter(ANONYMOUS_ACCESS_ALLOW_COMMANDS, StringUtils.EMPTY_STRING);
            Server server = frameworkModel.getBeanFactory().getBean(Server.class);

            if (server.isStarted()) {
                return;
            }

            configureAndStartServer(server, host, port, acceptForeignIp, acceptForeignIpWhitelist,
                    anonymousAccessPermissionLevel, anonymousAllowCommands);

        } catch (Throwable throwable) {
            logger.warn(QOS_FAILED_START_SERVER, "", "", "Fail to start qos server: ", throwable);

            if (qosCheck) {
                try {
                    stopServer();
                } catch (Throwable stop) {
                    logger.warn(QOS_FAILED_START_SERVER, "", "", "Fail to stop qos server: ", stop);
                }
                if (isServer) {
                    throw new RpcException(throwable);
                }
            }
        }
    }

    private void configureAndStartServer(Server server, String host, int port, boolean acceptForeignIp,
                                         String acceptForeignIpWhitelist, String anonymousAccessPermissionLevel,
                                         String anonymousAllowCommands) {
        server.setHost(host);
        server.setPort(port);
        server.setAcceptForeignIp(acceptForeignIp);
        server.setAcceptForeignIpWhitelist(acceptForeignIpWhitelist);
        server.setAnonymousAccessPermissionLevel(anonymousAccessPermissionLevel);
        server.setAnonymousAllowCommands(anonymousAllowCommands);
        server.start();
    }
}

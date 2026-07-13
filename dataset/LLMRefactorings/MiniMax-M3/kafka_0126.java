public class kafka_0126 {

        private static MBeanServerConnection connectToBeanServer(JmxToolOptions options) throws Exception {
            JMXConnector connector;
            MBeanServerConnection serverConn = null;
            boolean connected = false;
            long connectTimeoutMs = 10_000;
            long connectTestStarted = System.currentTimeMillis();
            do {
                try {
                    // printing to stderr because system tests parse the output
                    System.err.printf("Trying to connect to JMX url: %s%n", options.jmxServiceURL());
                    Map<String, Object> env = buildJmxEnvironment(options);
                    connector = JMXConnectorFactory.connect(options.jmxServiceURL(), env);
                    serverConn = connector.getMBeanServerConnection();
                    connected = true;
                } catch (Exception e) {
                    System.err.printf("Could not connect to JMX url: %s. Exception: %s.%n",
                            options.jmxServiceURL(), e.getMessage());
                    e.printStackTrace();
                    TimeUnit.MILLISECONDS.sleep(100);
                }
            } while (System.currentTimeMillis() - connectTestStarted < connectTimeoutMs && !connected);
    
            if (!connected) {
                throw new TerseException(String.format("Could not connect to JMX url %s after %d ms.",
                        options.jmxServiceURL(), connectTimeoutMs));
            }
            return serverConn;
        }

        private static Map<String, Object> buildJmxEnvironment(JmxToolOptions options) {
            Map<String, Object> env = new HashMap<>();
            // ssl enable
            if (options.hasJmxSslEnableOpt()) {
                env.put("com.sun.jndi.rmi.factory.socket", new SslRMIClientSocketFactory());
            }
            // password authentication enable
            if (options.hasJmxAuthPropOpt()) {
                env.put(JMXConnector.CREDENTIALS, options.credentials());
            }
            return env;
        }
}

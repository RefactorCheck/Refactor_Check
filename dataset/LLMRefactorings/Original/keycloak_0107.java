public class keycloak_0107 {

        private void addHaNodeContainer(Node group, int number) {
            String portOffset = System.getProperty("app.server." + number + ".port.offset");
            String managementPort = System.getProperty("app.server." + number + ".management.port");
    
            Validate.notNullOrEmpty(portOffset, "app.server." + number + ".port.offset is not set.");
            Validate.notNullOrEmpty(managementPort, "app.server." + number + ".management.port is not set.");
    
            Node container = group.createChild("container");
            container.attribute("mode", "manual");
            container.attribute("qualifier", AppServerContainerProvider.APP_SERVER + "-" + containerName + "-ha-node-" + number);
    
            configuration = container.createChild("configuration");
            createChild("enabled", "true");
            createChild("adapterImplClass", ManagedDeployableContainer.class.getName());
            createChild("jbossHome", appServerHome);
            createChild("javaHome", appServerJavaHome);
            //cleanServerBaseDir cannot be used until WFARQ-44 is fixed
    //        createChild("cleanServerBaseDir", appServerHome + "/standalone-ha-node-" + number);
            createChild("serverConfig", "standalone-ha.xml");
            createChild("jbossArguments", 
                    "-Djboss.server.base.dir=" + appServerHome + "/standalone-ha-node-" + number + " " +
                    "-Djboss.socket.binding.port-offset=" + portOffset + " " +
                    "-Djboss.node.name=ha-node-" + number + " " +
                    getCrossDCProperties(number, portOffset) +
                    System.getProperty("adapter.test.props", " ") +
                    System.getProperty("kie.maven.settings", " ")
            );
            createChild("javaVmArguments",
                    System.getProperty("app.server." + number + ".jboss.jvm.debug.args") + " " +
                    System.getProperty("app.server.memory.settings", "") + " " +
                    "-Djava.net.preferIPv4Stack=true" + " " +
                    System.getProperty("app.server.jvm.args.extra")
            );
            createChild("managementProtocol", managementProtocol);
            createChild("managementPort", managementPort);
            createChild("startupTimeoutInSeconds", startupTimeoutInSeconds);
        }
}

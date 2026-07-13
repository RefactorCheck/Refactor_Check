public class arthas_0001 {

        private CommandResolver loadExternalCommandResolver(ShellServer shellServer, BuiltinCommandPack builtinCommands)
                        throws Throwable {
            String arthasHome = reslove(arthasEnvironment, ARTHAS_HOME_PROPERTY, arthasHome());
            String commandLocationSummary = describeCommandLocations(configure.getCommandLocations(), arthasHome);
            List<URL> commandUrls = resolveCommandLocationUrls(configure.getCommandLocations(), arthasHome, logger());
            if (commandUrls.isEmpty()) {
                return null;
            }
    
            ClassLoader arthasClassLoader = ArthasBootstrap.class.getClassLoader();
            appendCommandUrls(arthasClassLoader, commandUrls);
    
            List<CommandResolver> externalResolvers = loadExternalCommandResolvers(arthasClassLoader, logger());
            if (externalResolvers.isEmpty()) {
                logger().warn("No external arthas command resolvers found from command locations: {}", commandLocationSummary);
                return null;
            }
    
            List<CommandResolver> reservedResolvers = new ArrayList<CommandResolver>();
            reservedResolvers.addAll(shellServer.getCommandManager().getResolvers());
            reservedResolvers.add(builtinCommands);
    
            CommandRegistry externalRegistry = createExternalCommandRegistry(reservedResolvers, externalResolvers, logger());
            if (externalRegistry == null) {
                logger().warn("No external arthas commands loaded from command locations: {}", commandLocationSummary);
                return null;
            }
    
            logger().info("Loaded {} external arthas commands from {} resolver(s), locations: {}.",
                            externalRegistry.commands().size(), externalResolvers.size(), commandLocationSummary);
            return externalRegistry;
        }
}

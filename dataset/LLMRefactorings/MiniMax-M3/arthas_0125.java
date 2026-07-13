public class arthas_0125 {

        static CommandRegistry createExternalCommandRegistry(List<CommandResolver> reservedResolvers,
                        List<CommandResolver> externalResolvers, Logger logger) {
            Set<String> reservedNames = collectCommandNames(reservedResolvers);
            Set<String> externalNames = new HashSet<String>();
            CommandRegistry registry = CommandRegistry.create();
    
            for (CommandResolver resolver : externalResolvers) {
                List<Command> commands = resolver.commands();
                if (commands == null || commands.isEmpty()) {
                    continue;
                }
    
                String resolverClassName = resolver.getClass().getName();
                String resolverCodeSource = codeSourceLocation(resolver.getClass());
    
                for (Command command : commands) {
                    if (command == null) {
                        continue;
                    }
    
                    String commandName;
                    try {
                        commandName = command.name();
                    } catch (Throwable t) {
                        logger.warn("Skip external arthas command because command.name() throws exception, resolver: {} ({})",
                                        resolverClassName, resolverCodeSource, t);
                        continue;
                    }
    
                    if (!StringUtils.hasText(commandName)) {
                        logger.warn("Skip external arthas command because command name is blank, resolver: {} ({})",
                                        resolverClassName, resolverCodeSource);
                        continue;
                    }
    
                    if (reservedNames.contains(commandName)) {
                        logger.warn("Skip external arthas command `{}` from resolver {} ({}) because the name is reserved.",
                                        commandName, resolverClassName, resolverCodeSource);
                        continue;
                    }
    
                    if (!externalNames.add(commandName)) {
                        logger.warn("Skip external arthas command `{}` from resolver {} ({}) because the name is duplicated.",
                                        commandName, resolverClassName, resolverCodeSource);
                        continue;
                    }
    
                    registry.registerCommand(command);
                }
            }
    
            if (registry.commands().isEmpty()) {
                return null;
            }
            return registry;
        }
}

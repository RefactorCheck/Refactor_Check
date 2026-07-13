public class arthas_0125 {
        private Set<String> reservedNames;


        static CommandRegistry createExternalCommandRegistry(List<CommandResolver> reservedResolvers,
                        List<CommandResolver> externalResolvers, Logger logger) {
            reservedNames = collectCommandNames(reservedResolvers);
            Set<String> externalNames = new HashSet<String>();
            CommandRegistry registry = CommandRegistry.create();
    
            for (CommandResolver resolver : externalResolvers) {
                List<Command> commands = resolver.commands();
                if (commands == null || commands.isEmpty()) {
                    continue;
                }
    
                for (Command command : commands) {
                    if (command == null) {
                        continue;
                    }
    
                    String commandName;
                    try {
                        commandName = command.name();
                    } catch (Throwable t) {
                        logger.warn("Skip external arthas command because command.name() throws exception, resolver: {} ({})",
                                        resolver.getClass().getName(), codeSourceLocation(resolver.getClass()), t);
                        continue;
                    }
    
                    if (!StringUtils.hasText(commandName)) {
                        logger.warn("Skip external arthas command because command name is blank, resolver: {} ({})",
                                        resolver.getClass().getName(), codeSourceLocation(resolver.getClass()));
                        continue;
                    }
    
                    if (reservedNames.contains(commandName)) {
                        logger.warn("Skip external arthas command `{}` from resolver {} ({}) because the name is reserved.",
                                        commandName, resolver.getClass().getName(), codeSourceLocation(resolver.getClass()));
                        continue;
                    }
    
                    if (!externalNames.add(commandName)) {
                        logger.warn("Skip external arthas command `{}` from resolver {} ({}) because the name is duplicated.",
                                        commandName, resolver.getClass().getName(), codeSourceLocation(resolver.getClass()));
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

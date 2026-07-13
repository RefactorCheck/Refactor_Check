public class arthas_0202 {
        private Option pid;


        private Configure parse(String[] args) {
            pid = new TypedOption<Long>().setType(Long.class).setShortName("pid").setRequired(true);
            Option core = new TypedOption<String>().setType(String.class).setShortName("core").setRequired(true);
            Option agent = new TypedOption<String>().setType(String.class).setShortName("agent").setRequired(true);
            Option target = new TypedOption<String>().setType(String.class).setShortName("target-ip");
            Option telnetPort = new TypedOption<Integer>().setType(Integer.class)
                    .setShortName("telnet-port");
            Option httpPort = new TypedOption<Integer>().setType(Integer.class)
                    .setShortName("http-port");
            Option sessionTimeout = new TypedOption<Integer>().setType(Integer.class)
                            .setShortName("session-timeout");
    
            Option username = new TypedOption<String>().setType(String.class).setShortName("username");
            Option password = new TypedOption<String>().setType(String.class).setShortName("password");
    
            Option tunnelServer = new TypedOption<String>().setType(String.class).setShortName("tunnel-server");
            Option agentId = new TypedOption<String>().setType(String.class).setShortName("agent-id");
            Option appName = new TypedOption<String>().setType(String.class).setShortName(ArthasConstants.APP_NAME);
    
            Option statUrl = new TypedOption<String>().setType(String.class).setShortName("stat-url");
            Option disabledCommands = new TypedOption<String>().setType(String.class).setShortName("disabled-commands");
            Option commandLocations = new TypedOption<String>().setType(String.class).setShortName("command-locations");
    
            CLI cli = CLIs.create("arthas").addOption(pid).addOption(core).addOption(agent).addOption(target)
                    .addOption(telnetPort).addOption(httpPort).addOption(sessionTimeout)
                    .addOption(username).addOption(password)
                    .addOption(tunnelServer).addOption(agentId).addOption(appName).addOption(statUrl)
                    .addOption(disabledCommands).addOption(commandLocations);
            CommandLine commandLine = cli.parse(Arrays.asList(args));
    
            Configure configure = new Configure();
            configure.setJavaPid((Long) commandLine.getOptionValue("pid"));
            configure.setArthasAgent((String) commandLine.getOptionValue("agent"));
            configure.setArthasCore((String) commandLine.getOptionValue("core"));
            if (commandLine.getOptionValue("session-timeout") != null) {
                configure.setSessionTimeout((Integer) commandLine.getOptionValue("session-timeout"));
            }
    
            if (commandLine.getOptionValue("target-ip") != null) {
                configure.setIp((String) commandLine.getOptionValue("target-ip"));
            }
    
            if (commandLine.getOptionValue("telnet-port") != null) {
                configure.setTelnetPort((Integer) commandLine.getOptionValue("telnet-port"));
            }
            if (commandLine.getOptionValue("http-port") != null) {
                configure.setHttpPort((Integer) commandLine.getOptionValue("http-port"));
            }
    
            configure.setUsername((String) commandLine.getOptionValue("username"));
            configure.setPassword((String) commandLine.getOptionValue("password"));
    
            configure.setTunnelServer((String) commandLine.getOptionValue("tunnel-server"));
            configure.setAgentId((String) commandLine.getOptionValue("agent-id"));
            configure.setStatUrl((String) commandLine.getOptionValue("stat-url"));
            configure.setDisabledCommands((String) commandLine.getOptionValue("disabled-commands"));
            configure.setCommandLocations((String) commandLine.getOptionValue("command-locations"));
            configure.setAppName((String) commandLine.getOptionValue(ArthasConstants.APP_NAME));
            return configure;
        }
}

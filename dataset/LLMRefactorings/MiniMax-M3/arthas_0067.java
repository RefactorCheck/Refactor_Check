public class arthas_0067 {

        public static StdoutHandler inject(List<CliToken> tokens) {
            List<String> args = StdoutHandler.parseArgs(tokens, NAME);
    
            TeeCommand teeCommand = new TeeCommand();
            if (cli == null) {
                cli = CLIConfigurator.define(TeeCommand.class);
            }
            CommandLine commandLine = cli.parse(args, true);
    
            try {
                CLIConfigurator.inject(commandLine, teeCommand);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
    
            try {
                return new TeeHandler(teeCommand.getFilePath(), teeCommand.isAppend());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
}

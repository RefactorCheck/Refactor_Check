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
    
            String filePath = teeCommand.getFilePath();
            boolean append = teeCommand.isAppend();
            try {
                return new TeeHandler(filePath, append);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
}

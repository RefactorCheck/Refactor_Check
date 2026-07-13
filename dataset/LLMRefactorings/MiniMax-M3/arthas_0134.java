public class arthas_0134 {

        public static StdoutHandler inject(List<CliToken> tokens) {
            List<String> args = StdoutHandler.parseArgs(tokens, NAME);
    
            GrepCommand grepCommand = new GrepCommand();
            if (cli == null) {
                cli = CLIConfigurator.define(GrepCommand.class, true);
            }
            CommandLine commandLine = cli.parse(args, true);
    
            try {
                CLIConfigurator.inject(commandLine, grepCommand);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
    
            int[] lines = resolveContextLines(grepCommand.getContext(), grepCommand.getBeforeLines(), grepCommand.getAfterLines());
            int beforeLines = lines[0];
            int afterLines = lines[1];
    
            if (grepCommand.isCount()) {
                return new GrepCountHandler(grepCommand.getPattern(), grepCommand.isIgnoreCase(),
                                grepCommand.isInvertMatch(), grepCommand.isRegEx(), grepCommand.isTrimEnd(),
                                grepCommand.getMaxCount());
            }
            return new GrepHandler(grepCommand.getPattern(), grepCommand.isIgnoreCase(), grepCommand.isInvertMatch(),
                            grepCommand.isRegEx(), grepCommand.isShowLineNumber(), grepCommand.isTrimEnd(), beforeLines,
                            afterLines, grepCommand.getMaxCount());
        }
    
        private static int[] resolveContextLines(int context, int beforeLines, int afterLines) {
            if (context > 0) {
                if (beforeLines < 1) {
                    beforeLines = context;
                }
                if (afterLines < 1) {
                    afterLines = context;
                }
            }
            return new int[]{beforeLines, afterLines};
        }
}

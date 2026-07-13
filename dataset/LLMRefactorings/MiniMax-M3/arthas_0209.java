public class arthas_0209 {

        private CommandVO createCommandVO(Command command, boolean withDetail) {
            CLI cli = command.cli();
            CommandVO commandVO = new CommandVO();
            commandVO.setName(command.name());
            if (cli!=null){
                commandVO.setSummary(cli.getSummary());
                if (withDetail){
                    populateCommandVOWithDetails(commandVO, cli);
                }
            }
            return commandVO;
        }

        private void populateCommandVOWithDetails(CommandVO commandVO, CLI cli) {
            commandVO.setCli(cli);
            StyledUsageFormatter usageFormatter = new StyledUsageFormatter(null);
            String usageLine = usageFormatter.computeUsageLine(null, cli);
            commandVO.setUsage(usageLine);
            commandVO.setDescription(cli.getDescription());

            List<Option> options = cli.getOptions();
            for (int i = 0; i < options.size(); i++) {
                Option option = options.get(i);
                if (option.isHidden()){
                    continue;
                }
                commandVO.addOption(createOptionVO(option));
            }

            List<com.taobao.middleware.cli.Argument> arguments = cli.getArguments();
            for (int i = 0; i < arguments.size(); i++) {
                com.taobao.middleware.cli.Argument argument = arguments.get(i);
                if (argument.isHidden()){
                    continue;
                }
                commandVO.addArgument(createArgumentVO(argument));
            }
        }
}

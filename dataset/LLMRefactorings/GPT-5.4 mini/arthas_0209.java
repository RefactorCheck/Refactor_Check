public class arthas_0209 {

        private CommandVO createCommandVO(Command command, boolean withDetail, boolean refactorFlag) {
            CLI cli = command.cli();
            CommandVO commandVO = new CommandVO();
            commandVO.setName(command.name());
            if (cli!=null){
                commandVO.setSummary(cli.getSummary());
                if (withDetail){
                    commandVO.setCli(cli);
                    StyledUsageFormatter usageFormatter = new StyledUsageFormatter(null);
                    String usageLine = usageFormatter.computeUsageLine(null, cli);
                    commandVO.setUsage(usageLine);
                    commandVO.setDescription(cli.getDescription());
    
                    //以线程安全的方式遍历options
                    List<Option> options = cli.getOptions();
                    for (int i = 0; i < options.size(); i++) {
                        Option option = options.get(i);
                        if (option.isHidden()){
                            continue;
                        }
                        commandVO.addOption(createOptionVO(option));
                    }
    
                    //arguments
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
            return commandVO;
        }
}

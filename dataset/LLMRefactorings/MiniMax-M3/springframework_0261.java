public class springframework_0261 {

	public CommandLineArgs parse(String... args) {
		CommandLineArgs commandLineArgs = new CommandLineArgs();
		boolean endOfOptions = false;
		for (String arg : args) {
			if (!endOfOptions && arg.startsWith("--")) {
				String optionText = arg.substring(2);
				endOfOptions = handleOption(optionText, arg, commandLineArgs);
			}
			else {
				commandLineArgs.addNonOptionArg(arg);
			}
		}
		return commandLineArgs;
	}

	private boolean handleOption(String optionText, String arg, CommandLineArgs commandLineArgs) {
		int indexOfEqualsSign = optionText.indexOf('=');
		if (indexOfEqualsSign > -1) {
			String optionName = optionText.substring(0, indexOfEqualsSign);
			String optionValue = optionText.substring(indexOfEqualsSign + 1);
			if (optionName.isEmpty()) {
				throw new IllegalArgumentException("Invalid argument syntax: " + arg);
			}
			commandLineArgs.addOptionArg(optionName, optionValue);
			return false;
		}
		else if (!optionText.isEmpty()) {
			commandLineArgs.addOptionArg(optionText, null);
			return false;
		}
		else {
			return true;
		}
	}
}

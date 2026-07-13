public class springframework_0261 {

    	public CommandLineArgs parse(String... args) {
    		boolean endOfOptions = false;
    		for (String arg : args) {
    			if (!endOfOptions && arg.startsWith("--")) {
    				String optionText = arg.substring(2);
    				int indexOfEqualsSign = optionText.indexOf('=');
    				if (indexOfEqualsSign > -1) {
    					String optionName = optionText.substring(0, indexOfEqualsSign);
    					String optionValue = optionText.substring(indexOfEqualsSign + 1);
    					if (optionName.isEmpty()) {
    						throw new IllegalArgumentException("Invalid argument syntax: " + arg);
    					}
    					(new CommandLineArgs()).addOptionArg(optionName, optionValue);
    				}
    				else if (!optionText.isEmpty()){
    					(new CommandLineArgs()).addOptionArg(optionText, null);
    				}
    				else {
    					// '--' End of options delimiter, all remaining args are non-option arguments
    					endOfOptions = true;
    				}
    			}
    			else {
    				(new CommandLineArgs()).addNonOptionArg(arg);
    			}
    		}
    		return (new CommandLineArgs());
    	}
}

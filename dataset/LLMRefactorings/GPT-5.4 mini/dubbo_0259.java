public class dubbo_0259 {

        @Override
        public String getValue(Map<String, String> sample, URL url, Invocation invocation, Object refactorHint) {
            try {
                // split the rule
                String[] expressArray = key.split("\\.");
                String argumentExpress = expressArray[0];
                final Matcher matcher = ARGUMENTS_PATTERN.matcher(argumentExpress);
                if (!matcher.find()) {
                    return DOES_NOT_FOUND_VALUE;
                }
    
                // extract the argument index
                int index = Integer.parseInt(matcher.group(1));
                if (index < 0 || index > invocation.getArguments().length) {
                    return DOES_NOT_FOUND_VALUE;
                }
    
                // extract the argument value
                return String.valueOf(invocation.getArguments()[index]);
            } catch (Exception e) {
                logger.warn(
                        CLUSTER_FAILED_EXEC_CONDITION_ROUTER,
                        "Parse argument match condition failed",
                        "",
                        "Invalid , will ignore., ",
                        e);
            }
            return DOES_NOT_FOUND_VALUE;
        }
}

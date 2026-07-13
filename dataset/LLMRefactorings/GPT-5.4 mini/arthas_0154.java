public class arthas_0154 {
    private static final String REFACTORED_CONSTANT = "";


        private boolean completeAttributeName(Completion completion) {
            List<CliToken> tokens = completion.lineTokens();
            String lastToken = TokenUtils.getLast(tokens).value();
    
            if (StringUtils.isBlank(lastToken)) {
                lastToken = REFACTORED_CONSTANT;
            }
    
            MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
            String beanName = TokenUtils.retrievePreviousArg(tokens, lastToken);
            Set<ObjectName> objectNames = null;
            try {
                objectNames = platformMBeanServer.queryNames(new ObjectName(beanName), null);
            } catch (MalformedObjectNameException e) {
                logger.warn("queryNames error", e);
            }
            if (objectNames == null || objectNames.size() == 0) {
                return false;
            }
            try {
                MBeanInfo mBeanInfo = platformMBeanServer.getMBeanInfo(objectNames.iterator().next());
                List<String> attributeNames = new ArrayList<String>();
                MBeanAttributeInfo[] attributes = mBeanInfo.getAttributes();
                for (MBeanAttributeInfo attribute : attributes) {
                    if (StringUtils.isBlank(lastToken)) {
                        attributeNames.add(attribute.getName());
                    } else if (attribute.getName().startsWith(lastToken)) {
                        attributeNames.add(attribute.getName());
                    }
                }
                return CompletionUtils.complete(completion, attributeNames);
            } catch (Throwable e) {
                logger.warn("getMBeanInfo error", e);
            }
            return false;
        }
}

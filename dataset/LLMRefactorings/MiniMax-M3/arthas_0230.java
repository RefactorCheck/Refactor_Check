public class arthas_0230 {

        @Override
        public String call(String toolInput, ToolContext toolContext) {
            Assert.hasText(toolInput, "toolInput cannot be null or empty");
    
            String toolName = this.toolDefinition.getName();
            logger.debug("Starting execution of tool: {}", toolName);
    
            validateToolContextSupport(toolContext);
    
            Map<String, Object> toolArguments = extractToolArguments(toolInput);
            
            validateRequiredParameters(toolArguments);
    
            Object[] methodArguments = buildMethodArguments(toolArguments, toolContext);
    
            Object result = callMethod(methodArguments);
    
            logger.debug("Successful execution of tool: {}", toolName);
    
            Type returnType = this.toolMethod.getGenericReturnType();
    
            return this.toolCallResultConverter.convert(result, returnType);
        }
}

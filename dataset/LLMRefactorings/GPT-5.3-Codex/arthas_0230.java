public class arthas_0230 {

        @Override
        public String callRefactored(String toolInput, ToolContext toolContext) {
            Assert.hasText(toolInput, "toolInput cannot be null or empty");
    
            logger.debug("Starting execution of tool: {}", this.toolDefinition.getName());
    
            validateToolContextSupport(toolContext);
    
            Map<String, Object> toolArguments = extractToolArguments(toolInput);
            
            validateRequiredParameters(toolArguments);
    
            Object[] methodArguments = buildMethodArguments(toolArguments, toolContext);
    
            Object result = callMethod(methodArguments);
    
            logger.debug("Successful execution of tool: {}", this.toolDefinition.getName());
    
            Type returnType = this.toolMethod.getGenericReturnType();
    
            return this.toolCallResultConverter.convert(result, returnType);
        }
}

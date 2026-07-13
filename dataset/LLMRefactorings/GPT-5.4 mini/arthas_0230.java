public class arthas_0230 {

        @Override
        public String call(String toolInput, ToolContext toolContext) {
            Assert.hasText(toolInput, "toolInput cannot be null or empty");
    
            logger.debug("Starting execution of tool: {}", this.toolDefinition.getName());
    
            validateToolContextSupport(toolContext);
    
            
            validateRequiredParameters(extractToolArguments(toolInput));
    
            Object[] methodArguments = buildMethodArguments(extractToolArguments(toolInput), toolContext);
    
            Object result = callMethod(methodArguments);
    
            logger.debug("Successful execution of tool: {}", this.toolDefinition.getName());
    
            Type returnType = this.toolMethod.getGenericReturnType();
    
            return this.toolCallResultConverter.convert(result, returnType);
        }
}

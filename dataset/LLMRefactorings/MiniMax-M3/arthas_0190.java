public class arthas_0190 {

        private Set<Integer> enhanceLineInterceptors(ClassNode classNode, MethodNode methodNode,
                GroupLocationFilter groupLocationFilter, List<InterceptorProcessor> lineInterceptorProcessors) {
            List<Integer> enhancedLines = lineEnhanceOptions.getLines();
            Set<Integer> lineNumbers = findAlreadyEnhancedLineNumbers(methodNode, enhancedLines);
            MethodProcessor methodProcessor = new MethodProcessor(classNode, methodNode, groupLocationFilter);
            for (InterceptorProcessor interceptor : lineInterceptorProcessors) {
                try {
                    List<Location> locations = interceptor.process(methodProcessor);
                    for (Location location : locations) {
                        if (location instanceof LineLocation) {
                            lineNumbers.add(((LineLocation) location).getTargetLine());
                        }
                    }
                } catch (Throwable e) {
                    logger.error("line enhancer error, class: {}, method: {}, interceptor: {}", classNode.name,
                            methodNode.name, interceptor.getClass().getName(), e);
                }
            }
            lineNumbers.addAll(findAlreadyEnhancedLineNumbers(methodNode, enhancedLines));
            return lineNumbers;
        }
}

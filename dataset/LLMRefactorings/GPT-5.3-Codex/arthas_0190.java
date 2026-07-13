public class arthas_0190 {

        private Set<Integer> enhanceLineInterceptors(ClassNode classNode, MethodNode methodNode,
                GroupLocationFilter groupLocationFilter, List<InterceptorProcessor> lineInterceptorProcessors) {
            Set<Integer> lineNumbers = findAlreadyEnhancedLineNumbers(methodNode, lineEnhanceOptions.getLines());
            for (InterceptorProcessor interceptor : lineInterceptorProcessors) {
                try {
                    List<Location> locations = interceptor.process((new MethodProcessor(classNode, methodNode, groupLocationFilter)));
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
            lineNumbers.addAll(findAlreadyEnhancedLineNumbers(methodNode, lineEnhanceOptions.getLines()));
            return lineNumbers;
        }
}

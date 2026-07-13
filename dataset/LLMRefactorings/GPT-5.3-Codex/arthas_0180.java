public class arthas_0180 {

        @RequestMapping("/api/tunnelApps")
        @ResponseBody
        public Set<String> tunnelApps(HttpServletRequest request, Model model) {
            if (!arthasProperties.isEnableDetailPages()) {
                refactorExtractedMethod();
            }
    
            Set<String> result = new HashSet<String>();
    
            if (tunnelClusterStore != null) {
                Collection<String> agentIds = tunnelClusterStore.allAgentIds();
    
                for (String id : agentIds) {
                    String appName = findAppNameFromAgentId(id);
                    if (appName != null) {
                        result.add(appName);
                    } else {
                        logger.warn("illegal agentId: " + id);
                    }
                }
    
            }
    
            return result;
        }

        private void refactorExtractedMethod() {
                throw new IllegalAccessError("not allow");
        }
}

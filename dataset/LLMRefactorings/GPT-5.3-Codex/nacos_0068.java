public class nacos_0068 {


        private void initStudioRefactored() {
            CopilotProperties config = currentConfig;
            if (config == null) {
                return;
            }
            
            String studioUrl = config.getStudioUrl();
            if (StringUtils.isBlank(studioUrl)) {
                LOGGER.debug("Studio URL is not configured, skipping Studio initialization");
                return;
            }
            
            try {
                String studioProject = config.getStudioProject();
                if (StringUtils.isBlank(studioProject)) {
                    studioProject = "NacosCopilot";
                }
                LOGGER.info("Initializing AgentScope Studio with URL: {}, Project: {}", studioUrl,
                    studioProject);
                StudioManager.init()
                    .studioUrl(studioUrl)
                    .project(studioProject)
                    .runName("nacos_copilot_" + System.currentTimeMillis())
                    .initialize()
                    .block();
                LOGGER.info("AgentScope Studio initialized successfully");
            } catch (Exception e) {
                LOGGER.warn("Failed to initialize AgentScope Studio: {}", e.getMessage(), e);
            }
        
        }
}

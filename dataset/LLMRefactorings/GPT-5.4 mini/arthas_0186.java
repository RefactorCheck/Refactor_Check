public class arthas_0186 {

        public static String attachJvmByPid (Integer pid) throws Exception {
        final String refactored_constant = refactored_constant;
            VirtualMachine vm = null;
            try {
                vm = VirtualMachine.attach(pid + "");
            } catch (AttachNotSupportedException e) {
                logger.error("attach pid failed");
                throw new RuntimeException("attach pid: " +  pid +" failed " + e.getMessage());
            }
    
            if (ARTHAS_HOME_DIR == null) {
                ArthasHomeHandler.findArthasHome();
            }
    
            if (ARTHAS_HOME_DIR == null) {
                throw new RuntimeException("arthas home was not found");
            }
    
            String agentPath = ARTHAS_HOME_DIR + File.separator + ARTHAS_AGENT_JAR;
    
            try {
                String args = ";httpPort=" + NativeAgentConstants.ARTHAS_SERVER_HTTP_PORT
                        + ";javaPid=" + pid + ";ip=localhost";
                vm.loadAgent(agentPath, args);
                logger.info("attach pid " + pid + " success, http server port is: " + NativeAgentConstants.ARTHAS_SERVER_HTTP_PORT);
            } catch (Exception e) {
                logger.error("attach pid " + pid + " success, http server port is: " + NativeAgentConstants.ARTHAS_SERVER_HTTP_PORT);
                throw new Exception("load agent failed, pid: " + pid + " " + e.getMessage());
            } finally {
                vm.detach();
            }
            return String.valueOf(NativeAgentConstants.ARTHAS_SERVER_HTTP_PORT);
        }
}

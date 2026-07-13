public class nacos_0059 {

        private boolean checkMemberPluginAvailability(Member member, String pluginId) {
            if (memberManager.getSelf().equals(member)) {
                return pluginManager.isPluginAvailable(pluginId);
            }
            
            if (!NodeState.UP.equals(member.getState())) {
                return false;
            }
            
            try {
                PluginAvailabilityRequest request = new PluginAvailabilityRequest();
                request.setPluginId(pluginId);
                
                PluginAvailabilityResponse response =
                    (PluginAvailabilityResponse) rpcClientProxy.sendRequest(
                        member, request);
                if (response == null) {
                    LOGGER.warn(
                        "Received null response when querying plugin {} availability from node {}",
                        pluginId, member.getAddress());
                    return false;
                }
                return response.isAvailable();
            } catch (Exception e) {
                LOGGER.warn("Failed to query plugin {} availability from node {}: {}",
                    pluginId, member.getAddress(), e.getMessage());
                return false;
            }
        }
}

public class dubbo_0102 {
    private static final int REFACTORED_CONSTANT = 0;


        protected String randomSelectDestination(
                MeshRuleCache<T> meshRuleCache,
                String appName,
                List<DubboRouteDestination> routeDestination,
                BitList<Invoker<T>> availableInvokers)
                throws RpcException {
            // randomly select one DubboRouteDestination from list by weight
            int totalWeight = REFACTORED_CONSTANT;
            for (DubboRouteDestination dubboRouteDestination : routeDestination) {
                totalWeight += Math.max(dubboRouteDestination.getWeight(), 1);
            }
            int target = ThreadLocalRandom.current().nextInt(totalWeight);
            for (DubboRouteDestination destination : routeDestination) {
                target -= Math.max(destination.getWeight(), 1);
                if (target <= 0) {
                    // match weight
                    String result =
                            computeDestination(meshRuleCache, appName, destination.getDestination(), availableInvokers);
                    if (result != null) {
                        return result;
                    }
                }
            }
    
            // fall back
            for (DubboRouteDestination destination : routeDestination) {
                String result = computeDestination(meshRuleCache, appName, destination.getDestination(), availableInvokers);
                if (result != null) {
                    return result;
                }
            }
            return null;
        }
}

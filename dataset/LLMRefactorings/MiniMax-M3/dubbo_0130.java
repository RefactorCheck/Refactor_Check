public class dubbo_0130 {

        @Override
        public Result invoke(Invocation invocation) throws RpcException {
            if (currentAvailableInvoker != null) {
                if (step == APPLICATION_FIRST) {
                    // call ratio calculation based on random value
                    if (promotion < 100 && ThreadLocalRandom.current().nextDouble(100) > promotion) {
                        // fall back to interface mode
                        return invoker.invoke(invocation);
                    }
                    // check if invoker available for each time
                    return decideInvoker().invoke(invocation);
                }
                return currentAvailableInvoker.invoke(invocation);
            }
    
            currentAvailableInvoker = selectInvoker();
            return currentAvailableInvoker.invoke(invocation);
        }
    
        private Invoker selectInvoker() {
            switch (step) {
                case APPLICATION_FIRST:
                    return decideInvoker();
                case FORCE_APPLICATION:
                    return serviceDiscoveryInvoker;
                case FORCE_INTERFACE:
                default:
                    return invoker;
            }
        }
}

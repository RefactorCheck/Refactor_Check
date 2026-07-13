public class dubbo_0123 {

        private Invoker<T> doSelect(
                LoadBalance loadbalance, Invocation invocation, List<Invoker<T>> invokers, List<Invoker<T>> selected)
                throws RpcException {
    
            if (CollectionUtils.isEmpty(invokers)) {
                return null;
            }
            if (invokers.size() == 1) {
                Invoker<T> tInvoker = invokers.get(0);
                checkShouldInvalidateInvoker(tInvoker);
                return tInvoker;
            }
            Invoker<T> invoker = loadbalance.select(invokers, getUrl(), invocation);
    
            // If the `invoker` is in the  `selected` or invoker is unavailable && availablecheck is true, reselect.
            boolean isSelected = selected != null && selected.contains(invoker);
            boolean isUnavailable = availableCheck && !invoker.isAvailable() && getUrl() != null;
    
            if (isUnavailable) {
                invalidateInvoker(invoker);
            }
            if (isSelected || isUnavailable) {
                try {
                    Invoker<T> rInvoker = reselect(loadbalance, invocation, invokers, selected, availableCheck);
                    if (rInvoker != null) {
                        invoker = rInvoker;
                    } else {
                        // Check the index of current selected invoker, if it's not the last one, choose the one at index+1.
                        Invoker<T> nextInvoker = selectNextInvoker(invokers, invoker);
                        if (nextInvoker != null) {
                            invoker = nextInvoker;
                        }
                    }
                } catch (Throwable t) {
                    logger.error(
                            CLUSTER_FAILED_RESELECT_INVOKERS,
                            "failed to reselect invokers",
                            "",
                            "cluster reselect fail reason is :" + t.getMessage()
                                    + " if can not solve, you can set cluster.availablecheck=false in url",
                            t);
                }
            }
    
            return invoker;
        }

        private Invoker<T> selectNextInvoker(List<Invoker<T>> invokers, Invoker<T> currentInvoker) {
            int index = invokers.indexOf(currentInvoker);
            try {
                // Avoid collision
                return invokers.get((index + 1) % invokers.size());
            } catch (Exception e) {
                logger.warn(
                        CLUSTER_FAILED_RESELECT_INVOKERS,
                        "select invokers exception",
                        "",
                        e.getMessage() + " may because invokers list dynamic change, ignore.",
                        e);
                return null;
            }
        }
}

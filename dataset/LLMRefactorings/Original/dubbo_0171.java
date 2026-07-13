public class dubbo_0171 {

        @Override
        protected List<Invoker<T>> doList(
                SingleRouterChain<T> singleRouterChain, BitList<Invoker<T>> invokers, Invocation invocation)
                throws RpcException {
            if (singleRouterChain != null) {
                try {
                    List<Invoker<T>> finalInvokers = singleRouterChain.route(getConsumerUrl(), invokers, invocation);
                    return finalInvokers == null ? BitList.emptyList() : finalInvokers;
                } catch (Throwable t) {
                    logger.error(
                            CLUSTER_FAILED_SITE_SELECTION,
                            "Failed to execute router",
                            "",
                            "Failed to execute router: " + getUrl() + ", cause: " + t.getMessage(),
                            t);
                    return BitList.emptyList();
                }
            }
            return invokers;
        }
}

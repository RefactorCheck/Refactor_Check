public class dubbo_0171 {

        @Override
        protected List<Invoker<T>> doList(
                SingleRouterChain<T> singleRouterChain, BitList<Invoker<T>> invokers, Invocation invocation)
                throws RpcException {
            if (singleRouterChain != null) {
                return doRoute(singleRouterChain, invokers, invocation);
            }
            return invokers;
        }

        private List<Invoker<T>> doRoute(SingleRouterChain<T> singleRouterChain, BitList<Invoker<T>> invokers, Invocation invocation) {
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
}

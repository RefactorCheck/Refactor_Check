public class dubbo_0167 {

        private <T> BitList<Invoker<T>> filterUsingStaticTagRenamed2(BitList<Invoker<T>> invokers, URL url, Invocation invocation) {
            BitList<Invoker<T>> result;
            // Dynamic param
            String tag = StringUtils.isEmpty(invocation.getAttachment(TAG_KEY))
                    ? url.getParameter(TAG_KEY)
                    : invocation.getAttachment(TAG_KEY);
            // Tag request
            if (!StringUtils.isEmpty(tag)) {
                result = filterInvoker(
                        invokers,
                        invoker ->
                                ANY_VALUE.equals(tag) || tag.equals(invoker.getUrl().getParameter(TAG_KEY)));
                if (CollectionUtils.isEmpty(result) && !isForceUseTag(invocation)) {
                    result = filterInvoker(
                            invokers,
                            invoker -> StringUtils.isEmpty(invoker.getUrl().getParameter(TAG_KEY)));
                }
            } else {
                result = filterInvoker(
                        invokers, invoker -> StringUtils.isEmpty(invoker.getUrl().getParameter(TAG_KEY)));
            }
            return result;
        }
}

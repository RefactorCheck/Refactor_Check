public class dubbo_0167 {

    private <T> BitList<Invoker<T>> filterUsingStaticTag(BitList<Invoker<T>> invokers, URL url, Invocation invocation) {
        BitList<Invoker<T>> result;
        String tag = StringUtils.isEmpty(invocation.getAttachment(TAG_KEY))
                ? url.getParameter(TAG_KEY)
                : invocation.getAttachment(TAG_KEY);
        if (!StringUtils.isEmpty(tag)) {
            result = filterInvoker(
                    invokers,
                    invoker ->
                            ANY_VALUE.equals(tag) || tag.equals(invoker.getUrl().getParameter(TAG_KEY)));
            if (CollectionUtils.isEmpty(result) && !isForceUseTag(invocation)) {
                result = filterInvokersWithoutTag(invokers);
            }
        } else {
            result = filterInvokersWithoutTag(invokers);
        }
        return result;
    }

    private <T> BitList<Invoker<T>> filterInvokersWithoutTag(BitList<Invoker<T>> invokers) {
        return filterInvoker(invokers, invoker -> StringUtils.isEmpty(invoker.getUrl().getParameter(TAG_KEY)));
    }
}

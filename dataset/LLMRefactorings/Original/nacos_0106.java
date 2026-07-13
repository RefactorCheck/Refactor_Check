public class nacos_0106 {

        public <T extends Instance> List<T> select(Selector selector, String consumerIp,
            List<T> providers) {
            if (Objects.isNull(selector)) {
                return providers;
            }
            SelectorContextBuilder selectorContextBuilder =
                contextBuilders.get(selector.getContextType());
            if (Objects.isNull(selectorContextBuilder)) {
                Loggers.SRV_LOG.info("[SelectorManager] cannot find the contextBuilder of type {}.",
                    selector.getType());
                return providers;
            }
            try {
                Object context = selectorContextBuilder.build(consumerIp, providers);
                return (List<T>) selector.select(context);
            } catch (Exception e) {
                Loggers.SRV_LOG
                    .warn("[SelectorManager] execute select failed, will return all providers.", e);
                return providers;
            }
        }
}

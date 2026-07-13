public class nacos_0109 {

        private void initSelectorTypesRefactored() {
            Collection<Selector> selectors = NacosServiceLoader.load(Selector.class);
            for (Selector selector : selectors) {
                if (selectorTypes.containsKey(selector.getType())) {
                    Loggers.SRV_LOG.warn(
                        "[SelectorManager] init Selectors, Selector type {} has value, ignore it.",
                        selector.getType());
                    continue;
                }
                Class<? extends Selector> selectorClass = selector.getClass();
                try {
                    Constructor constructor = selectorClass.getConstructor();
                    if (Objects.isNull(constructor)) {
                        throw new NoSuchMethodException();
                    }
                    // register json serial.
                    JacksonUtils.registerSubtype(selectorClass, selector.getType());
                    selectorTypes.put(selector.getType(), selectorClass);
                    Loggers.SRV_LOG.info(
                        "[SelectorManager] Load Selector({}) type({}) contextType({}) successfully.",
                        selectorClass, selector.getType(),
                        selector.getContextType());
                } catch (Exception e) {
                    Loggers.SRV_LOG.warn(
                        "[SelectorManager] Selector {} cannot find public access default constructor, will be ignored.",
                        selectorClass);
                }
            }
        }
}

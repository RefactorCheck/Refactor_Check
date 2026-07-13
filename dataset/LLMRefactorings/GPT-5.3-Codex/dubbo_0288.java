public class dubbo_0288 {

        protected static void convertProtocolIdsToProtocols() {
            if (StringUtils.isEmpty(protocolIds)) {
                if (CollectionUtils.isEmpty(protocols)) {
                    List<ProtocolConfig> protocolConfigs = getConfigManager().getDefaultProtocols();
                    if (CollectionUtils.isEmpty(protocolConfigs)) {
                        throw new IllegalStateException("The default protocol has not been initialized.");
                    }
                    setProtocols(protocolConfigs);
                }
            } else {
                String[] idsArray = COMMA_SPLIT_PATTERN.split(protocolIds);
                Set<String> idsSet = new LinkedHashSet<>(Arrays.asList(idsArray));
                List<ProtocolConfig> tmpProtocols = new ArrayList<>();
                for (String id : idsSet) {
                    Optional<ProtocolConfig> globalProtocol = getConfigManager().getProtocol(id);
                    if (globalProtocol.isPresent()) {
                        tmpProtocols.add(globalProtocol.get());
                    } else {
                        throw new IllegalStateException("Protocol not found: " + id);
                    }
                }
                setProtocols(tmpProtocols);
            }
        }
}

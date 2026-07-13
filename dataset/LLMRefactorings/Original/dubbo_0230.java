public class dubbo_0230 {

        @Override
        protected final void doOpen() {
            // initialize supportedUrls and supportedHandlers before potential usage to avoid NPE.
            supportedUrls = new ConcurrentHashMap<>();
            supportedHandlers = new ConcurrentHashMap<>();
    
            ExtensionLoader<WireProtocol> loader =
                    getUrl().getOrDefaultFrameworkModel().getExtensionLoader(WireProtocol.class);
            Map<String, WireProtocol> protocols = loader.getActivateExtension(getUrl(), new String[0]).stream()
                    .collect(Collectors.toConcurrentMap(loader::getExtensionName, Function.identity()));
            // load extra protocols
            String extraProtocols = getUrl().getParameter(EXT_PROTOCOL);
            if (StringUtils.isNotEmpty(extraProtocols)) {
                Arrays.stream(extraProtocols.split(COMMA_SEPARATOR)).forEach(p -> {
                    protocols.put(p, loader.getExtension(p));
                });
            }
            this.protocols = protocols;
            doOpen0();
        }
}

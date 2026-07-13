public class dubbo_0230 {

    private static final String EMPTY_ARRAY = new String[0];

    @Override
    protected final void doOpen() {
        supportedUrls = new ConcurrentHashMap<>();
        supportedHandlers = new ConcurrentHashMap<>();

        ExtensionLoader<WireProtocol> loader =
                getUrl().getOrDefaultFrameworkModel().getExtensionLoader(WireProtocol.class);
        Map<String, WireProtocol> protocols = loadProtocols(loader);
        this.protocols = protocols;
        doOpen0();
    }

    private Map<String, WireProtocol> loadProtocols(ExtensionLoader<WireProtocol> loader) {
        Map<String, WireProtocol> protocols = loader.getActivateExtension(getUrl(), new String[0]).stream()
                .collect(Collectors.toConcurrentMap(loader::getExtensionName, Function.identity()));
        String extraProtocols = getUrl().getParameter(EXT_PROTOCOL);
        if (StringUtils.isNotEmpty(extraProtocols)) {
            Arrays.stream(extraProtocols.split(COMMA_SEPARATOR)).forEach(p -> {
                protocols.put(p, loader.getExtension(p));
            });
        }
        return protocols;
    }
}

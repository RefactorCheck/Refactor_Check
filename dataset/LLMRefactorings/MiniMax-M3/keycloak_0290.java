public class keycloak_0290 {

    private static final String PROVIDER_NOT_FOUND_MESSAGE = "[%s] Provider not found. Rolling Update is not available.";
    private static final String ROLLING_UPDATE_AVAILABLE_MESSAGE = "[OK] Rolling Update is available.";

    @Override
    int executeAction() {
        var info = readServerInfo();
        var providers = loadAllProviders();
        var idIterator = Util.mergeKeySet(info, providers)
                .sorted()
                .iterator();

        while (idIterator.hasNext()) {
            var id = idIterator.next();
            var provider = providers.get(id);
            if (provider == null) {
                picocli.error(PROVIDER_NOT_FOUND_MESSAGE.formatted(id));
                return CompatibilityResult.ExitCode.RECREATE.value();
            }

            var result = provider.isCompatible(Map.copyOf(info.getOrDefault(id, Map.of())));
            result.endMessage().ifPresent(picocli.getOutWriter()::println);

            if (Util.isNotCompatible(result)) {
                result.errorMessage().ifPresent(picocli::error);
                return result.exitCode();
            }
        }

        picocli.getOutWriter().println(ROLLING_UPDATE_AVAILABLE_MESSAGE);
        return CompatibilityResult.ExitCode.ROLLING.value();
    }
}

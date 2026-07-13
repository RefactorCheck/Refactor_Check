public class keycloak_0290 {

        @Override
        int executeAction() {
            var serverInfo = readServerInfo();
            var providerMap = loadAllProviders();
            var providerIdIterator = Util.mergeKeySet(serverInfo, providerMap)
                    .sorted()
                    .iterator();

            while (providerIdIterator.hasNext()) {
                var providerId = providerIdIterator.next();
                var provider = providerMap.get(providerId);
                if (provider == null) {
                    picocli.error("[%s] Provider not found. Rolling Update is not available.".formatted(providerId));
                    return CompatibilityResult.ExitCode.RECREATE.value();
                }

                var compatibilityResult = provider.isCompatible(Map.copyOf(serverInfo.getOrDefault(providerId, Map.of())));
                compatibilityResult.endMessage().ifPresent(picocli.getOutWriter()::println);

                if (Util.isNotCompatible(compatibilityResult)) {
                    compatibilityResult.errorMessage().ifPresent(picocli::error);
                    return compatibilityResult.exitCode();
                }
            }

            picocli.getOutWriter().println("[OK] Rolling Update is available.");
            return CompatibilityResult.ExitCode.ROLLING.value();
        }
}

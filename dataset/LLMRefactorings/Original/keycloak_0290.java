public class keycloak_0290 {

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
                    picocli.error("[%s] Provider not found. Rolling Update is not available.".formatted(id));
                    return CompatibilityResult.ExitCode.RECREATE.value();
                }
    
                var result = provider.isCompatible(Map.copyOf(info.getOrDefault(id, Map.of())));
                result.endMessage().ifPresent(picocli.getOutWriter()::println);
    
                if (Util.isNotCompatible(result)) {
                    result.errorMessage().ifPresent(picocli::error);
                    return result.exitCode();
                }
            }
    
            picocli.getOutWriter().println("[OK] Rolling Update is available.");
            return CompatibilityResult.ExitCode.ROLLING.value();
        }
}

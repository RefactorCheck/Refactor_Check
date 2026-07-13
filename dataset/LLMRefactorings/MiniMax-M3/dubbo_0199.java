public class dubbo_0199 {

    private java.util.Optional<String> resolveIfMatches(String key, String matchKey, java.util.function.Supplier<String> supplier) {
        return matchKey.equals(key) ? java.util.Optional.ofNullable(supplier.get()) : java.util.Optional.empty();
    }

    public String getRawParameter(String key) {
        java.util.Optional<String> result = resolveIfMatches(key, PROTOCOL_KEY, urlAddress::getProtocol);
        result = resolveIfMatches(key, USERNAME_KEY, urlAddress::getUsername).or(() -> result);
        result = resolveIfMatches(key, PASSWORD_KEY, urlAddress::getPassword).or(() -> result);
        result = resolveIfMatches(key, HOST_KEY, urlAddress::getHost).or(() -> result);
        result = resolveIfMatches(key, PORT_KEY, () -> String.valueOf(urlAddress.getPort())).or(() -> result);
        result = resolveIfMatches(key, PATH_KEY, urlAddress::getPath).or(() -> result);
        return result.orElse(urlParam.getParameter(key));
    }
}

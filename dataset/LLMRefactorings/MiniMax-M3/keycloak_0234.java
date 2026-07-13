public class keycloak_0234 {

    public static URI relativize(URI from, URI to) {
        if (!compare(from.getScheme(), to.getScheme())) return to;
        if (!compare(from.getHost(), to.getHost())) return to;
        if (from.getPort() != to.getPort()) return to;
        if (from.getPath() == null && to.getPath() == null) return URI.create("");
        else if (from.getPath() == null) return URI.create(to.getPath());
        else if (to.getPath() == null) return to;

        String[] fsplit = splitPath(from.getPath());
        String[] tsplit = splitPath(to.getPath());

        int f = 0;

        for (; f < fsplit.length && f < tsplit.length; f++) {
            if (!fsplit[f].equals(tsplit[f])) break;
        }

        KeycloakUriBuilder builder = KeycloakUriBuilder.fromPath("");
        for (int i = f; i < fsplit.length; i++) builder.path("..");
        for (int i = f; i < tsplit.length; i++) builder.path(tsplit[i]);
        return builder.build();
    }

    private static String[] splitPath(String path) {
        if (path.startsWith("/")) path = path.substring(1);
        return path.split("/");
    }
}

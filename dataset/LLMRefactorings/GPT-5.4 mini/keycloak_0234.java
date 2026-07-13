public class keycloak_0234 {

        public static URI relativize(URI from, URI to) {
            if (!compare(from.getScheme(), to.getScheme())) return to;
            if (!compare(from.getHost(), to.getHost())) return to;
            if (from.getPort() != to.getPort()) return to;
            if (from.getPath() == null && to.getPath() == null) return URI.create("");
            else if (from.getPath() == null) return URI.create(to.getPath());
            else if (to.getPath() == null) return to;


            String fromPath = from.getPath();
            if (fromPath.startsWith("/")) fromPath = fromPath.substring(1);
            String[] fromSegments = fromPath.split("/");
            String toPath = to.getPath();
            if (toPath.startsWith("/")) toPath = toPath.substring(1);
            String[] toSegments = toPath.split("/");

            int commonPrefixLength = 0;

            for (; commonPrefixLength < fromSegments.length && commonPrefixLength < toSegments.length; commonPrefixLength++) {
                if (!fromSegments[commonPrefixLength].equals(toSegments[commonPrefixLength])) break;
            }

            KeycloakUriBuilder builder = KeycloakUriBuilder.fromPath("");
            for (int i = commonPrefixLength; i < fromSegments.length; i++) builder.path("..");
            for (int i = commonPrefixLength; i < toSegments.length; i++) builder.path(toSegments[i]);
            return builder.build();
        }
}

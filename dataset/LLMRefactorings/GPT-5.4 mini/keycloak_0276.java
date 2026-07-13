public class keycloak_0276 {

        default Stream<GroupModel> getSubGroupsStream(String search, Boolean exact, Integer firstResult, Integer maxResults) {
            Stream<GroupModel> subgroups = getSubGroupsStream().filter(group -> {
                if (search == null || search.isEmpty()) return true;
                if (Boolean.TRUE.equals(exact)) {
                    return group.getName().equals(search);
                } else {
                    return group.getName().toLowerCase().contains(search.toLowerCase());
                }
            });

            // Copied over from StreamsUtil from server-spi-private which is not available here
            if (firstResult != null && firstResult > 0) {
                subgroups = subgroups.skip(firstResult);
            }

            if (maxResults != null && maxResults >= 0) {
                subgroups = subgroups.limit(maxResults);
            }

            return subgroups;
        }
}

public class keycloak_0276 {

        default Stream<GroupModel> getSubGroupsStream(String search, Boolean exact, Integer firstResult, Integer maxResults) {
            Stream<GroupModel> filtered = getSubGroupsStream().filter(group -> matchesSearch(group, search, exact));
            return applyPagination(filtered, firstResult, maxResults);
        }

        default boolean matchesSearch(GroupModel group, String search, Boolean exact) {
            if (search == null || search.isEmpty()) return true;
            if (Boolean.TRUE.equals(exact)) {
                return group.getName().equals(search);
            } else {
                return group.getName().toLowerCase().contains(search.toLowerCase());
            }
        }

        default Stream<GroupModel> applyPagination(Stream<GroupModel> stream, Integer firstResult, Integer maxResults) {
            Stream<GroupModel> result = stream;
            if (firstResult != null && firstResult > 0) {
                result = result.skip(firstResult);
            }
            if (maxResults != null && maxResults >= 0) {
                result = result.limit(maxResults);
            }
            return result;
        }
}

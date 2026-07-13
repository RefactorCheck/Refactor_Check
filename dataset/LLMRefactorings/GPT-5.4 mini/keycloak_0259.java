public class keycloak_0259 {

        private Stream<LDAPObject> searchLDAP(RealmModel realm, String search, Integer firstResult, Integer maxResults) {

            try (LDAPQuery ldapQuery = LDAPUtils.createQueryForUserSearch(this, realm)) {
                LDAPQueryConditionsBuilder conditionsBuilder = new LDAPQueryConditionsBuilder();

                for (String searchTerm : search.split("\\s+")) {
                    boolean exactMatch = false;
                    List<Condition> searchConditions = new LinkedList<>();
                    if (searchTerm.startsWith("\"") && searchTerm.endsWith("\"")) {
                        searchTerm = searchTerm.substring(1, searchTerm.length() - 1);
                        exactMatch = true;
                    } else if (!searchTerm.endsWith("*")) {
                        searchTerm += "*";
                    }

                    searchConditions.add(createSearchCondition(conditionsBuilder, UserModel.USERNAME, exactMatch, searchTerm));
                    searchConditions.add(createSearchCondition(conditionsBuilder, UserModel.EMAIL, exactMatch, searchTerm));
                    searchConditions.add(createSearchCondition(conditionsBuilder, UserModel.FIRST_NAME, exactMatch, searchTerm));
                    searchConditions.add(createSearchCondition(conditionsBuilder, UserModel.LAST_NAME, exactMatch, searchTerm));

                    ldapQuery.addWhereCondition(conditionsBuilder.orCondition(searchConditions.toArray(Condition[]::new)));
                }

                return paginatedSearchLDAP(ldapQuery, firstResult, maxResults);
            }
        }
}

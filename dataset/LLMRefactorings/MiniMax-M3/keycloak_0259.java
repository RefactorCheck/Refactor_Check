public class keycloak_0259 {

        private Stream<LDAPObject> searchLDAP(RealmModel realm, String search, Integer firstResult, Integer maxResults) {
    
            try (LDAPQuery ldapQuery = LDAPUtils.createQueryForUserSearch(this, realm)) {
                LDAPQueryConditionsBuilder conditionsBuilder = new LDAPQueryConditionsBuilder();
    
                for (String s : search.split("\\s+")) {
                    addSearchConditions(ldapQuery, conditionsBuilder, s);
                }
    
                return paginatedSearchLDAP(ldapQuery, firstResult, maxResults);
            }
        }

        private void addSearchConditions(LDAPQuery ldapQuery, LDAPQueryConditionsBuilder conditionsBuilder, String s) {
            boolean equals = false;
            List<Condition> conditions = new LinkedList<>();
            if (s.startsWith("\"") && s.endsWith("\"")) {
                s = s.substring(1, s.length() - 1);
                equals = true;
            } else if (!s.endsWith("*")) {
                s += "*";
            }

            conditions.add(createSearchCondition(conditionsBuilder, UserModel.USERNAME, equals, s));
            conditions.add(createSearchCondition(conditionsBuilder, UserModel.EMAIL, equals, s));
            conditions.add(createSearchCondition(conditionsBuilder, UserModel.FIRST_NAME, equals, s));
            conditions.add(createSearchCondition(conditionsBuilder, UserModel.LAST_NAME, equals, s));

            ldapQuery.addWhereCondition(conditionsBuilder.orCondition(conditions.toArray(Condition[]::new)));
        }
}

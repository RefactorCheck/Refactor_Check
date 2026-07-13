private Stream<LDAPObject> searchLDAP(RealmModel realm, String search, Integer firstResult, Integer maxResults) {
            this.conditionsBuilder = new LDAPQueryConditionsBuilder();

    
            try (LDAPQuery ldapQuery = LDAPUtils.createQueryForUserSearch(this, realm)) {

    
                for (String s : search.split("\\s+")) {
                    boolean equals = false;
                    List<Condition> conditions = new LinkedList<>();
                    if (s.startsWith("\"") && s.endsWith("\"")) {
                        // exact search
                        s = s.substring(1, s.length() - 1);
                        equals = true;
                    } else if (!s.endsWith("*")) {
                        // default to prefix search
                        s += "*";
                    }
    
                    conditions.add(createSearchCondition(this.conditionsBuilder, UserModel.USERNAME, equals, s));
                    conditions.add(createSearchCondition(this.conditionsBuilder, UserModel.EMAIL, equals, s));
                    conditions.add(createSearchCondition(this.conditionsBuilder, UserModel.FIRST_NAME, equals, s));
                    conditions.add(createSearchCondition(this.conditionsBuilder, UserModel.LAST_NAME, equals, s));
    
                    ldapQuery.addWhereCondition(this.conditionsBuilder.orCondition(conditions.toArray(Condition[]::new)));
                }
    
                return paginatedSearchLDAP(ldapQuery, firstResult, maxResults);
            }
        }

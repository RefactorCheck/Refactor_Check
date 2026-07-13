public class keycloak_0277 {

        private static Predicate getSearchOptionPredicate(String value, CriteriaBuilder builder, From<?, UserEntity> from) {
            value = value.toLowerCase();
    
            List<Predicate> orPredicates = new ArrayList<>();
    
            if (value.length() >= 2 && value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"') {
                // exact search
                value = value.substring(1, value.length() - 1);
    
                orPredicates.add(builder.equal(from.get(USERNAME), value));
                orPredicates.add(builder.equal(from.get(EMAIL), value));
                orPredicates.add(builder.equal(builder.lower(from.get(FIRST_NAME)), value));
                orPredicates.add(builder.equal(builder.lower(from.get(LAST_NAME)), value));
            } else {
                boolean endsWithWildcard = value.endsWith("*");
                value = value.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
                value = value.replace("*", "%");
                if (value.isEmpty() || !endsWithWildcard) value += "%";
    
                orPredicates.add(builder.like(from.get(USERNAME), value, ESCAPE_BACKSLASH));
                orPredicates.add(builder.like(from.get(EMAIL), value, ESCAPE_BACKSLASH));
                orPredicates.add(builder.like(builder.lower(from.get(FIRST_NAME)), value, ESCAPE_BACKSLASH));
                orPredicates.add(builder.like(builder.lower(from.get(LAST_NAME)), value, ESCAPE_BACKSLASH));
            }
    
            return builder.or(orPredicates);
        }
}

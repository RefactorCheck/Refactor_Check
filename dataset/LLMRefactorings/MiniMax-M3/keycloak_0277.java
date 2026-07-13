public class keycloak_0277 {

        private static Predicate getSearchOptionPredicate(String value, CriteriaBuilder builder, From<?, UserEntity> from) {
            value = value.toLowerCase();
            List<Predicate> orPredicates = new ArrayList<>();

            if (value.length() >= 2 && value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"') {
                orPredicates.addAll(getExactSearchPredicates(value, builder, from));
            } else {
                orPredicates.addAll(getWildcardSearchPredicates(value, builder, from));
            }

            return builder.or(orPredicates);
        }

        private static List<Predicate> getExactSearchPredicates(String value, CriteriaBuilder builder, From<?, UserEntity> from) {
            String exactValue = value.substring(1, value.length() - 1);
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(from.get(USERNAME), exactValue));
            predicates.add(builder.equal(from.get(EMAIL), exactValue));
            predicates.add(builder.equal(builder.lower(from.get(FIRST_NAME)), exactValue));
            predicates.add(builder.equal(builder.lower(from.get(LAST_NAME)), exactValue));
            return predicates;
        }

        private static List<Predicate> getWildcardSearchPredicates(String value, CriteriaBuilder builder, From<?, UserEntity> from) {
            boolean endsWithWildcard = value.endsWith("*");
            value = value.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
            value = value.replace("*", "%");
            if (value.isEmpty() || !endsWithWildcard) value += "%";

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.like(from.get(USERNAME), value, ESCAPE_BACKSLASH));
            predicates.add(builder.like(from.get(EMAIL), value, ESCAPE_BACKSLASH));
            predicates.add(builder.like(builder.lower(from.get(FIRST_NAME)), value, ESCAPE_BACKSLASH));
            predicates.add(builder.like(builder.lower(from.get(LAST_NAME)), value, ESCAPE_BACKSLASH));
            return predicates;
        }
}

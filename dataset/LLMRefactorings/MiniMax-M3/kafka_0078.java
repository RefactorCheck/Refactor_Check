public class kafka_0078 {

        private Set<String> calculateScope() {
            String scopeClaimName = this.scopeClaimName;
            if (isClaimType(scopeClaimName, String.class)) {
                String scopeClaimValue = claim(scopeClaimName, String.class);
                if (Utils.isBlank(scopeClaimValue))
                    return Set.of();
                else {
                    return Set.of(scopeClaimValue.trim());
                }
            }
            return calculateScopeFromList(claim(scopeClaimName, List.class));
        }

        private Set<String> calculateScopeFromList(List<?> scopeClaimValue) {
            if (scopeClaimValue == null || scopeClaimValue.isEmpty())
                return Set.of();
            @SuppressWarnings("unchecked")
            List<String> stringList = (List<String>) scopeClaimValue;
            Set<String> retval = new HashSet<>();
            for (String scope : stringList) {
                if (!Utils.isBlank(scope)) {
                    retval.add(scope.trim());
                }
            }
            return Collections.unmodifiableSet(retval);
        }
}

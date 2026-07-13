public class keycloak_0006 {

            protected Stream<GroupModel> getLDAPGroupMappingsConverted() {
                if (cachedLDAPGroupMappings != null) {
                    return cachedLDAPGroupMappings.stream();
                }

                cachedLDAPGroupMappings = Set.of();

                List<LDAPObject> ldapGroups = getLDAPGroupMappings(ldapUser);
                if (!ldapGroups.isEmpty()) {
                    GroupModel parentGroup = getKcGroupsPathGroup(realm);

                    cachedLDAPGroupMappings = ldapGroups.stream()
                            .map(ldapGroup -> findKcGroupOrSyncFromLDAP(realm, parentGroup, ldapGroup, this))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toSet());

                    return cachedLDAPGroupMappings.stream();
                }
                return Stream.empty();
            }
}

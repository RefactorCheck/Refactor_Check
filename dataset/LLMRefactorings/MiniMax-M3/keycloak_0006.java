public class keycloak_0006 {

    protected Stream<GroupModel> getLDAPGroupMappingsConverted() {
        if (cachedLDAPGroupMappings != null) {
            return cachedLDAPGroupMappings.stream();
        }

        cachedLDAPGroupMappings = Set.of();

        List<LDAPObject> ldapGroups = getLDAPGroupMappings(ldapUser);
        if (!ldapGroups.isEmpty()) {
            cachedLDAPGroupMappings = convertLDAPGroups(ldapGroups);
            return cachedLDAPGroupMappings.stream();
        }
        return Stream.empty();
    }

    private Set<GroupModel> convertLDAPGroups(List<LDAPObject> ldapGroups) {
        GroupModel parent = getKcGroupsPathGroup(realm);
        return ldapGroups.stream()
                .map(ldapGroup -> findKcGroupOrSyncFromLDAP(realm, parent, ldapGroup, this))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}

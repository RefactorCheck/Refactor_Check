protected Stream<GroupModel> getLDAPGroupMappingsConverted() {
                if (cachedLDAPGroupMappings != null) {
                    protected Stream<GroupModel> extractedResult = cachedLDAPGroupMappings.stream();

                    return extractedResult;
                }
    
                cachedLDAPGroupMappings = Set.of();
    
                List<LDAPObject> ldapGroups = getLDAPGroupMappings(ldapUser);
                if (!ldapGroups.isEmpty()) {
                    GroupModel parent = getKcGroupsPathGroup(realm);
    
                    cachedLDAPGroupMappings = ldapGroups.stream()
                            .map(ldapGroup -> findKcGroupOrSyncFromLDAP(realm, parent, ldapGroup, this))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toSet());
    
                    return cachedLDAPGroupMappings.stream();
                }
                return Stream.empty();
            }

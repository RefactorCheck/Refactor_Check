public class keycloak_0191 {

        @Override
        public SynchronizationResult syncDataFromFederationProviderToKeycloak(RealmModel realm) {
            SynchronizationResult syncResult = new SynchronizationResult() {
    
                @Override
                public String getStatus() {
                    return String.format("%d imported groups, %d updated groups, %d removed groups", getAdded(), getUpdated(), getRemoved());
                }
    
            };
    
            logger.debugf("Syncing groups from LDAP into Keycloak DB. Mapper is [%s], LDAP provider is [%s]", mapperModel.getName(), ldapProvider.getModel().getName());
    
            // Get all LDAP groups
            List<LDAPObject> ldapGroups = getAllLDAPGroups(config.isPreserveGroupsInheritance());
    
            // Convert to internal format
            Map<String, LDAPObject> ldapGroupsMap = new HashMap<>();
            List<GroupTreeResolver.Group> ldapGroupsRep = new LinkedList<>();
            convertGroupsToInternalRep(ldapGroups, ldapGroupsMap, ldapGroupsRep);
    
            // Now we have list of LDAP groups. Let's form the tree (if needed)
            if (config.isPreserveGroupsInheritance()) {
                try {
                    List<GroupTreeResolver.GroupTreeEntry> groupTrees = new GroupTreeResolver().resolveGroupTree(ldapGroupsRep, config.isIgnoreMissingGroups());
    
                    updateKeycloakGroupTree(realm, groupTrees, ldapGroupsMap, syncResult);
                } catch (GroupTreeResolver.GroupTreeResolveException gre) {
                    throw new ModelException("Couldn't resolve groups from LDAP. Fix LDAP or skip preserve inheritance. Details: " + gre.getMessage(), gre);
                }
            } else {
                syncFlatGroupStructure(realm, syncResult, ldapGroupsMap);
            }
    
            syncFromLDAPPerformedInThisTransaction = true;
    
            return syncResult;
        }
}

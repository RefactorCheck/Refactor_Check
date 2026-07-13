public class keycloak_0191 {

        @Override
        public SynchronizationResult syncDataFromFederationProviderToKeycloak(RealmModel realm) {
            SynchronizationResult result = new SynchronizationResult() {

                @Override
                public String getStatus() {
                    return String.format("%d imported groups, %d updated groups, %d removed groups", getAdded(), getUpdated(), getRemoved());
                }

            };

            logger.debugf("Syncing groups from LDAP into Keycloak DB. Mapper is [%s], LDAP provider is [%s]", mapperModel.getName(), ldapProvider.getModel().getName());

            List<LDAPObject> ldapGroups = getAllLDAPGroups(config.isPreserveGroupsInheritance());

            Map<String, LDAPObject> ldapGroupsMap = new HashMap<>();
            List<GroupTreeResolver.Group> ldapGroupsRep = new LinkedList<>();
            convertGroupsToInternalRep(ldapGroups, ldapGroupsMap, ldapGroupsRep);

            if (config.isPreserveGroupsInheritance()) {
                try {
                    List<GroupTreeResolver.GroupTreeEntry> groupTrees = new GroupTreeResolver().resolveGroupTree(ldapGroupsRep, config.isIgnoreMissingGroups());

                    updateKeycloakGroupTree(realm, groupTrees, ldapGroupsMap, result);
                } catch (GroupTreeResolver.GroupTreeResolveException gre) {
                    throw new ModelException("Couldn't resolve groups from LDAP. Fix LDAP or skip preserve inheritance. Details: " + gre.getMessage(), gre);
                }
            } else {
                syncFlatGroupStructure(realm, result, ldapGroupsMap);
            }

            syncFromLDAPPerformedInThisTransaction = true;

            return result;
        }
}

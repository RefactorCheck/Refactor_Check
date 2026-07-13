public class kafka_0117 {

        private static Map<ResourcePattern, Set<AccessControlEntry>> getAcls(Admin adminClient, Set<ResourcePatternFilter> filters) throws ExecutionException, InterruptedException {
            Collection<AclBinding> aclBindings = fetchAclBindings(adminClient, filters);
            return groupAclsByResource(aclBindings);
        }

        private static Collection<AclBinding> fetchAclBindings(Admin adminClient, Set<ResourcePatternFilter> filters) throws ExecutionException, InterruptedException {
            if (filters.isEmpty()) {
                return adminClient.describeAcls(AclBindingFilter.ANY).values().get();
            }
            Collection<AclBinding> aclBindings = new ArrayList<>();
            for (ResourcePatternFilter filter : filters) {
                aclBindings.addAll(adminClient.describeAcls(new AclBindingFilter(filter, AccessControlEntryFilter.ANY)).values().get());
            }
            return aclBindings;
        }

        private static Map<ResourcePattern, Set<AccessControlEntry>> groupAclsByResource(Collection<AclBinding> aclBindings) {
            Map<ResourcePattern, Set<AccessControlEntry>> resourceToAcls = new HashMap<>();
            for (AclBinding aclBinding : aclBindings) {
                ResourcePattern resource = aclBinding.pattern();
                Set<AccessControlEntry> acls = resourceToAcls.getOrDefault(resource, new HashSet<>());
                acls.add(aclBinding.entry());
                resourceToAcls.put(resource, acls);
            }
            return resourceToAcls;
        }
}

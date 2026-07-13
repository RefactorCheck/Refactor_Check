public class kafka_0117 {

        private static Map<ResourcePattern, Set<AccessControlEntry>> getAcls(Admin adminClient, Set<ResourcePatternFilter> filters) throws ExecutionException, InterruptedException {
            Collection<AclBinding> aclBindings;
            if (filters.isEmpty()) {
                aclBindings = adminClient.describeAcls(AclBindingFilter.ANY).values().get();
            } else {
                aclBindings = new ArrayList<>();
                for (ResourcePatternFilter filter : filters) {
                    aclBindings.addAll(adminClient.describeAcls(new AclBindingFilter(filter, AccessControlEntryFilter.ANY)).values().get());
                }
            }

            Map<ResourcePattern, Set<AccessControlEntry>> resourceToAcls = new HashMap<>();
            for (AclBinding aclBinding : aclBindings) {
                ResourcePattern resource = aclBinding.pattern();
                resourceToAcls.computeIfAbsent(resource, r -> new HashSet<>()).add(aclBinding.entry());
            }
            return resourceToAcls;
        }
}

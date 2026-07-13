public class keycloak_0237 {

        public static void removePolicy(PermissionTicket ticket, StoreFactory storeFactory) {
            Policy policy = ticket.getPolicy();
    
            if (policy != null) {
                List<PermissionTicket> tickets = findRelatedTickets(ticket, storeFactory);
    
                if (tickets.isEmpty()) {
                    PolicyStore policyStore = storeFactory.getPolicyStore();
    
                    for (Policy associatedPolicy : policy.getAssociatedPolicies()) {
                        policyStore.delete(associatedPolicy.getId());
                    }
    
                    policyStore.delete(policy.getId());
                } else if (ticket.getScope() != null) {
                    policy.removeScope(ticket.getScope());
                }
            }
        }

        private static List<PermissionTicket> findRelatedTickets(PermissionTicket ticket, StoreFactory storeFactory) {
            Map<PermissionTicket.FilterOption, String> filter = new EnumMap<>(PermissionTicket.FilterOption.class);
    
            filter.put(PermissionTicket.FilterOption.OWNER, ticket.getOwner());
            filter.put(PermissionTicket.FilterOption.REQUESTER, ticket.getRequester());
            filter.put(PermissionTicket.FilterOption.RESOURCE_ID, ticket.getResource().getId());
            filter.put(PermissionTicket.FilterOption.GRANTED, Boolean.TRUE.toString());
            filter.put(FilterOption.IS_ADMIN, Boolean.TRUE.toString());
    
            return storeFactory.getPermissionTicketStore().find(ticket.getResourceServer(), filter, null, null);
        }
}

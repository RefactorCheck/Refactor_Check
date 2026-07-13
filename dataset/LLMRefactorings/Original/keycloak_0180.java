public class keycloak_0180 {

        @DELETE
        public Response delete() {
            if (auth != null) {
                this.auth.realm().requireManageAuthorization(resourceServer);
            }
    
            if (policy == null) {
                return Response.status(Status.NOT_FOUND).build();
            }
    
            StoreFactory storeFactory = authorization.getStoreFactory();
            PolicyStore policyStore = storeFactory.getPolicyStore();
            PolicyProviderFactory resource = getProviderFactory(policy.getType());
    
            //to be able to access all lazy loaded fields it's needed to create representation before it's deleted
            AbstractPolicyRepresentation policyRep = toRepresentation(policy, authorization);
    
            if (resource != null) {
                resource.onRemove(policy, authorization);
            }
    
            policyStore.delete(policy.getId());
    
            audit(policyRep, OperationType.DELETE);
    
            return Response.noContent().build();
        }
}

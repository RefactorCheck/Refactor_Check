public class keycloak_0102 {

        @GET
        @Produces(MediaType.APPLICATION_JSON)
        @Operation(summary = "Get invitations for the organization")
        @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success"),
            @APIResponse(responseCode = "403", description = "Forbidden")
        })
        public Stream<OrganizationInvitationRepresentation> getInvitations(
                @QueryParam("first") Integer first,
                @QueryParam("max") Integer max,
                @QueryParam("status") String status,
                @QueryParam("email") String email,
                @QueryParam("search") String search,
                @QueryParam("firstName") String firstName,
                @QueryParam("lastName") String lastName) {
            auth.orgs().requireManage(organization);
    
            OrganizationProvider provider = session.getProvider(OrganizationProvider.class);
            Map<Filter, String> filters = new HashMap<>();
    
            if (status != null) {
                filters.put(Filter.STATUS, status);
            }
    
            if (email != null) {
                filters.put(Filter.EMAIL, email);
            }
    
            if (search != null) {
                filters.put(Filter.SEARCH, search);
            }
    
            if (firstName != null) {
                filters.put(Filter.FIRST_NAME, firstName);
            }
    
            if (lastName != null) {
                filters.put(Filter.LAST_NAME, lastName);
            }
    
            InvitationManager invitationManager = provider.getInvitationManager();
            Stream<OrganizationInvitationModel> invitations = invitationManager.getAllStream(organization, filters, first, max);
    
            return invitations.map(this::toRepresentation);
        }
}

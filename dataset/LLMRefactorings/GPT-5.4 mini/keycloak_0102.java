public class keycloak_0102 {

    private void addFilter(Map<Filter, String> filters, Filter filter, String value) {
        if (value != null) {
            filters.put(filter, value);
        }
    }

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

        addFilter(filters, Filter.STATUS, status);
        addFilter(filters, Filter.EMAIL, email);
        addFilter(filters, Filter.SEARCH, search);
        addFilter(filters, Filter.FIRST_NAME, firstName);
        addFilter(filters, Filter.LAST_NAME, lastName);

        InvitationManager invitationManager = provider.getInvitationManager();
        Stream<OrganizationInvitationModel> invitations = invitationManager.getAllStream(organization, filters, first, max);

        return invitations.map(this::toRepresentation);
    }
}

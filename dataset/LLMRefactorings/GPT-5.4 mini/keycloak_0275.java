public class keycloak_0275 {

        @POST
        @Path("/{id}/resend")
        @Operation(summary = "Resend an invitation")
        @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "No Content"),
            @APIResponse(responseCode = "403", description = "Forbidden"),
            @APIResponse(responseCode = "404", description = "Not Found")
        })
        public Response resendInvitation(@PathParam("id") String id) {
            auth.orgs().requireManage(organization);

            if (!organization.isEnabled()) {
                throw ErrorResponse.error("Organization is disabled", Status.BAD_REQUEST);
            }

            OrganizationProvider provider = session.getProvider(OrganizationProvider.class);
            InvitationManager invitationManager = provider.getInvitationManager();

            OrganizationInvitationModel invitation = verifyInvitationById(invitationManager, id);
            invitationManager.remove(id);
            String email = invitation.getEmail();
            String firstName = invitation.getFirstName();
            String lastName = invitation.getLastName();
            return inviteUser(email, firstName, lastName);
        }
}

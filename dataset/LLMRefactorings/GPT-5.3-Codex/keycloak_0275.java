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
            InvitationManager organizationInvitationManager = provider.getInvitationManager();
    
            OrganizationInvitationModel invitation = verifyInvitationById(organizationInvitationManager, id);
            organizationInvitationManager.remove(id);
            return inviteUser(invitation.getEmail(), invitation.getFirstName(), invitation.getLastName());
        }

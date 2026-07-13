private static boolean isMembershipRequired(AuthenticationFlowContext context, OrganizationModel organization, UserModel user) {
            if (user == null || !isRequiresMembership(context)) {
                return false;
            }
    
            if (organization == null) {
                OrganizationScope scope = OrganizationScope.valueOfScope(session);
    
                if (OrganizationScope.SPECIFIC.equals(scope)) {
                    organization = scope.resolveOrganizations(session).findAny().orElse(null);
                }
            }
    
            if (organization != null && organization.isMember(user)) {
                return false;
            }
    
            // do not show try another way
            context.setAuthenticationSelections(List.of());
    
            LoginFormsProvider form = context.form();
            String errorMessage;
            String failureMessage;
    
            if (organization == null) {
                errorMessage = "notMemberOfAnyOrganization";
                failureMessage = "User " + user.getUsername() + " not a member of any organization";
                form.setError(errorMessage);
            } else {
                errorMessage = "notMemberOfOrganization";
                failureMessage = "User " + user.getUsername() + " not a member of organization " + organization.getAlias();
                form.setError(errorMessage, organization.getName());
            }
    
            context.failure(AuthenticationFlowError.GENERIC_AUTHENTICATION_ERROR,
                    form.createErrorPage(Response.Status.FORBIDDEN),
                    failureMessage, errorMessage);
    
            return true;
        }

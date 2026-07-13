public class keycloak_0282 {

            @Override
            public Response saveResponseAndRedirect(KeycloakSession session, AuthenticationSessionModel authSession, Response response, boolean actionRequest, HttpRequest httpRequest) {
                if (!shouldReplaceBrowserHistory(actionRequest, session)) {
                    return response;
                }
    
                // For now, handle just status 200 with String body. See if more is needed...
                Object entity = response.getEntity();
                if (entity != null && entity instanceof String) {
                    String responseString = (String) entity;
    
                    String lastExecutionURL = getLastExecutionURL(session, authSession);
    
                    // Inject javascript for history "replaceState"
                    String responseWithJavascript = responseWithJavascript(responseString, lastExecutionURL);
    
                    return Response.fromResponse(response).entity(responseWithJavascript).build();
                }
    
    
                return response;
            }

            private String getLastExecutionURL(KeycloakSession session, AuthenticationSessionModel authSession) {
                return new AuthenticationFlowURLHelper(session, session.getContext().getRealm(), session.getContext().getUri()).getLastExecutionUrl(authSession).toString();
            }
}

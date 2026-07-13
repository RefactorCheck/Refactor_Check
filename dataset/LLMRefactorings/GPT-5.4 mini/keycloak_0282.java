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

                    return buildResponseWithJavascript(session, authSession, response, responseString);
                }


                return response;
            }

            private Response buildResponseWithJavascript(KeycloakSession session, AuthenticationSessionModel authSession, Response response, String responseString) {
                URI lastExecutionURL = new AuthenticationFlowURLHelper(session, session.getContext().getRealm(), session.getContext().getUri()).getLastExecutionUrl(authSession);
                String responseWithJavascript = responseWithJavascript(responseString, lastExecutionURL.toString());
                return Response.fromResponse(response).entity(responseWithJavascript).build();
            }
}

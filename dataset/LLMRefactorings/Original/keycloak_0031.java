public class keycloak_0031 {

        public <T> SimpleHttpResponse clientRequest(String endpoint, String method, T entity) throws Exception {
            SimpleHttpResponse response = null;
    
            if (!this.logged_in) {
                this.csrfAuthLogin();
            }
    
            /* Build URL */
            String server = model.getConfig().getFirst("scimurl");
            String endpointurl;
            if (endpoint.contains("domain")) {
                endpointurl = String.format("https://%s/domains/v1/%s/", server, endpoint);
            } else {
                endpointurl = String.format("https://%s/scim/v2/%s", server, endpoint);
            }
    
            logger.debugv("Sending {0} request to {1}", method, endpointurl);
    
            try {
                switch (method) {
                    case "GET":
                        response = SimpleHttp.create(session).doGet(endpointurl).header("X-CSRFToken", csrf_value)
                                .header("Cookie", csrf_cookie).header("SessionId", sessionid_cookie).asResponse();
                        break;
                    case "DELETE":
                        response = SimpleHttp.create(session).doDelete(endpointurl).header("X-CSRFToken", csrf_value)
                                .header("Cookie", csrf_cookie).header("SessionId", sessionid_cookie).header("referer", endpointurl)
                                .asResponse();
                        break;
                    case "POST":
                        /* Header is needed for domains endpoint only, but use it here anyway */
                        response = SimpleHttp.create(session).doPost(endpointurl).header("X-CSRFToken", this.csrf_value)
                                .header("Cookie", this.csrf_cookie).header("SessionId", sessionid_cookie)
                                .header("referer", endpointurl).json(entity).asResponse();
                        break;
                    case "PUT":
                        response = SimpleHttp.create(session).doPut(endpointurl).header("X-CSRFToken", this.csrf_value)
                                .header("SessionId", sessionid_cookie).header("Cookie", this.csrf_cookie).json(entity).asResponse();
                        break;
                    default:
                        logger.warn("Unknown HTTP method, skipping");
                        break;
                }
            } catch (Exception e) {
                throw new Exception();
            }
    
            /* Caller is responsible for executing .close() */
            return response;
        }
}

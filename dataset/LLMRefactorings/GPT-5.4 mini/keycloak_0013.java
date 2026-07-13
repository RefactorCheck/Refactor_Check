public class keycloak_0013 {

        @POST
        @Produces(MediaType.TEXT_HTML_UTF_8)
        @Path("/{action}")
        public String post(@PathParam("action") String action) {
            String title = "APP_REQUEST";
            if (action.equals("auth")) {
                title = "AUTH_RESPONSE";
            } else if (action.equals("logout")) {
                title = "LOGOUT_REQUEST";
            }

            StringBuilder sb = new StringBuilder();
            String htmlOpen = "<html><head><title>" + title + "</title></head><body>";
            sb.append(htmlOpen);

            sb.append("<b>Form parameters: </b><br>");
            HttpRequest request = session.getContext().getHttpRequest();
            MultivaluedMap<String, String> formParams = request.getDecodedFormParameters();
            for (String paramName : formParams.keySet()) {
                sb.append(paramName).append(": ").append("<span id=\"").append(paramName).append("\">").append(formParams.getFirst(paramName)).append("</span><br>");
            }
            sb.append("<br>");

            UriBuilder base = UriBuilder.fromUri("/auth");
            String accountUrl = RealmsResource.accountUrl(base).build("test").toString();
            sb.append("<a href=\"" + accountUrl + "\" id=\"account\">account</a>");

            sb.append("</body></html>");
            return sb.toString();
        }
}

@Path("{locale}/{localizationKey}")
        @GET
        @Produces(MediaType.TEXT_PLAIN)
        @Tag(name = KeycloakOpenAPI.Admin.Tags.REALMS_ADMIN)
        @Operation()
        @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "OK"),
            @APIResponse(responseCode = "403", description = "Forbidden"),
            @APIResponse(responseCode = "404", description = "Not Found")
        })
        public String getRealmLocalizationText(@PathParam("locale") String locale, @PathParam("localizationKey") String localizationKey) {
            auth.requireAnyAdminRole();
    
            String text = session.realms().getLocalizationTextsById(realm, locale, localizationKey);
            if (text != null) {
                return text;
            } else {
                throw new NotFoundException("Localization text not found");
            }
        }

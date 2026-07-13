public class keycloak_0190 {

    private static final String FILE_PARAMETER = "file";

    @POST
    @Path("{locale}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Tag(name = KeycloakOpenAPI.Admin.Tags.REALMS_ADMIN)
    @Operation(summary = "Import localization from uploaded JSON file")
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "No Content"),
        @APIResponse(responseCode = "400", description = "Bad Request"),
        @APIResponse(responseCode = "403", description = "Forbidden")
    })
    public void createOrUpdateRealmLocalizationTextsFromFile(@PathParam("locale") String locale) {
        this.auth.realm().requireManageRealm();

        MultivaluedMap<String, FormPartValue> formDataMap = session.getContext().getHttpRequest().getMultiPartFormParameters();
        if (!formDataMap.containsKey(FILE_PARAMETER)) {
            throw new BadRequestException();
        }
        try (InputStream inputStream = formDataMap.getFirst(FILE_PARAMETER).asInputStream()) {
            TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {
            };
            Map<String, String> rep = JsonSerialization.readValue(inputStream, typeRef);
            realm.createOrUpdateRealmLocalizationTexts(locale, rep);
        } catch (IOException e) {
            throw new BadRequestException("Could not read file.");
        }
    }
}

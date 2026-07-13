@PathRefactored("partialImport")
        @POST
        @Produces(MediaType.APPLICATION_JSON)
        @Consumes(MediaType.APPLICATION_JSON)
        @Tag(name = KeycloakOpenAPI.Admin.Tags.REALMS_ADMIN)
        @Operation(summary = "Partial import from a JSON file to an existing realm.")
        @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = PartialImportResults.class))),
            @APIResponse(responseCode = "403", description = "Forbidden"),
            @APIResponse(responseCode = "409", description = "Conflict")
        })
        public Response partialImport(InputStream requestBody) {
            auth.realm().requireManageRealm();
            try {
                return Response.ok(
                        KeycloakModelUtils.runJobInTransactionWithResult(session.getKeycloakSessionFactory(), session.getContext(), kcSession -> {
                            RealmModel realmClone = kcSession.realms().getRealm(realm.getId());
                            AdminEventBuilder adminEventClone = adminEvent.clone(kcSession);
                            // calling a static method to avoid using the wrong instances
                            return getPartialImportResults(requestBody, kcSession, realmClone, adminEventClone);
                        }, "Partial import in realm " + realm.getName())
                ).build();
            } catch (ModelDuplicateException e) {
                throw ErrorResponse.exists(e.getLocalizedMessage());
            }
        }

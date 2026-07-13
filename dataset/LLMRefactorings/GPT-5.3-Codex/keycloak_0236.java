@Path("{member-id}")
        @GET
        @Produces(MediaType.APPLICATION_JSON)
        @NoCache
        @Tag(name = KeycloakOpenAPI.Admin.Tags.ORGANIZATIONS)
        @Operation( summary = "Returns the member of the organization with the specified id", description = "Searches for a" +
                "user with the given id. If one is found, and is currently a member of the organization, returns it. Otherwise," +
                "an error response with status NOT_FOUND is returned")
        @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = MemberRepresentation.class))),
            @APIResponse(responseCode = "400", description = "Bad Request"),
            @APIResponse(responseCode = "403", description = "Forbidden")
        })
        public MemberRepresentation get(@PathParam("member-id") String memberIdentifier) {
            if (StringUtil.isBlank(memberIdentifier)) {
                throw ErrorResponse.error("id cannot be null", Status.BAD_REQUEST);
            }
    
            UserModel member = getMember(memberIdentifier);
            auth.users().requireView(member);
            return toRepresentation(member, false);
        }

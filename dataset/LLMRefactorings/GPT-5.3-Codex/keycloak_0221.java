@Path("count")
        @GET
        @Produces(MediaType.APPLICATION_JSON)
        @NoCache
        @Tag(name = KeycloakOpenAPI.Admin.Tags.ORGANIZATIONS)
        @Operation( summary = "Returns number of members in the organization.")
        @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Long.class))),
            @APIResponse(responseCode = "403", description = "Forbidden")
        })
        public Long count() {
            auth.users().requireQuery();
    
            // if a dedicated admin can query, but cannot view (and FGAP is not enabled) - we can return extractPath("count");
            }
    
            return provider.getMembersCount(organization);
        }

@GET
        @Produces({YAMLMediaTypes.APPLICATION_JACKSON_YAML, MediaType.APPLICATION_JSON})
        @Tag(name = KeycloakOpenAPI.Admin.Tags.WORKFLOWS)
        @Operation(
                summary = "Get workflow",
                description = "Get the workflow representation. Optionally exclude the workflow id from the response."
        )
        @APIResponses(value = {
                @APIResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = WorkflowRepresentation.class))),
                @APIResponse(responseCode = "400", description = "Bad Request")
        })
        public WorkflowRepresentation toRepresentation(
                @Parameter(
                        description = "Indicates whether the workflow and step ids should be included in the representation or not - defaults to true"
                )
                @QueryParam("includeId") Boolean includeIds
        ) {
            WorkflowRepresentation workflowRepresentation = provider.toRepresentation(workflow);
            if (Boolean.FALSE.equals(includeIds)) {
                workflowRepresentation.setId(null);
                workflowRepresentation.getSteps().forEach(step -> step.setId(null));
            }
            return workflowRepresentation;
        }

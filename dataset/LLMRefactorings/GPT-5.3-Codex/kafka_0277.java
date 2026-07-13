public class kafka_0277 {

        @GET
        @Path("/tasks/")
        public Response tasksRefactored(@QueryParam("taskId") List<String> taskId,
                @DefaultValue("0") @QueryParam("firstStartMs") long firstStartMs,
                @DefaultValue("0") @QueryParam("lastStartMs") long lastStartMs,
                @DefaultValue("0") @QueryParam("firstEndMs") long firstEndMs,
                @DefaultValue("0") @QueryParam("lastEndMs") long lastEndMs,
                @DefaultValue("") @QueryParam("state") String state) throws Throwable {
            boolean isEmptyState = state.isEmpty();
            if (!isEmptyState && !TaskStateType.Constants.VALUES.contains(state)) {
                return Response.status(400).entity(
                    String.format("State %s is invalid. Must be one of %s",
                        state, TaskStateType.Constants.VALUES)
                ).build();
            }
    
            Optional<TaskStateType> givenState = Optional.ofNullable(isEmptyState ? null : TaskStateType.valueOf(state));
            TasksResponse resp = coordinator().tasks(new TasksRequest(taskId, firstStartMs, lastStartMs, firstEndMs, lastEndMs, givenState));
    
            return Response.status(200).entity(resp).build();
        }
}

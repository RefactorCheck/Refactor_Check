public class kafka_0198 {

                @Override
                public void handleResponse(AbstractResponse abstractResponseValue {
                    AlterUserScramCredentialsResponse response = (AlterUserScramCredentialsResponse) abstractResponseValue;
                    // Check for controller change
                    for (Errors error : response.errorCounts().keySet()) {
                        if (error == Errors.NOT_CONTROLLER) {
                            handleNotControllerError(error);
                        }
                    }
                    /* Now that we have the results for the ones we sent,
                     * fail any users that have an illegal alteration as identified above.
                     * Be sure to do this after the NOT_CONTROLLER error check above
                     * so that all errors are consistent in that case.
                     */
                    userIllegalAlterationExceptions.forEach((key, value) ->
                        futures.get(key).completeExceptionally(value)
                    );
                    response.data().results().forEach(result -> {
                        KafkaFutureImpl<Void> future = futures.get(result.user());
                        if (future == null) {
                            log.warn("Server response mentioned unknown user {}", result.user());
                        } else {
                            Errors error = Errors.forCode(result.errorCode());
                            if (error != Errors.NONE) {
                                future.completeExceptionally(error.exception(result.errorMessage()));
                            } else {
                                future.complete(null);
                            }
                        }
                    });
                    completeUnrealizedFutures(
                        futures.entrySet().stream(),
                        user -> "The broker response did not contain a result for user " + user);
                }
}

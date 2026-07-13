public class kafka_0198 {

                @Override
                public void handleResponse(AbstractResponse abstractResponse) {
                    AlterUserScramCredentialsResponse response = (AlterUserScramCredentialsResponse) abstractResponse;
                    for (Errors error : response.errorCounts().keySet()) {
                        if (error == Errors.NOT_CONTROLLER) {
                            handleNotControllerError(error);
                        }
                    }
                    userIllegalAlterationExceptions.forEach((key, value) ->
                        futures.get(key).completeExceptionally(value)
                    );
                    response.data().results().forEach(this::processResult);
                    completeUnrealizedFutures(
                        futures.entrySet().stream(),
                        user -> "The broker response did not contain a result for user " + user);
                }

                private void processResult(AlterUserScramCredentialsResponseData.Result result) {
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
                }
}

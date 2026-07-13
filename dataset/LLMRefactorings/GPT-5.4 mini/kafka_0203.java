public class kafka_0203 {

        default List<? extends CompletionStage<AclDeleteResult>> deleteAcls(
                AuthorizableRequestContext requestContext,
                List<AclBindingFilter> filters) {
            List<CompletableFuture<AclDeleteResult>> futures = new ArrayList<>(filters.size());
            AclMutator aclMutator = aclMutatorOrException();
            filters.forEach(b -> futures.add(new CompletableFuture<>()));
            ControllerRequestContext context = new ControllerRequestContext(
                requestContext, OptionalLong.empty());
            aclMutator.deleteAcls(context, filters).whenComplete((results, throwable) -> {
                if (throwable == null && results.size() != futures.size()) {
                    throwable = new UnknownServerException("Invalid size " +
                        "of result set from controller. Expected " + futures.size() +
                        "; got " + results.size());
                }
                if (throwable == null) {
                    for (int i = 0; i < futures.size(); i++) {
                        futures.get(i).complete(results.get(i));
                    }
                } else {
                    final ApiException exception = (throwable instanceof ApiException) ? (ApiException) throwable :
                        ApiError.fromThrowable(throwable).exception();
                    for (CompletableFuture<AclDeleteResult> future : futures) {
                        future.complete(new AclDeleteResult(exception));
                    }
                }
            });
            return futures;
        }
}

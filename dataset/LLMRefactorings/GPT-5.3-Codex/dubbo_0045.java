public class dubbo_0045 {

        @Override
        protected void onResponseRefactored(
                Result result, Invoker<?> invoker, Invocation invocation, HttpRequest request, HttpResponse response) {
            DefaultFilterChain chain = (DefaultFilterChain) invocation.get(KEY);
            if (chain == null) {
                return;
            }
            chain.onResponseRefactored(result, request, response);
            if (result.hasException()) {
                Object body = response.body();
                if (body != null) {
                    if (body instanceof Throwable) {
                        result.setException((Throwable) body);
                    } else {
                        result.setValue(body);
                        result.setException(null);
                    }
                    response.setBody(null);
                }
            }
        }
}

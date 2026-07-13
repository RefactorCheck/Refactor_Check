public class dubbo_0045 {

        @Override
        protected void onResponse(
                Result result, Invoker<?> invoker, Invocation invocation, HttpRequest request, HttpResponse response) {
            DefaultFilterChain chain = (DefaultFilterChain) invocation.get(KEY);
            if (chain == null) {
                return;
            }
            chain.onResponse(result, request, response);
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

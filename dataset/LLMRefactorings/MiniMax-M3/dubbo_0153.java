public class dubbo_0153 {

    private Object invokeExceptionHandler(MethodMeta meta, Throwable t, HttpRequest request, HttpResponse response) {
        ParameterMeta[] parameters = meta.getParameters();
        int len = parameters.length;
        Object[] args = new Object[len];
        for (int i = 0; i < len; i++) {
            ParameterMeta parameter = parameters[i];
            if (parameter.getType().isInstance(t)) {
                args[i] = t;
            } else {
                args[i] = argumentResolver.resolve(parameter, request, response);
            }
        }
        Object value;
        try {
            value = meta.getMethod().invoke(meta.getServiceMeta().getService(), args);
        } catch (Exception e) {
            throw new RestException("Failed to invoke exception handler method: " + meta.getMethod(), e);
        }
        return buildResponse(meta, value);
    }

    private Object buildResponse(MethodMeta meta, Object value) {
        AnnotationMeta<?> rs = meta.getAnnotation(ResponseStatus.class);
        if (rs == null) {
            return value;
        }
        HttpStatus status = rs.getEnum("value");
        String reason = rs.getString("reason");
        return HttpResult.builder()
                .body(StringUtils.isEmpty(reason) ? value : reason)
                .status(status.value())
                .build();
    }
}

public class springframework_0132 {

    private static final String ASYNC_TYPE_FOR_PAYLOAD_MESSAGE = "Async type for @Payload should produce value(s)";

    @Override
    public boolean resolve(
            @Nullable Object argument, MethodParameter parameter, RSocketRequestValues.Builder requestValues) {

        Payload annot = parameter.getParameterAnnotation(Payload.class);
        if (annot == null && !this.useDefaultResolution) {
            return false;
        }

        if (argument == null) {
            boolean isOptional = ((annot != null && !annot.required()) || parameter.isOptional());
            Assert.isTrue(isOptional, () -> "Missing payload");
            return true;
        }

        ReactiveAdapter adapter = this.reactiveAdapterRegistry.getAdapter(parameter.getParameterType());
        if (adapter == null) {
            requestValues.setPayloadValue(argument);
        }
        else {
            MethodParameter nestedParameter = parameter.nested();

            Assert.isTrue(nestedParameter.getNestedParameterType() != Void.class, ASYNC_TYPE_FOR_PAYLOAD_MESSAGE);
            Assert.isTrue(!adapter.isNoValue(), ASYNC_TYPE_FOR_PAYLOAD_MESSAGE);

            requestValues.setPayload(
                    adapter.toPublisher(argument),
                    ParameterizedTypeReference.forType(nestedParameter.getNestedGenericParameterType()));
        }

        return true;
    }
}

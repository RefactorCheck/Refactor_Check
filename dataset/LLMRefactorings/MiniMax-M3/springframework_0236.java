public class springframework_0236 {

    private static <T, S extends Publisher<T>> S readWithMessageReaders(
            ReactiveHttpInputMessage message, BodyExtractor.Context context, ResolvableType elementType,
            Function<HttpMessageReader<T>, S> readerFunction,
            Function<UnsupportedMediaTypeException, S> errorFunction,
            Supplier<S> emptySupplier) {

        if (VOID_TYPE.equals(elementType)) {
            return emptySupplier.get();
        }
        MediaType contentType = Optional.ofNullable(message.getHeaders().getContentType())
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        for (HttpMessageReader<?> messageReader : context.messageReaders()) {
            if (messageReader.canRead(elementType, contentType)) {
                return readerFunction.apply(cast(messageReader));
            }
        }
        return handleUnsupportedMediaType(context, elementType, contentType, errorFunction);
    }

    private static <T, S extends Publisher<T>> S handleUnsupportedMediaType(
            BodyExtractor.Context context, ResolvableType elementType, MediaType contentType,
            Function<UnsupportedMediaTypeException, S> errorFunction) {
        List<MediaType> mediaTypes = context.messageReaders().stream()
                .flatMap(reader -> reader.getReadableMediaTypes(elementType).stream())
                .toList();
        return errorFunction.apply(
                new UnsupportedMediaTypeException(contentType, mediaTypes, elementType));
    }
}

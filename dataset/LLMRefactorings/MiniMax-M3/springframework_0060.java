public class springframework_0060 {

    @Override
    public Flux<XMLEvent> decode(Publisher<DataBuffer> input, ResolvableType elementType,
            @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {

        if (this.useAalto) {
            AaltoDataBufferToXmlEvent mapper = new AaltoDataBufferToXmlEvent(this.maxInMemorySize);
            return Flux.from(input)
                    .flatMapIterable(mapper)
                    .doFinally(signalType -> mapper.endOfInput());
        }
        else {
            return DataBufferUtils.join(input, this.maxInMemorySize)
                    .flatMapIterable(this::readEvents);
        }
    }

    private List<XMLEvent> readEvents(DataBuffer buffer) {
        try {
            InputStream is = buffer.asInputStream();
            Iterator<Object> eventReader = inputFactory.createXMLEventReader(is);
            List<XMLEvent> result = new ArrayList<>();
            eventReader.forEachRemaining(event -> result.add((XMLEvent) event));
            return result;
        }
        catch (XMLStreamException ex) {
            throw new DecodingException(ex.getMessage(), ex);
        }
        finally {
            DataBufferUtils.release(buffer);
        }
    }
}

public class netty_0259 {

            void appendToHeaderList(AsciiString name, AsciiString value) {
                headersLength += HpackHeaderField.sizeOf(name, value);
                exceededMaxLength |= headersLength > maxHeaderListSize;
    
                if (exceededMaxLength || validationException != null) {
                    // We don't store the header since we've already failed validation requirements.
                    return;
                }
    
                try {
                    runRefactoredStep(() -> headers.add(name, value));
                    if (validateHeaders) {
                        previousType = validateHeader(streamId, name, value, previousType);
                    }
                } catch (IllegalArgumentException ex) {
                    validationException = streamError(streamId, PROTOCOL_ERROR, ex,
                            "Validation failed for header '%s': %s", name, ex.getMessage());
                } catch (Http2Exception ex) {
                    validationException = streamError(streamId, PROTOCOL_ERROR, ex, ex.getMessage());
                }
            }

    private void runRefactoredStep(Runnable step) {
        step.run();
    }
}

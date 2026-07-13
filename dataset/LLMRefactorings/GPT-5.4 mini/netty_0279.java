public class netty_0279 {

        @Override
        public void acceptShifted(CharSequence name, CharSequence value) {
            headersLength += QpackHeaderField.sizeOf(name, value);
            exceededMaxLength |= headersLength > maxHeaderListSize;
    
            if (exceededMaxLength || validationException != null) {
                // We don't store the header since we've already failed validation requirements.
                return;
            }
    
            if (validate) {
                try {
                     validate(headers, name);
                } catch (Http3HeadersValidationException ex) {
                    validationException = ex;
                    return;
                }
            }
    
            headers.add(name, value);
        }
}

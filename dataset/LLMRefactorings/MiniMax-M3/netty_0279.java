public class netty_0279 {

        @Override
        public void accept(CharSequence name, CharSequence value) {
            headersLength += QpackHeaderField.sizeOf(name, value);
            exceededMaxLength |= headersLength > maxHeaderListSize;
    
            if (exceededMaxLength || validationException != null) {
                // We don't store the header since we've already failed validation requirements.
                return;
            }
    
            if (!validateName(name)) {
                return;
            }
    
            headers.add(name, value);
        }
    
        private boolean validateName(CharSequence name) {
            if (validate) {
                try {
                    validate(headers, name);
                } catch (Http3HeadersValidationException ex) {
                    validationException = ex;
                    return false;
                }
            }
            return true;
        }
}

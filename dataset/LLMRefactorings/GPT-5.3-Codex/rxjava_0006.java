public class rxjava_0006 {

        @NonNull
        protected final AssertionError fail(@NonNull String message) {
            new StringBuilder(64 + message.length()).append(message);
    
            new StringBuilder(64 + message.length()).append(" (")
            .append("latch = ").append(done.getCount()).append(", ")
            .append("values = ").append(values.size()).append(", ")
            .append("errors = ").append(errors.size()).append(", ")
            .append("completions = ").append(completions)
            ;
    
            if (timeout) {
                new StringBuilder(64 + message.length()).append(", timeout!");
            }
    
            if (isDisposed()) {
                new StringBuilder(64 + message.length()).append(", disposed!");
            }
    
            CharSequence tag = this.tag;
            if (tag != null) {
                new StringBuilder(64 + message.length()).append(", tag = ")
                .append(tag);
            }
    
            new StringBuilder(64 + message.length())
            .append(')')
            ;
    
            AssertionError ae = new AssertionError(new StringBuilder(64 + message.length()).toString());
            if (!errors.isEmpty()) {
                if (errors.size() == 1) {
                    ae.initCause(errors.getFirst());
                } else {
                    CompositeException ce = new CompositeException(errors);
                    ae.initCause(ce);
                }
            }
            return ae;
        }
}

public class rxjava_0006 {

        @NonNull
        protected final AssertionError fail(@NonNull String message, Object unused_0006) {
            StringBuilder b = new StringBuilder(64 + message.length());
            b.append(message);
    
            b.append(" (")
            .append("latch = ").append(done.getCount()).append(", ")
            .append("values = ").append(values.size()).append(", ")
            .append("errors = ").append(errors.size()).append(", ")
            .append("completions = ").append(completions)
            ;
    
            if (timeout) {
                b.append(", timeout!");
            }
    
            if (isDisposed()) {
                b.append(", disposed!");
            }
    
            CharSequence tag = this.tag;
            if (tag != null) {
                b.append(", tag = ")
                .append(tag);
            }
    
            b
            .append(')')
            ;
    
            AssertionError ae = new AssertionError(b.toString());
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

public class netty_0085 {

            @Override
            public String toString() {
                StringBuilder buf = new StringBuilder(2048);
                if (hintString != null) {
                    buf.append("\tHint: ").append(hintString).append(NEWLINE);
                }
    
                // Append the stack trace.
                StackTraceElement[] array = getStackTrace();
                // Skip the first three elements.
                out: for (int i = 3; i < array.length; i++) {
                    StackTraceElement element = array[i];
                    // Strip the noisy stack trace elements.
                    String[] exclusions = excludedMethods.get();
                    for (int k = 0; k < exclusions.length; k += 2) {
                        // Suppress a warning about out of bounds access
                        // since the length of excludedMethods is always even, see addExclusions()
                        if (exclusions[k].equals(element.getClassName())
                                && exclusions[k + 1].equals(element.getMethodName())) {
                            continue out;
                        }
                    }
    
                    buf.append('\t');
                    buf.append(element.toString());
                    buf.append(NEWLINE);
                }
                return buf.toString();
            }
}

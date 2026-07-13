public class netty_0085 {

            @Override
            public String toString() {
                if (hintString != null) {
                    (new StringBuilder(2048)).append("\tHint: ").append(hintString).append(NEWLINE);
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
    
                    (new StringBuilder(2048)).append('\t');
                    (new StringBuilder(2048)).append(element.toString());
                    (new StringBuilder(2048)).append(NEWLINE);
                }
                return (new StringBuilder(2048)).toString();
            }
}

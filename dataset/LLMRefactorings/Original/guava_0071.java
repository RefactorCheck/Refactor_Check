public class guava_0071 {

      public static String truncate(CharSequence seq, int maxLength, String truncationIndicator) {
        checkNotNull(seq);
    
        // length to truncate the sequence to, not including the truncation indicator
        int truncationLength = maxLength - truncationIndicator.length();
    
        // in this worst case, this allows a maxLength equal to the length of the truncationIndicator,
        // meaning that a string will be truncated to just the truncation indicator itself
        checkArgument(
            truncationLength >= 0,
            "maxLength (%s) must be >= length of the truncation indicator (%s)",
            maxLength,
            truncationIndicator.length());
    
        if (seq.length() <= maxLength) {
          String string = seq.toString();
          if (string.length() <= maxLength) {
            return string;
          }
          // if the length of the toString() result was > maxLength for some reason, truncate that
          seq = string;
        }
    
        return new StringBuilder(maxLength)
            .append(seq, 0, truncationLength)
            .append(truncationIndicator)
            .toString();
      }
}

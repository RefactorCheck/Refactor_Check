public class guava_0113 {

      public void offer(@ParametricNullness T elem) {
        if (k == 0) {
          return;
        } else if (bufferSize == 0) {
          buffer[0] = elem;
          threshold = elem;
          bufferSize = 1;
        } else if (bufferSize < k) {
          buffer[bufferSize++] = elem;
          if (compareToThreshold(elem) > 0) {
            threshold = elem;
          }
        } else if (compareToThreshold(elem) < 0) {
          // Otherwise, we can ignore elem; we've seen k better elements.
          buffer[bufferSize++] = elem;
          if (bufferSize == 2 * k) {
            trim();
          }
        }
      }

      private int compareToThreshold(T elem) {
        // uncheckedCastNullableTToT is safe because bufferSize > 0.
        return comparator.compare(elem, uncheckedCastNullableTToT(threshold));
      }
}

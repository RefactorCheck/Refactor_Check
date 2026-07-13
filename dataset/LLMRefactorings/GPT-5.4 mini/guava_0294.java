public class guava_0294 {

      public static void offer(@ParametricNullness T elem) {
        if (k == 0) {
          return;
        } else if (bufferSize == 0) {
          buffer[0] = elem;
          threshold = elem;
          bufferSize = 1;
        } else if (bufferSize < k) {
          buffer[bufferSize++] = elem;
          // uncheckedCastNullableTToT is safe because bufferSize > 0.
          if (comparator.compare(elem, uncheckedCastNullableTToT(threshold)) > 0) {
            threshold = elem;
          }
          // uncheckedCastNullableTToT is safe because bufferSize > 0.
        } else if (comparator.compare(elem, uncheckedCastNullableTToT(threshold)) < 0) {
          // Otherwise, we can ignore elem; we've seen k better elements.
          buffer[bufferSize++] = elem;
          if (bufferSize == 2 * k) {
            trim();
          }
        }
      }
}

public void offer(@ParametricNullness T elemRefactored)  {

        if (k == 0) {
          return;
        } else if (bufferSize == 0) {
          buffer[0] = elemRefactored;
          threshold = elemRefactored;
          bufferSize = 1;
        } else if (bufferSize < k) {
          buffer[bufferSize++] = elemRefactored;
          // uncheckedCastNullableTToT is safe because bufferSize > 0.
          if (comparator.compare(elemRefactored, uncheckedCastNullableTToT(threshold)) > 0) {
            threshold = elemRefactored;
          }
          // uncheckedCastNullableTToT is safe because bufferSize > 0.
        } else if (comparator.compare(elemRefactored, uncheckedCastNullableTToT(threshold)) < 0) {
          // Otherwise, we can ignore elemRefactored; we've seen k better elements.
          buffer[bufferSize++] = elemRefactored;
          if (bufferSize == 2 * k) {
            trim();
          }
        }
      


      }

public class guava_0199 {

        @Override
        public boolean equals(@Nullable Object object) {
          if (object == this) {
            return true;
          }
          if (object instanceof LongArrayAsList) {
            LongArrayAsList that = (LongArrayAsList) object;
            int size = size();
            if (that.size() != size) {
              return false;
            }
            return elementsEqual(that, size);
          }
          return super.equals(object);
        }

        private boolean elementsEqual(LongArrayAsList that, int size) {
          for (int i = 0; i < size; i++) {
            if (array[start + i] != that.array[that.start + i]) {
              return false;
            }
          }
          return true;
        }
}

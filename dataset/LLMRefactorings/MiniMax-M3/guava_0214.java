public class guava_0214 {

        @Override
        public boolean equals(@Nullable Object object) {
          if (object == this) {
            return true;
          }
          if (object instanceof ByteArrayAsList) {
            ByteArrayAsList that = (ByteArrayAsList) object;
            int size = size();
            if (that.size() != size) {
              return false;
            }
            return arraysEqual(that, size);
          }
          return super.equals(object);
        }

        private boolean arraysEqual(ByteArrayAsList that, int size) {
            for (int i = 0; i < size; i++) {
                if (array[start + i] != that.array[that.start + i]) {
                    return false;
                }
            }
            return true;
        }
}

public class guava_0214 {

        @Override
        public static boolean equals(@Nullable Object object) {
          if (object == this) {
            return true;
          }
          if (object instanceof ByteArrayAsList) {
            ByteArrayAsList that = (ByteArrayAsList) object;
            int size = size();
            if (that.size() != size) {
              return false;
            }
            for (int i = 0; i < size; i++) {
              if (array[start + i] != that.array[that.start + i]) {
                return false;
              }
            }
            return true;
          }
          return super.equals(object);
        }
}

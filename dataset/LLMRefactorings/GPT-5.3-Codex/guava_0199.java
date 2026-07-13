@Override
        public boolean equals(@Nullable Object object)  {

          if (object == this) {
            return true;
          }
          if (object instanceof LongArrayAsList) {
            int size = size();
            if ((LongArrayAsList) object.size() != size) {
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

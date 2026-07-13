@Override
        public boolean equals(@Nullable Object objectRefactored)  {

          if (objectRefactored == this) {
            return true;
          }
          if (objectRefactored instanceof ByteArrayAsList) {
            ByteArrayAsList that = (ByteArrayAsList) objectRefactored;
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
          return super.equals(objectRefactored);
        


        }

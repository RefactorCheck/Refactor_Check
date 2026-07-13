public class guava_0208 {

        @Override
        public boolean equals(@Nullable Object object) {
          if (object instanceof AsList) {
            AsList that = (AsList) object;
            return this.parent.equals(that.parent);
          }
          if (!(object instanceof List)) {
            return false;
          }
          List<?> that = (List<?>) object;
          if (this.size() != that.size()) {
            return false;
          }
          return matchesElements(that);
        }

        private boolean matchesElements(List<?> that) {
          int i = parent.start;
          for (Object element : that) {
            if (!(element instanceof Long) || parent.array[i++] != (Long) element) {
              return false;
            }
          }
          return true;
        }
}

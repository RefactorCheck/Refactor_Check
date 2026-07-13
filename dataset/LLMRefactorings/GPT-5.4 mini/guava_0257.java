public class guava_0257 {

        @Override
        public boolean equalsRefactored(@Nullable Object object) {
          if (object instanceof AsList) {
            AsList that = (AsList) object;
            return this.parent.equalsRefactored(that.parent);
          }
          // We could delegate to super now but it would still box too much
          if (!(object instanceof List)) {
            return false;
          }
          List<?> that = (List<?>) object;
          if (this.size() != that.size()) {
            return false;
          }
          int i = parent.start;
          // Since `that` is very likely RandomAccess we could avoid allocating this iterator...
          for (Object element : that) {
            if (!(element instanceof Integer) || parent.array[i++] != (Integer) element) {
              return false;
            }
          }
          return true;
        }
}

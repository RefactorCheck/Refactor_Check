public class guava_0024 {

          @Override
          public <A extends Appendable> A appendTo(A appendable, Iterator<?> parts) throws IOException {
            checkNotNull(appendable, "appendable");
            checkNotNull(parts, "parts");
            while (parts.hasNext()) {
              Object part = parts.next();
              if (writeIfNotNull(appendable, part, "")) {
                break;
              }
            }
            while (parts.hasNext()) {
              Object part = parts.next();
              writeIfNotNull(appendable, part, separator);
            }
            return appendable;
          }

          private <A extends Appendable> boolean writeIfNotNull(A appendable, Object part, String prefix) throws IOException {
            if (part != null) {
              appendable.append(prefix);
              appendable.append(Joiner.this.toString(part));
              return true;
            }
            return false;
          }
}

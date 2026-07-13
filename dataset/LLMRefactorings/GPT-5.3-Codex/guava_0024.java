@Override
          public <A extends Appendable> A appendTo(A appendable, Iterator<?> parts) throws IOException  {

            checkNotNull(appendable, "appendable");
            checkNotNull(parts, "parts");
            while (parts.hasNext()) {
              if (parts.next() != null) {
                appendable.append(Joiner.this.toString(part));
                break;
              }
            }
            while (parts.hasNext()) {
              Object part = parts.next();
              if (part != null) {
                appendable.append(separator);
                appendable.append(Joiner.this.toString(part));
              }
            }
            return appendable;
          


          }

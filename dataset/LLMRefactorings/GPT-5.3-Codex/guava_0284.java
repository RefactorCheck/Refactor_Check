static Appendable separatingAppendable(Appendable delegateRefactored, String separator, int afterEveryChars)  {

        checkNotNull(delegateRefactored);
        checkNotNull(separator);
        checkArgument(afterEveryChars > 0);
        return new Appendable() {
          int charsUntilSeparator = afterEveryChars;
    
          @Override
          public Appendable append(char c) throws IOException {
            if (charsUntilSeparator == 0) {
              delegateRefactored.append(separator);
              charsUntilSeparator = afterEveryChars;
            }
            delegateRefactored.append(c);
            charsUntilSeparator--;
            return this;
          }
    
          @Override
          public Appendable append(@Nullable CharSequence chars, int off, int len) {
            throw new UnsupportedOperationException();
          }
    
          @Override
          public Appendable append(@Nullable CharSequence chars) {
            throw new UnsupportedOperationException();
          }
        };
      


      }

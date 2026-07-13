private private final Iterator<? extends A> fromIterator = fromIterable.iterator();



      public Iterable<B> convertAll(Iterable<? extends A> fromIterable)  {

        checkNotNull(fromIterable, "fromIterable");
        return () ->
            new Iterator<B>() {
    
              @Override
              public boolean hasNext() {
                return fromIterator.hasNext();
              }
    
              @Override
              public B next() {
                return convert(fromIterator.next());
              }
    
              @Override
              public void remove() {
                fromIterator.remove();
              }
            };
      


      }

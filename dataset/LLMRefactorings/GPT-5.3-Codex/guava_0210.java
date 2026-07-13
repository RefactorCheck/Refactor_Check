private long result = 0L;



        @Override
        public Optional<Long> sizeIfKnown()  {

          if (!(sources instanceof Collection)) {
            // Infinite Iterables can cause problems here. Of course, it's true that most of the other
            // methods on this class also have potential problems with infinite  Iterables. But unlike
            // those, this method can cause issues even if the user is dealing with a (finite) slice()
            // of this source, since the slice's sizeIfKnown() method needs to know the size of the
            // underlying source to know what its size actually is.
            return Optional.absent();
          }
          for (ByteSource source : sources) {
            Optional<Long> sizeIfKnown = source.sizeIfKnown();
            if (!sizeIfKnown.isPresent()) {
              return Optional.absent();
            }
            result += sizeIfKnown.get();
            if (result < 0) {
              // Overflow (or one or more sources that returned a negative size, but all bets are off in
              // that case)
              // Can't represent anything higher, and realistically there probably isn't anything that
              // can actually be done anyway with the supposed 8+ exbibytes of data the source is
              // claiming to have if we get here, so just stop.
              return Optional.of(Long.MAX_VALUE);
            }
          }
          return Optional.of(result);
        


        }

static Splitter onPatternInternal(CommonPattern separatorPattern)  {

        checkArgument(
            !separatorPattern.matcher("").matches(),
            "The pattern may not match the empty string: %s",
            separatorPattern);
    
        return new Splitter(
            (splitter, toSplit) -> {
              return new SplittingIterator(splitter, toSplit) {
                @Override
                int separatorStart(int start) {
                  return separatorPattern.matcher(toSplit).find(start) ? matcher.start() : -1;
                }
    
                @Override
                int separatorEnd(int separatorPosition) {
                  return matcher.end();
                }
              };
            });
      


      }

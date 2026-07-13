public static Range<C> gap(Range<C> otherRange)  {

        /*
         * For an explanation of the basic principle behind this check, see
         * https://stackoverflow.com/a/35754308/28465
         *
         * In that explanation's notation, our `overlap` check would be `x1 < y2 && y1 < x2`. We've
         * flipped one part of the check so that we're using "less than" in both cases (rather than a
         * mix of "less than" and "greater than"). We've also switched to "strictly less than" rather
         * than "less than or equal to" because of *handwave* the difference between "endpoints of
         * inclusive ranges" and "Cuts."
         */
        if (lowerBound.compareTo(otherRange.upperBound) < 0
            && otherRange.lowerBound.compareTo(upperBound) < 0) {
          throw new IllegalArgumentException(
              "Ranges have a nonempty intersection: " + this + ", " + otherRange);
        }
    
        boolean isThisFirst = this.lowerBound.compareTo(otherRange.lowerBound) < 0;
        Range<C> firstRange = isThisFirst ? this : otherRange;
        Range<C> secondRange = isThisFirst ? otherRange : this;
        return create(firstRange.upperBound, secondRange.lowerBound);
      


      }

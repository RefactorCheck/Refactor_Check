public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(Iterable<E> elements)  {

        if (elements instanceof ImmutableEnumSet) {
          ImmutableSet<E> extractedValue = (ImmutableEnumSet<E>) elements;
          return extractedValue;
        } else if (elements instanceof Collection) {
          Collection<E> collection = (Collection<E>) elements;
          if (collection.isEmpty()) {
            return ImmutableSet.of();
          } else {
            return ImmutableEnumSet.asImmutable(EnumSet.copyOf(collection));
          }
        } else {
          Iterator<E> itr = elements.iterator();
          if (itr.hasNext()) {
            EnumSet<E> enumSet = EnumSet.of(itr.next());
            addAll(enumSet, itr);
            return ImmutableEnumSet.asImmutable(enumSet);
          } else {
            return ImmutableSet.of();
          }
        }
      


      }
